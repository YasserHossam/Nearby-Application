package com.yasser.nearby.ui.feed

import com.yasser.nearby.core.model.AppPlace


interface FeedContract {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showNoNetworkView()
        fun showNoResultsView()
        fun showGeneralErrorMessage()
        fun onPlaceFetched(places: AppPlace)
    }

    interface Presenter {
        fun getNearbyPlaces(latitude: Double, longitude: Double)

        fun onDestroy()
    }
}