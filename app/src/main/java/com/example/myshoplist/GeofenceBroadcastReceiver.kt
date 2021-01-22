package com.example.myshoplist

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofenceStatusCodes
import com.google.android.gms.location.GeofencingEvent

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private val TAG = "GeofenceTransitionsIS"

    override fun onReceive(context: Context?, intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            val errorMessage = GeofenceStatusCodes
                .getStatusCodeString(geofencingEvent.errorCode)
            Log.e(TAG, errorMessage)
            return
        }

        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT ||
            geofenceTransition == Geofence.GEOFENCE_TRANSITION_DWELL
        ) {

            // Get the geofences that were triggered. A single event can trigger
            // multiple geofences.
            val triggeringGeofences = geofencingEvent.triggeringGeofences

            // Get the transition details as a String.
            val geofenceTransitionDetails = getGeofenceTransitionDetails(
                    context,
                    geofenceTransition,
                    triggeringGeofences
            )

            // Send notification and log the transition details.
            Toast.makeText(
                    context,
                    geofenceTransitionDetails,
                    Toast.LENGTH_SHORT
            ).show()
            /*sendNotification(geofenceTransitionDetails)
            Log.i(TAG, geofenceTransitionDetails)*/
        } /*else {
            // Log the error.
            Log.e(
                    TAG, context!!.getString(
                    R.string.geofence_transition_invalid_type,
                    geofenceTransition
            )
            )
        }*/
    }
    private fun getGeofenceTransitionDetails(
            context: Context?,
            geofenceTransition: Int,
            triggeringGeofences: List<Geofence>): String {
        val geofenceTransitionString: String = getTransitionString(context, geofenceTransition)

        // Get the Ids of each geofence that was triggered.
        val triggeringGeofencesIdsList: ArrayList<String?> = ArrayList()
        for (geofence in triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.requestId)
        }
        val triggeringGeofencesIdsString = TextUtils.join(", ", triggeringGeofencesIdsList)
        return "$geofenceTransitionString: $triggeringGeofencesIdsString"
    }

    private fun getTransitionString(context: Context?, transitionType: Int): String {
        return when (transitionType) {
            Geofence.GEOFENCE_TRANSITION_ENTER -> context!!.getString(R.string.geofence_transition_entered)
            Geofence.GEOFENCE_TRANSITION_EXIT -> context!!.getString(R.string.geofence_transition_exited)
            Geofence.GEOFENCE_TRANSITION_DWELL -> context!!.getString(R.string.geofence_transition_dwell)
            else -> context!!.getString(R.string.unknown_geofence_transition)
        }
    }
}
