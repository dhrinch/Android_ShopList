package com.example.myshoplist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent

class GeofenceReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val geoEvent = GeofencingEvent.fromIntent(intent)
        val triggering = geoEvent.triggeringGeofences
        for( geo in triggering) {
            Log.i("geofence", "Geofence with id: ${geo.requestId} triggered.")
        }
        when (geoEvent.geofenceTransition) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> {
                Log.i("geofences", "Entered ${geoEvent.triggeringLocation.toString()}")
            }
            Geofence.GEOFENCE_TRANSITION_EXIT -> {
                Log.i("geofences", "Exited ${geoEvent.triggeringLocation.toString()}")
            }
            else -> {Log.e("geofences", "Error.")
            }
        }
    }
}
