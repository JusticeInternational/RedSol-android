package com.cleteci.redsolidaria.util

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.location.Location
import android.os.Build
import android.widget.Toast
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.ui.base.BaseFragment
import com.google.android.gms.location.*
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog
import android.os.Looper
import android.provider.Settings
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import android.location.LocationManager
import androidx.core.content.ContextCompat

import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.ContextCompat.getSystemService




class LocationResources(context: Context, val activity: Activity) : BaseFragment(),
    EasyPermissions.PermissionCallbacks {

    val currentLocation = MutableLiveData<Location>()
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)
    val PERMISSION_LOCATION_REQUEST_CODE = 1

    fun hasLocationPermission(): Boolean {
        return EasyPermissions.hasPermissions(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestLocationPermission() {
        EasyPermissions.requestPermissions(
            activity,
            activity.getString(R.string.position_permissed),
            PERMISSION_LOCATION_REQUEST_CODE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    @SuppressLint("MissingPermission")
    private fun reloadLocation() {
        locationRequest = LocationRequest.create()
        locationRequest?.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest?.setInterval(20 * 1000)
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.getLocations()) {
                    if (location != null) {
                        currentLocation.value = location
                    }
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    @SuppressLint("MissingPermission")
    fun loadLocation() {
        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                currentLocation.value = location
            } else {
                reloadLocation()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
    fun isLocationEnabled(context: Context): Boolean {
        val manager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            return false
        }
        return true
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionDenied(this, perms.first())) {
            SettingsDialog.Builder(requireActivity()).build().show()
        } else {
            requestLocationPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Toast.makeText(
            requireContext(),
            "Permission Granted!",
            Toast.LENGTH_SHORT
        ).show()
    }

}


