package com.yasser.nearby.ui.places

import com.yasser.nearby.core.model.AppPlace


interface PlacesContract {

    interface View {
        fun showProgress()

        fun hideProgress()

        fun showNoNetworkView()

        fun showNoResultsView()

        fun showGeneralErrorMessage()

        fun onNewPlaceFetched(place: AppPlace)

        fun onSingleModeTriggered()

        fun onRealtimeModeTriggered()

        fun clearOldResults()

        fun hideErrorViews()
    }

    interface Presenter {
        fun getNearbyPlaces(latitude: Double, longitude: Double)

        fun onDestroy()

        fun getModes(): Array<String>

        fun setModeByIndex(i: Int)

        fun isRealtimeMode(): Boolean
    }
}