package com.example.capstonedesign.ui.activity

import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.adapter.DetailViewPagerAdapter
import com.example.capstonedesign.data.itemViewModel.ItemSearchViewModel
import com.example.capstonedesign.data.itemViewModel.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ItemResponse
import com.example.capstonedesign.databinding.ActivityCameraSearchResultBinding
import com.example.capstonedesign.utils.Resource
import com.google.android.material.tabs.TabLayoutMediator

class CameraSearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraSearchResultBinding
    private lateinit var viewModel: ItemSearchViewModel
    private lateinit var brand: String
    private lateinit var itemID: String
    private val tableTitle = arrayOf(
        "지도",
        "리뷰"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val imgUri = intent.getStringExtra("imgUri")?.toUri()
        val absolutePath = absolutelyPath(imgUri!!)

        val itemRepository = Repository()
        val ItemSearchViewModelProviderFactory = ItemSearchViewModelProviderFactory(itemRepository)
        viewModel = ViewModelProvider(this, ItemSearchViewModelProviderFactory).get(ItemSearchViewModel::class.java)
        viewModel.uploadImgToServer(absolutePath)
        viewModel.CamerItems.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { response ->
                        Log.d("사진검색", "response: $response")
                        if (response.response.searchItems.isEmpty()){
                            Toast.makeText(this@CameraSearchResultActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            val item = response.response.searchItems[0]
                            addInfo(response)
                            itemID = item.id.toString()
                            brand = item.brand

                            val viewPager = binding.viewPager
                            val tabLayout = binding.tabLayout
                            val adapter = DetailViewPagerAdapter(supportFragmentManager, lifecycle, brand, itemID)
                            viewPager.adapter = adapter
                            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                                tab.text = tableTitle[position]
                            }.attach()
                        }
                    }
                }
                is Resource.Error -> {
                    response.message?.let { message ->
                        Log.e("사진검색", "An error occured: $message")
                        Toast.makeText(this, "서버와 연결 오류. 인터넷상태를 확인해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
                is Resource.Loading -> {
                    Log.d("사진검색", "Loading...")
                }
            }

        })
        binding.ibGoBack.setOnClickListener {
            finish()
        }

    }
    private fun absolutelyPath(contentUri : Uri): String {
        val result : String
        val cursor : Cursor? = this.contentResolver.query(contentUri, null, null, null, null)
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

        val response = result.response.searchItems[0]
        val name = response.name
        val formatPromotion = when(response.promotion){
            "ONE_PLUS_ONE" -> "1+1"
            "TWO_PLUS_ONE" -> "2+1"
            else -> "할인"
        }
        val brand = response.brand
        val brandColor = when(brand) {
            "CU" -> ContextCompat.getColor(this, R.color.cu)
            "GS25" -> ContextCompat.getColor(this, R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(this, R.color.seven)
            "EMART24" -> ContextCompat.getColor(this, R.color.emart24)
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
        val price = response.pricePerUnit

        binding.apply {
            tvItemPromotion.text = formatPromotion
            tvConvName.text = brand
            tvConvName.setTextColor(brandColor)
            tvConvName.setBackgroundResource(borderColor)
            tvItemName.text = name
            tvItemPrice.text = price.toString()+"원"
            Glide.with(this@CameraSearchResultActivity).load(fullImgUrl).into(ivItemImage)
        }
    }
}