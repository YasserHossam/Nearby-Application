package com.yasser.nearby.ui.places

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.yasser.nearby.R
import com.yasser.nearby.core.ServiceLocator
import com.yasser.nearby.core.model.AppPlace
import com.yasser.nearby.ui.location.NearbyLocationManager
import com.yasser.nearby.ui.location.NearbyLocationManagerCallback
import com.yasser.nearby.ui.location.NearbyLocationManagerImpl
import com.yasser.nearby.ui.places.adapter.PlacesAdapter
import com.yasser.nearby.ui.utils.*
import kotlinx.android.synthetic.main.activity_feed.*


class PlacesActivity : AppCompatActivity(), PlacesContract.View, NearbyLocationManagerCallback {

    companion object {
        private const val REQUEST_PERMISSIONS_REQUEST_CODE = 51

        private const val UPDATE_DISTANCE = 500

        private const val LIST_STATE_KEY = "LIST_STATE"
    }

    private val presenter: PlacesContract.Presenter by lazy {
        PlacesPresenter(
            ServiceLocator.getInstance().getNearbyPlacesRepository(),
            ServiceLocator.getInstance().getModeRepository(),
            this
        )
    }

    private val nearbyLocationManager: NearbyLocationManager by lazy {
        NearbyLocationManagerImpl(this, UPDATE_DISTANCE)
    }

    /** Android Activity methods **/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        if(savedInstanceState != null)
            return

        initAdapter()

        initClickListeners()

        if (!checkPermissions()) {
            requestPermissions()
        } else {
            nearbyLocationManager.getLocation()
        }
    }

    override fun onStart() {
        super.onStart()
        if (presenter.isRealtimeMode())
            nearbyLocationManager.startLocationUpdates()
    }

    override fun onStop() {
        if (presenter.isRealtimeMode())
            nearbyLocationManager.stopLocationUpdates()
        super.onStop()
    }

    // Save recycler view position before rotation change
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        val listState = recyclerPlaces.layoutManager?.onSaveInstanceState()
        outState.putParcelable(LIST_STATE_KEY, listState)
    }

    // Restore recycler view position after rotation change
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val listState = savedInstanceState.getParcelable<Parcelable>(LIST_STATE_KEY)
        listState?.let {
            recyclerPlaces.layoutManager?.onRestoreInstanceState(listState)
        }
    }


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
                nearbyLocationManager.getLocation()
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


    /** Private Methods **/

    private fun initAdapter() {
        recyclerPlaces.layoutManager = LinearLayoutManager(this)
        recyclerPlaces.adapter = PlacesAdapter(arrayListOf())
    }

    private fun initClickListeners() {
        fabChangeMode.setOnClickListener { createChooseModeDialog() }
    }

    private fun createChooseModeDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialog_chang_mode_prompt)
            .setItems(presenter.getModes()) { _, i ->
                presenter.setModeByIndex(i)
            }
            .create()
            .show()
    }

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

    override fun onNewPlaceFetched(place: AppPlace) {
        val adapter = recyclerPlaces.adapter
        if (adapter is PlacesAdapter)
            adapter.addItem(place)
    }

    override fun onSingleModeTriggered() {
        tvMode.text = getString(R.string.activity_feed_mode_single)
        nearbyLocationManager.stopLocationUpdates()
    }

    override fun onRealtimeModeTriggered() {
        tvMode.text = getString(R.string.activity_feed_mode_realtime)
        nearbyLocationManager.startLocationUpdates()
    }

    override fun clearOldResults() {
        val adapter = recyclerPlaces.adapter
        if (adapter is PlacesAdapter)
            adapter.clear()
    }

    /** NearbyLocationManagerCallback implementation**/

    override fun onLocationFetched(location: Location) {
        presenter.getNearbyPlaces(location.latitude, location.longitude)
    }

    override fun onUpdatedLocationFetched(location: Location) {
        showSnackbar(parentView, getString(R.string.activity_walk_distance, UPDATE_DISTANCE))
        presenter.getNearbyPlaces(location.latitude, location.longitude)
    }
}
