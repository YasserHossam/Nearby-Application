package com.yasser.nearby.ui.places

import com.yasser.nearby.core.NetworkException
import com.yasser.nearby.core.NoResultsException
import com.yasser.nearby.core.repository.places.NearbyPlacesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode

class PlacesPresenter(
    private val nearbyPlacesRepository: NearbyPlacesRepository,
    private val view: PlacesContract.View
) : PlacesContract.Presenter {

    companion object {
        private const val SEARCH_RADIUS = 1000
    }

    private val compositeDisposable = CompositeDisposable()

    override fun getNearbyPlaces(latitude: Double, longitude: Double) {
        view.showProgress()

        val nearbyPlacesObservable = nearbyPlacesRepository.getNearbyPlaces(
            constructLatLongString(latitude, longitude),
            SEARCH_RADIUS
        )

        compositeDisposable.add(
            nearbyPlacesObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    {
                        view.onPlaceFetched(it)
                    },
                    { exception ->
                        view.hideProgress()
                        when (exception) {
                            is NetworkException -> view.showNoNetworkView()
                            is NoResultsException -> view.showNoResultsView()
                            else -> view.showGeneralErrorMessage()
                        }
                    },
                    {
                        view.hideProgress()
                    })
        )
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
    }

    // Construct string from latitude and longitude values
    private fun constructLatLongString(latitude: Double, longitude: Double): String {
        val latitudePrecision = BigDecimal(latitude).setScale(5, RoundingMode.HALF_EVEN)
        val longitudePrecision = BigDecimal(longitude).setScale(5, RoundingMode.HALF_EVEN)
        return "$latitudePrecision,$longitudePrecision"
    }
}