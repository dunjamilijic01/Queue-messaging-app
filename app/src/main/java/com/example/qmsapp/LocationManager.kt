package com.example.qmsapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.CancellationToken
import com.google.android.gms.tasks.CancellationTokenSource
import com.google.android.gms.tasks.OnTokenCanceledListener

class LocationManager(val context:Context) {

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    var location:LatLng? =LatLng(0.0,0.0)

    fun findLocation( callback:(LatLng?) ->Unit ) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.getCurrentLocation(
                LocationRequest.PRIORITY_HIGH_ACCURACY,
                object : CancellationToken() {
                    override fun onCanceledRequested(p0: OnTokenCanceledListener): CancellationToken {
                        callback(null)
                        return CancellationTokenSource().token
                    }


                    override fun isCancellationRequested(): Boolean {
                        callback(null)
                        return false
                    }
                })
                .addOnSuccessListener { l: android.location.Location? ->
                    if (l == null)
                        Toast.makeText(context, "Cannot get location.", Toast.LENGTH_SHORT).show()
                    else {
                        val lat = l.latitude
                        val lon = l.longitude
                        location = LatLng(lat, lon)
                        callback(LatLng(lat, lon))
                    }

                }

        }

    }

}