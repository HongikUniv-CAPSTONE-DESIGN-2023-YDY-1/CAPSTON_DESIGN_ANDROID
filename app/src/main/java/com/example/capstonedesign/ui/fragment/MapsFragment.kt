package com.example.capstonedesign.ui.fragment



import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.capstonedesign.R
import com.example.capstonedesign.data.api.KakaoClient
import com.example.capstonedesign.data.api.KakaoInterface
import com.example.capstonedesign.data.response.PlaceInfo
import com.example.capstonedesign.databinding.FragmentMapsBinding
import com.example.capstonedesign.utils.Constants
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback



class MapsFragment(): Fragment(), OnMapReadyCallback{

    private lateinit var binding: FragmentMapsBinding

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private val defaultLocation = LatLng(37.5665, 126.9780)
    private var lastKnownLocation: Location? = null
    private var locationPermissionGranted = false
    private var currentLatitude: Double = 0.0
    private var currentLongitude: Double = 0.0
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                locationPermissionGranted = true
                updateLocationUI()

            } else {
                locationPermissionGranted = false
                updateLocationUI()
                Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMapsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

        } else {
            getLocationPermission()
        }


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as com.google.android.gms.maps.SupportMapFragment?
        mapFragment?.getMapAsync(this)




    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        updateLocationUI()

        val brand = arguments?.getString("brand")
        Log.d("brand", brand.toString())
        getDeviceLocation(brand!!)


    }
    @SuppressWarnings("MissingPermission")
    private fun updateLocationUI() {

        try {
            if (locationPermissionGranted) {
                mMap.isMyLocationEnabled = true
                mMap.uiSettings.isMyLocationButtonEnabled = true
            } else {
                mMap.isMyLocationEnabled = false
                mMap.uiSettings.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }
    @SuppressWarnings("MissingPermission")
    private fun getDeviceLocation(brand: String) {
        try {
            if (locationPermissionGranted){
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()) { task ->
                    if (task.isSuccessful) {
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            currentLongitude = lastKnownLocation!!.longitude
                            currentLatitude = lastKnownLocation!!.latitude
                            mMap.moveCamera(
                                com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                            mMap.addMarker(
                                MarkerOptions().position(
                                    LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)
                                ).title("현재 위치")


                            )
                            searchPlace(lastKnownLocation!!.latitude,lastKnownLocation!!.longitude , 1000, brand)




                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.")
                            if (task.exception != null) {
                                Log.e("TAG", "Exception: ${task.exception}")
                            }
                            else {
                                Log.e("TAG", "Exception: task.exception is null")
                            }
                            mMap.moveCamera(
                                com.google.android.gms.maps.CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                            )
                            mMap.uiSettings.isMyLocationButtonEnabled = false
                        }
                    }
                }


            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message.toString())
        }
    }
    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
        } else {
            requestPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    private fun searchPlace(x: Double, y: Double, radius: Int, query: String){
        val retrofit = KakaoClient.getClient("https://dapi.kakao.com")
        val api = retrofit?.create(KakaoInterface::class.java)
        val format = "json"
        val call = api?.getPlaceLatLng(format, x, y, radius, query, Constants.KAKAO_API_KEY)

        call?.enqueue(object : Callback<PlaceInfo> {
            override fun onResponse(call: Call<PlaceInfo>, response: Response<PlaceInfo>){
                Log.d("api", "call 함수 실행")
                if (response.isSuccessful) {
                    val result = response.body()
                    Log.d("api", "성공 : ${result.toString()}")

                    result?.documents?.forEach {
                        val latLng = LatLng(it.y.toDouble(), it.x.toDouble())
                        mMap.addMarker(MarkerOptions().position(latLng).title(it.place_name))
                    }


                } else {
                    Log.d("api", "실패")
                }
            }

            override fun onFailure(call: Call<PlaceInfo>, t: Throwable) {
                Log.d("api", "연결 실패 : $t")
            }
        })
    }



    companion object {
        private const val DEFAULT_ZOOM = 15
    }
}