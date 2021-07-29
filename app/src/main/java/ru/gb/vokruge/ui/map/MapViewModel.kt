package ru.gb.vokruge.ui.map

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.geometry.LatLngBounds
import ru.gb.vokruge.di.App
import ru.gb.vokruge.model.MainModel
import javax.inject.Inject

class MapViewModel : ViewModel() {

    companion object {
        val MIN_ZOOM = 10.0
        val BOUND_CORNER_NW = LatLng(59.78, 28.84)
        val BOUND_CORNER_SE = LatLng(59.95, 29.19)
        val INITIAL_CENTER_POSITION: CameraPosition = CameraPosition.Builder()
            .target(LatLng(59.87, 29.0))
            .zoom(10.0)
            .build()
        val RESTRICTED_BOUNDS_AREA: LatLngBounds = LatLngBounds.Builder()
            .include(BOUND_CORNER_NW)
            .include(BOUND_CORNER_SE)
            .build()
    }

    @Inject
    lateinit var model: MainModel

    var lastClickZoom = 0.0f
    var lastClickLongitude = 0.0f
    var lastClickLatitude = 0.0f

    var cameraPosition = INITIAL_CENTER_POSITION

    var enableLocation = MutableLiveData<Boolean>().apply { value = false }

    var needListShow = MutableLiveData<Boolean>().apply { value = false }
    var needZoomIn = MutableLiveData<Boolean>().apply { value = false }
    var needZoomOut = MutableLiveData<Boolean>().apply { value = false }
    var needShowDrawer = MutableLiveData<Boolean>().apply { value = false }
    var needShowSearch = MutableLiveData<Boolean>().apply { value = false }


    init {
        App.appComponent.inject(this)
    }

    fun currentCompany() = model.currentCompany

    fun onZoomInClick() {
        needZoomIn.value = true
    }

    fun onZoomOutClick() {
        needZoomOut.value = true
    }

    fun onLocationClick() {
        enableLocation.value = !(enableLocation.value!!)
    }

    fun onClearSearchClick() {
        panelState().value = MainModel.States.SHORT_CATEGORY
    }

    fun onShowDrawerClick() {
        needShowDrawer.value = true
    }

    fun onSearchClick() {
        needShowSearch.value = true
    }

    fun onMapClick(point: LatLng, zoom: Double) {
        lastClickZoom = zoom.toFloat()
        lastClickLatitude = point.latitude.toFloat()
        lastClickLongitude = point.longitude.toFloat()
        needListShow.value = true
    }

    fun onShowListClick() {
        panelState().value = MainModel.States.LIST_VISIBLE
    }

    fun onActivityCreated() {
        model.mapVisible = true
    }

    fun onBackSearchClick() = model.onBackSearchClick()

    fun panelState() = model.panelState

    fun onDestroyView() {
        model.mapVisible = false
    }

}
