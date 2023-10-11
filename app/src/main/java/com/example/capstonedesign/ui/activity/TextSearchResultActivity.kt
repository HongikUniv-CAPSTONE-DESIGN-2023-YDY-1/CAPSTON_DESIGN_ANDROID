package com.example.capstonedesign.ui.activity

import android.content.Intent
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.adapter.DetailViewPagerAdapter
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ReviewResponse
import com.example.capstonedesign.data.response.SearchItem
import com.example.capstonedesign.data.response.User
import com.example.capstonedesign.data.reviewViewModel.ReviewViewModel
import com.example.capstonedesign.data.reviewViewModel.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.ActivityTextSearchResultBinding
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.UserPreferences
import com.google.android.material.tabs.TabLayoutMediator

class TextSearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityTextSearchResultBinding
    private lateinit var viewModel: ReviewViewModel
    private lateinit var userPreferences: UserPreferences

    private val tableLayout = arrayOf(
        "지도",
        "리뷰"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTextSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val itemInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("itemInfo", SearchItem::class.java)
        } else {
            intent.getParcelableExtra<SearchItem>("itemInfo")
        }
        refactoringInfo(itemInfo!!)

        userPreferences = UserPreferences(this)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = DetailViewPagerAdapter(supportFragmentManager, lifecycle, itemInfo.brand, itemInfo.id.toString())
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tableLayout[position]
        }.attach()

        binding.ibGoBack.setOnClickListener {
            finish()
        }





    }
    private fun refactoringInfo(item: SearchItem) {
        val promotion = when(item.promotion){
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> item.promotion
        }
        val brandColor = when(item.brand){
            "CU" -> ContextCompat.getColor(this, R.color.cu)
            "GS25" -> ContextCompat.getColor(this, R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(this, R.color.seven)
            "EMART24" -> ContextCompat.getColor(this, R.color.emart24)
            else -> ContextCompat.getColor(this, R.color.black)
        }
        val borderColor = when(item.brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/${item.imgUrl}"
        val price = item.pricePerUnit

        binding.tvItemPromotion.text = promotion
        binding.tvConvName.text = item.brand
        binding.tvConvName.setTextColor(brandColor)
        binding.tvConvName.setBackgroundResource(borderColor)
        binding.tvItemName.text = item.name
        binding.tvItemPrice.text = price.toString()+"원"
        Glide.with(this).load(fullImgUrl).into(binding.ivItemImage)
    }

}