package com.yasser.nearby.ui.feed

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import com.yasser.nearby.R
import com.yasser.nearby.core.ServiceLocator
import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.ui.utils.*
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity(), FeedContract.View {

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 51
    }

    private val presenter: FeedPresenter by lazy {
        FeedPresenter(ServiceLocator.getInstance().getNearbyPlacesRepository(), this)
    }

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            initLocationClient()
            getLastKnownLocation()
        }
    }

    /** Private Methods **/

    private fun checkPermissions(): Boolean {
        return isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun requestPermissions() {
        if (shouldProvideRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
            showSnackbar(
                parentView,
                getString(R.string.location_permission_rational),
                getString(R.string.ok),
                Snackbar.LENGTH_INDEFINITE
            ) {
                requestPermission(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    REQUEST_PERMISSIONS_REQUEST_CODE
                )
            }
        } else {
            requestPermission(
                Manifest.permission.ACCESS_FINE_LOCATION,
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    private fun initLocationClient() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun getLastKnownLocation() {
        progress.visibility = View.VISIBLE
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            location?.let {
               presenter.getNearbyPlaces(it.latitude, it.longitude)
            }
        }
    }

    /** AppCompatActivity methods **/

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (permissions.size == 1 &&
                permissions[0] == Manifest.permission.ACCESS_FINE_LOCATION &&
                grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                initLocationClient()
                getLastKnownLocation()
            } else {
                showSnackbar(
                    parentView,
                    getString(R.string.location_permission_denied_explanation),
                    getString(R.string.settings),
                    Snackbar.LENGTH_INDEFINITE
                ) { goToMobileSettings() }
            }
        }
    }

    /** FeedContractView Implementation**/
    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showNoNetworkView() {

    }

    override fun showNoResultsView() {

    }

    override fun showGeneralErrorMessage() {

    }

    override fun onPlaceFetched(places: AppPlace) {
        if (recyclerPlaces.adapter == null) {
            /* InitAdapter */
        } else {
            /* Update adapter*/
        }


    }
}
