package com.yasser.nearby.ui.places

import com.yasser.nearby.core.InvalidCredentialsException
import com.yasser.nearby.core.NetworkException
import com.yasser.nearby.core.NoResultsException
import com.yasser.nearby.core.repository.mode.ApplicationMode
import com.yasser.nearby.core.repository.mode.ModeRepository
import com.yasser.nearby.core.repository.places.NearbyPlacesRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.math.BigDecimal
import java.math.RoundingMode

class PlacesPresenter(
    private val nearbyPlacesRepository: NearbyPlacesRepository,
    private val modeRepository: ModeRepository,
    private val view: PlacesContract.View
) : PlacesContract.Presenter {

    companion object {
        private const val SEARCH_RADIUS = 1000
    }

    init {
        val currentMode = modeRepository.getCurrentMode()
        if (currentMode == ApplicationMode.SINGLE)
            view.onSingleModeTriggered()
        else if (currentMode == ApplicationMode.REALTIME)
            view.onRealtimeModeTriggered()

    }

    private val compositeDisposable = CompositeDisposable()

    override fun getNearbyPlaces(latitude: Double, longitude: Double) {
        view.showProgress()
        view.hideErrorViews()
        view.clearOldResults()

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
                        view.onNewPlaceFetched(it)
                    },
                    { exception ->
                        view.hideProgress()
                        when (exception) {
                            is NetworkException -> view.showNoNetworkView()
                            is NoResultsException -> view.showNoResultsView()
                            is InvalidCredentialsException -> {
                                view.showInvalidCredentialsView()
                                view.showGeneralErrorView()
                            }
                            else -> view.showGeneralErrorView()
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

    override fun setMode(@ApplicationMode mode: Int) {
        modeRepository.chooseMode(mode)
        when (mode) {
            ApplicationMode.SINGLE -> view.onSingleModeTriggered()
            ApplicationMode.REALTIME -> view.onRealtimeModeTriggered()
        }
    }

    override fun isRealtimeMode(): Boolean {
        val currentMode = modeRepository.getCurrentMode()
        return currentMode == ApplicationMode.REALTIME
    }

    // Construct string from latitude and longitude values
    private fun constructLatLongString(latitude: Double, longitude: Double): String {
        val latitudePrecision = BigDecimal(latitude).setScale(5, RoundingMode.HALF_EVEN)
        val longitudePrecision = BigDecimal(longitude).setScale(5, RoundingMode.HALF_EVEN)
        return "$latitudePrecision,$longitudePrecision"
    }
}