package com.example.capstonedesign.ui



import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.libraries.places.api.model.LocationRestriction

import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.example.capstonedesign.MainActivity
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentCameraResultBinding
import com.example.capstonedesign.model.ItemSearchViewModel
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.utils.Resource
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient


class CameraResultFragment: Fragment(), OnMapReadyCallback {

    private lateinit var binding: FragmentCameraResultBinding
    lateinit var viewModel: ItemSearchViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private lateinit var mMap: GoogleMap
    private lateinit var placeName: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCameraResultBinding.inflate(inflater, container, false)


        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as com.google.android.gms.maps.SupportMapFragment?
        mapFragment?.getMapAsync(this)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        Places.initialize(requireContext(), getString(R.string.google_maps_key))
        placesClient = Places.createClient(requireContext())
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
        fetchCurrentLocation()
    }

    private fun fetchCurrentLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)==PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true

            fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    val currentLatLng = com.google.android.gms.maps.model.LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(currentLatLng, 17f))
                    mMap.addMarker(MarkerOptions().position(currentLatLng).title("현재 위치"))

                    searchPlaces()
                }
            }
        } else {
            requestPermissions(arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }
    }
    private fun searchPlaces() {
        try {
            val currentLatLng = mMap.cameraPosition.target


            val request = FindCurrentPlaceRequest.builder(listOf(Place.Field.NAME, Place.Field.LAT_LNG)).build()

            placesClient.findCurrentPlace(request).addOnSuccessListener { response ->
                mMap.clear()
                val distanceThreshold = 0.01
                val distanceThresholdSquared = distanceThreshold * distanceThreshold
                var markerAdded = false
                for (placeLikelihood in response.placeLikelihoods) {
                    val place =placeLikelihood.place
                    val placeLatLng = place.latLng
                    if (placeLatLng != null && calculateSquaredDistance(currentLatLng, placeLatLng) < distanceThresholdSquared && place.name?.startsWith(placeName) == true) {
                        mMap.addMarker(MarkerOptions().position(placeLatLng).title(place.name))
                        markerAdded = true
                    }
                }
                if (!markerAdded) {

                    Toast.makeText(requireContext(), "주변에 ${placeName}이(가) 없습니다.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(requireContext(), "오류 발생: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: SecurityException) {
            Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun calculateSquaredDistance(latLng1: LatLng, latLng2: LatLng): Double {
        val latDiff = latLng1.latitude - latLng2.latitude
        val lngDiff = latLng1.longitude - latLng2.longitude
        return (latDiff * latDiff) + (lngDiff * lngDiff)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(requireContext(), "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    companion object {
        private const val REQUEST_LOCATION_PERMISSION = 1
    }


}







