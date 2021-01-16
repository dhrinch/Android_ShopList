package com.example.myshoplist

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentSender
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import java.util.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener, AddShopDialogFragment.AddDialogListener {

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private var shopList: MutableList<ShopItem> = ArrayList()
    private lateinit var adapter: ShopListAdapter
    private lateinit var sp: SharedPreferences
    private lateinit var db: RoomDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(permissions, 0)
        }
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                //placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))
            }
        }
        var id = 0
        btn_addFavorite.setOnClickListener {
            val radius = 100F
            fusedLocationClient.lastLocation
                .addOnCompleteListener {
                    val place = LatLng(it.result.latitude, it.result.longitude)
                    val marker = MarkerOptions()
                    marker.position(place)
                    marker.title(et_placeName.text.toString())
                    marker.snippet(getAddress(place))
                    map.addMarker(marker)
                    map.moveCamera(CameraUpdateFactory.newLatLng(place))

                    val dialog = AddShopDialogFragment()
                    //val bundle = Bundle()

                    //bundle.putFloat("radius", radius)
                    //bundle.putString("lat", place.latitude.toString())
                    //bundle.putString("long", place.longitude.toString())

                    //dialog.arguments = bundle

                    /*val intent = Intent(this@MapsActivity, AddShopDialogFragment::class.java)
                    intent.putExtra("lat", place.latitude.toString())
                    intent.putExtra("long", place.longitude.toString())
                    startActivity(intent)*/
                    dialog.show(supportFragmentManager, "AskNewItemDialogFragment")

                    val geo = Geofence.Builder().setRequestId("Geofence${id++}")
                        .setCircularRegion(
                            place.latitude,
                            place.longitude,
                            radius
                        )
                            .setExpirationDuration(60 * 60 * 1000)
                            .setTransitionTypes(
                                Geofence.GEOFENCE_TRANSITION_ENTER or
                                Geofence.GEOFENCE_TRANSITION_EXIT
                            )
                        .build()
                    val geoClient = LocationServices.getGeofencingClient(this)
                    geoClient.addGeofences(getGeofencingRequest(geo), getGeofencePendingIntent())
                        .addOnSuccessListener {
                            Toast.makeText(
                                this@MapsActivity,
                                "Geofence added at${getAddress(place)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this@MapsActivity,
                                "Failed to add geofence at${getAddress(place)}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }
        }

        db = Room.databaseBuilder(applicationContext, RoomDB::class.java, "shops_db").build()

        createLocationRequest()
    }

    private fun getAddress(place: LatLng): String{
        val geocoder = Geocoder(this, Locale.getDefault())
        return geocoder.getFromLocation(place.latitude, place.longitude, 1)[0].getAddressLine(0)
    }

    private fun getGeofencingRequest(geofence: Geofence): GeofencingRequest{
        return GeofencingRequest.Builder()
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .addGeofence(geofence)
            .build()
    }

    private fun getGeofencePendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            this,
            0,
            Intent(this, GeofenceReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)

        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        this@MapsActivity,
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        map.isMyLocationEnabled = true
        map.mapType = GoogleMap.MAP_TYPE_NORMAL

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                //placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }
    }

   /* private fun placeMarkerOnMap(location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        val titleStr = getAddress(location)
        markerOptions.title(titleStr)
        map.addMarker(markerOptions)
    }*/

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true
        map.setOnMarkerClickListener(this)
        map.isMyLocationEnabled = true

        setUpMap()
    }

    override fun onDialogPositiveClick(item: ShopItem) {
        val handler = Handler(Handler.Callback {
            Toast.makeText(applicationContext, it.data.getString("message"), Toast.LENGTH_SHORT)
                .show()
            adapter.update(shopList)
            true
        })
        Thread(Runnable {

            val id = db.DAO().insert(item)
            item.id = id.toInt()
            shopList.add(item)
            val message = Message.obtain()
            message.data.putString("message", "Shop added to list")
            handler.sendMessage(message)
            adapter.notifyDataSetChanged()
        }).start()
    }

    override fun onMarkerClick(p0: Marker?) = false

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }
}