package com.example.capstonedesign.ui



import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.api.KakaoClient
import com.example.capstonedesign.api.KakaoInterface
import com.example.capstonedesign.databinding.FragmentCameraResultBinding
import com.example.capstonedesign.model.ItemSearchViewModel
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.response.PlaceInfo
import com.example.capstonedesign.utils.Constants
import com.example.capstonedesign.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CameraResultFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentCameraResultBinding
    lateinit var viewModel: ItemSearchViewModel
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var placeName: String
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
        binding = FragmentCameraResultBinding.inflate(inflater, container, false)




        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val input = requireArguments().getString("capturedPhoto")
        Log.d("사진경로", "input: $input")
        val absolutePath = absolutelyPath(input!!.toUri())
        Log.d("사진절대경로", "absolutePath: $absolutePath")

        viewModel = (activity as MainActivity).viewModel
        viewModel.uploadImgToServer(absolutePath)
        viewModel.Items.observe(viewLifecycleOwner, Observer { response ->
            when(response){
                is Resource.Success -> {
                    hideProgressBar()
                    response.data?.let { response ->
                        Log.d("사진검색", "response: $response")
                        val item = response
                        if(item.response.searchItems.isEmpty()){
                            placeName = "편의점"
                        } else {
                            placeName = item.response.searchItems[0].brand
                        }
                        Log.d("placeName", placeName)
                        addInfo(item)
                        Log.d("placeName", placeName)
                    }
                }
                is Resource.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e("사진검색", "An error occured: $message")
                        Toast.makeText(requireContext(), "서버와 연결 오류. 인터넷상태를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    showProgressBar()
                }
            }

        })

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
    private fun absolutelyPath(contentUri : Uri): String {
        val result : String
        val cursor : Cursor? = requireContext().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            result = contentUri.path ?: ""
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }

    private fun addInfo(result: ItemResponse){
        if (result.response.searchItems.isEmpty()){
            Toast.makeText(requireContext(), "검색 결과가 없습니다. 사진을 다시 촬영해주세요", Toast.LENGTH_SHORT).show()
            view?.findNavController()?.navigate(R.id.action_cameraResultFragment_to_cameraFragment)
            return
        }
        val response = result.response.searchItems[0]
        val name = response.name
        val promotion = response.promotion
        val formatPromotion = when(promotion){
            "ONE_PLUS_ONE" -> "1+1"
            "TWO_PLUS_ONE" -> "2+1"
            else -> "할인"
        }
        val brand = response.brand
        val brandColor = when(brand) {
            "CU" -> ContextCompat.getColor(requireContext(), R.color.cu)
            "GS25" -> ContextCompat.getColor(requireContext(), R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(requireContext(), R.color.seven)
            "EMART24" -> ContextCompat.getColor(requireContext(), R.color.emart24)
            else -> Color.BLACK
        }
        val borderColor = when(brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val imgData = response.imgUrl
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/$imgData"

        binding.apply {
            tvItemPromotion.text = formatPromotion
            tvConvName.text = brand
            tvConvName.setTextColor(brandColor)
            tvConvName.setBackgroundResource(borderColor)
            tvItemName.text = name
            tvItemPerPrice.text = response.pricePerUnit.toString()
            tvItemGroupPrice.text = response.pricePerGroup.toString()
            Glide.with(this@CameraResultFragment).load(fullImgUrl).into(ivItemImage)
        }

        searchPlace(currentLatitude, currentLongitude, 2000, placeName)

    }

    private fun hideProgressBar() {
        binding.progressBar.visibility = View.INVISIBLE
    }
    private fun showProgressBar() {
        binding.progressBar.visibility = View.VISIBLE
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.clear()
        updateLocationUI()
        getDeviceLocation()


    }

    @SuppressWarnings("MissingPermission")
    private fun updateLocationUI() {
        if (mMap == null) {
            return
        }
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
    private fun getDeviceLocation() {
        try {
            if (locationPermissionGranted){
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(requireActivity()){ task ->
                    if(task.isSuccessful) {
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
                            mMap.addMarker(MarkerOptions().position(LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)).title("현재 위치"))

                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.")
                            Log.e("TAG", "Exception: %s", task.exception)
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
            override fun onResponse(call: Call<PlaceInfo>, response: Response<PlaceInfo>) {
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
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

    }

}







