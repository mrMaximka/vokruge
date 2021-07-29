package ru.gb.vokruge.ui.map

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.databinding.DataBindingUtil
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mapbox.android.core.permissions.PermissionsListener
import com.mapbox.android.core.permissions.PermissionsManager
import com.mapbox.api.geocoding.v5.MapboxGeocoding
import com.mapbox.mapboxsdk.annotations.MarkerOptions
import com.mapbox.mapboxsdk.camera.CameraPosition
import com.mapbox.mapboxsdk.geometry.LatLng
import com.mapbox.mapboxsdk.location.LocationComponentActivationOptions
import com.mapbox.mapboxsdk.location.LocationComponentOptions
import com.mapbox.mapboxsdk.location.modes.CameraMode
import com.mapbox.mapboxsdk.location.modes.RenderMode
import com.mapbox.mapboxsdk.maps.MapboxMap
import com.mapbox.mapboxsdk.maps.Style
import kotlinx.android.synthetic.main.map_fragment.*
import ru.gb.vokruge.R
import ru.gb.vokruge.databinding.MapFragmentBinding
import ru.gb.vokruge.model.MainModel
import ru.gb.vokruge.ui.map.MapViewModel.Companion.MIN_ZOOM
import ru.gb.vokruge.ui.map.MapViewModel.Companion.RESTRICTED_BOUNDS_AREA
import ru.gb.vokruge.ui.utils.Dimensions
import ru.gb.vokruge.ui.utils.NavigationUtils
import kotlin.math.roundToInt


class MapFragment : Fragment(), PermissionsListener {

    companion object {
        private val DETAIL_ZOOM_LEVEL = 16.0
    }

    private lateinit var viewModel: MapViewModel
    private var mapboxMap: MapboxMap? = null
    private lateinit var binding: MapFragmentBinding
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private var permissionsManager: PermissionsManager = PermissionsManager(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.map_fragment, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(MapViewModel::class.java)
        binding.model = viewModel

        viewModel.panelState().observe(this, Observer { changeState() })
        initMapView(savedInstanceState)

        initBottomSheetBehavior()
        initNavMenuButton()

        viewModel.needShowSearch.observe(this, Observer {
            it?.let {
                if (it) {
                    NavigationUtils.observerNavigation(
                        view,
                        it,
                        viewModel.needShowSearch,
                        R.id.action_mapFragment_to_searchFragment
                    )
                }
            }
        })

        viewModel.currentCompany().observe(this, Observer {
            refreshMarkers()
        })

        viewModel.onActivityCreated()

    }

    private fun refreshMarkers() {
        mapboxMap?.markers?.clear()
        viewModel.currentCompany().value?.let {
            if (it != null) {
                if (it.latitude != null && it.longitude != null) {
                    mapboxMap?.cameraPosition = CameraPosition.Builder()
                        .target(LatLng(it.latitude, it.longitude))
                        .zoom(DETAIL_ZOOM_LEVEL)
                        .build()
                    mapboxMap?.addMarker(
                        MarkerOptions().position(LatLng(it.latitude, it.longitude))
                    )
                }
            }
        }
    }

    private fun initNavMenuButton() {
        viewModel.needShowDrawer.observe(this, Observer {
            it?.let {
                if (it) {
                    val drawerLayout: DrawerLayout = activity!!.findViewById(R.id.container)
                    nav_menu_btn.setOnClickListener { drawerLayout.openDrawer(GravityCompat.START) }
                }
            }
        })
    }

    private fun initBottomSheetBehavior() {
        bottomSheetBehavior = BottomSheetBehavior.from(list_fragment_frame)
        bottomSheetBehavior.setBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_HIDDEN -> {
                        ivShowList.visibility = View.VISIBLE
                    }
                    else -> {
                        ivShowList.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
            }
        })
    }

    private fun changeState() {
        category_short_fragment.visibility = View.GONE
        detail_short_fragment_frame.visibility = View.GONE
        list_fragment_frame.visibility = View.GONE
        ivShowList.visibility = View.GONE

        when (viewModel.panelState().value) {

            MainModel.States.SHORT_CATEGORY -> {
                category_short_fragment.visibility = View.VISIBLE
                close_search.visibility = View.GONE
                back_menu_btn.visibility = View.GONE
                nav_menu_btn.visibility = View.VISIBLE
            }

            MainModel.States.SHORT_DETAIL -> {
                detail_short_fragment_frame.visibility = View.VISIBLE
                close_search.visibility = View.VISIBLE
                back_menu_btn.visibility = View.VISIBLE
                nav_menu_btn.visibility = View.GONE
            }

            MainModel.States.LIST_VISIBLE -> {
                list_fragment_frame.visibility = View.VISIBLE
                close_search.visibility = View.VISIBLE
                back_menu_btn.visibility = View.VISIBLE
                nav_menu_btn.visibility = View.GONE
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }

    private fun initMapView(savedInstanceState: Bundle?) {
        mapView?.onCreate(savedInstanceState)
        mapView?.getMapAsync { mapboxMap ->
            this.mapboxMap = mapboxMap
            initCompassPosition()
            mapboxMap.setLatLngBoundsForCameraTarget(RESTRICTED_BOUNDS_AREA)
            mapboxMap.setMinZoomPreference(MIN_ZOOM)
            mapboxMap.addOnCameraMoveListener {
                viewModel.cameraPosition = mapboxMap.cameraPosition
            }
            mapboxMap.setStyle(Style.MAPBOX_STREETS) {
                mapboxMap.cameraPosition = viewModel.cameraPosition
                refreshLocationComponent()
                refreshMarkers()
            }
            mapboxMap.addOnMapClickListener {
                viewModel.onMapClick(it, mapboxMap.cameraPosition.zoom)
                true
            }
            viewModel.enableLocation.observe(this, Observer { refreshLocationComponent() })
            viewModel.needZoomIn.observe(this, Observer {
                if (viewModel.needZoomIn.value!!) {
                    setZoom(mapboxMap.cameraPosition.zoom * 1.1)
                    viewModel.needZoomIn.value = false
                }
            })
            viewModel.needZoomOut.observe(this, Observer {
                if (viewModel.needZoomOut.value!!) {
                    setZoom(mapboxMap.cameraPosition.zoom / 1.1)
                    viewModel.needZoomOut.value = false
                }
            })

            val mapboxGeocoding = MapboxGeocoding.builder()
                .accessToken("pk.eyJ1IjoibWFrbGFrb3ZzcyIsImEiOiJjanlzZ3Y0NWgwazI2M2NudXRlM2gyMWdyIn0.xD2wM0QkXozDYpA7kezyCg")
                .query("1600 Pennsylvania Ave NW")
                .build()
        }
    }

    private fun initCompassPosition() {
        val dpMarginInPx = Dimensions.convertDpToPixel(16f, context!!).roundToInt()
        val dpTopInPx = Dimensions.convertDpToPixel(130f, context!!).roundToInt()
        mapboxMap?.uiSettings?.setCompassMargins(
            dpMarginInPx,
            dpTopInPx,
            dpMarginInPx,
            dpMarginInPx
        )
    }

    private fun setZoom(zoom: Double) {
        mapboxMap?.cameraPosition = CameraPosition.Builder()
            .target(mapboxMap?.cameraPosition?.target)
            .zoom(zoom)
            .build()
    }

    @SuppressLint("MissingPermission")
    private fun refreshLocationComponent() {
        mapboxMap?.style?.apply {
            if (viewModel.enableLocation.value!!) {
                if (PermissionsManager.areLocationPermissionsGranted(requireContext())) {
                    mapboxMap?.locationComponent?.apply {
                        if (!isLocationComponentActivated) {
                            initLocationComponent()
                        }
                        isLocationComponentEnabled = true
                    }
                } else {
                    permissionsManager = PermissionsManager(this@MapFragment)
                    permissionsManager.requestLocationPermissions(this@MapFragment.activity)
                }
            } else {
                mapboxMap?.locationComponent?.apply {
                    if (isLocationComponentActivated) isLocationComponentEnabled = false
                }
            }
        }
    }

    private fun initLocationComponent() {
        mapboxMap?.style?.apply {
            val customLocationComponentOptions = LocationComponentOptions.builder(requireContext())
                .trackingGesturesManagement(true)
                .accuracyColor(ContextCompat.getColor(requireContext(), R.color.colorPrimary))
                .build()

            val locationComponentActivationOptions =
                LocationComponentActivationOptions.builder(requireContext(), this)
                    .locationComponentOptions(customLocationComponentOptions)
                    .build()

            mapboxMap?.locationComponent?.apply {
                activateLocationComponent(locationComponentActivationOptions)
                cameraMode = CameraMode.TRACKING
                renderMode = RenderMode.COMPASS
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onExplanationNeeded(permissionsToExplain: List<String>) {
        Toast.makeText(
            requireContext(),
            R.string.user_location_permission_explanation,
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onPermissionResult(granted: Boolean) {
        if (granted) {
            refreshLocationComponent()
        } else {
            Toast.makeText(
                requireContext(),
                R.string.user_location_permission_not_granted,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun closeAppBar() {
        (activity as AppCompatActivity).apply {
            supportActionBar?.hide()
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
            window.clearFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
        closeAppBar()
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView?.onPause()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    override fun onDestroyView() {
        viewModel.onDestroyView()
        mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView?.onSaveInstanceState(outState)
    }

    fun onBackPressed(): Boolean {
        return viewModel.onBackSearchClick()
    }
}
