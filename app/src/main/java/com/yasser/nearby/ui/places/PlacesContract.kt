package com.yasser.nearby.ui.places

import com.yasser.nearby.core.model.AppPlace


interface PlacesContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showNoNetworkView()

        fun showNoResultsView()

        fun showGeneralErrorView()

        fun onNewPlaceFetched(place: AppPlace)

        fun onSingleModeTriggered()

        fun onRealtimeModeTriggered()

        fun clearOldResults()

        fun hideErrorViews()

        fun showInvalidCredentialsView()
    }

    interface Presenter {
        fun getNearbyPlaces(latitude: Double, longitude: Double)

        fun onDestroy()

        fun setMode(mode: Int)

        fun isRealtimeMode(): Boolean
    }
}