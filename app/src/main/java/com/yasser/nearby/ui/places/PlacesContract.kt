package com.yasser.nearby.ui.places

import com.yasser.nearby.core.model.AppPlace


interface PlacesContract {

    interface View {
        fun showProgress()
        fun hideProgress()
        fun showNoNetworkView()
        fun showNoResultsView()
        fun showGeneralErrorMessage()
        fun onPlaceFetched(place: AppPlace)
    }

    interface Presenter {
        fun getNearbyPlaces(latitude: Double, longitude: Double)

        fun onDestroy()
    }
}