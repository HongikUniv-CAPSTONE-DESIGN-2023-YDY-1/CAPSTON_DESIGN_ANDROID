package com.example.capstonedesign.ui.activity

import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.adapter.DetailViewPagerAdapter
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModel
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.SearchItem
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModel
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModelProviderFactory
import com.example.capstonedesign.databinding.ActivityCameraSearchResultBinding
import com.example.capstonedesign.utils.UserPreferences
import com.google.android.material.tabs.TabLayoutMediator

class CameraSearchResultActivity : AppCompatActivity() {
    lateinit var binding: ActivityCameraSearchResultBinding
    private lateinit var viewModel: ItemSearchViewModel
    private lateinit var reviewViewModel: ReviewViewModel
    private lateinit var brand: String
    private lateinit var itemID: String
    private val tableTitle = arrayOf(
        "지도",
        "리뷰"
    )
    private lateinit var userPreferences: UserPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraSearchResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val itemRepository = Repository()
        val ItemSearchViewModelProviderFactory = ItemSearchViewModelProviderFactory(itemRepository)
        viewModel = ViewModelProvider(
            this,
            ItemSearchViewModelProviderFactory
        ).get(ItemSearchViewModel::class.java)

        val reviewRepository = Repository()
        val reviewViewModelProviderFactory = ReviewViewModelProviderFactory(reviewRepository)
        reviewViewModel =
            ViewModelProvider(this, reviewViewModelProviderFactory).get(ReviewViewModel::class.java)

        userPreferences = UserPreferences(this)

        val itemInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("itemInfo", SearchItem::class.java)
        } else {
            intent.getParcelableExtra<SearchItem>("itemInfo")
        }

        userPreferences.accessToken.asLiveData().observe(this) {
            val accessToken = it ?: ""
            if (accessToken.isEmpty()) {
                return@observe
            } else {
                val accessTokenHeader = "Bearer ${accessToken}"
                reviewViewModel.getReviewByItemId(itemInfo!!.id, 1, accessTokenHeader)
            }
        }

        addInfo(itemInfo!!)
        itemID = itemInfo.id.toString()
        brand = itemInfo.brand

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = DetailViewPagerAdapter(supportFragmentManager, lifecycle, brand, itemID)
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tableTitle[position]
        }.attach()

        binding.ibGoBack.setOnClickListener {
            finish()
        }

    }


    private fun addInfo(result: SearchItem) {


        val name = result.name
        val formatPromotion = when (result.promotion) {
            "ONE_PLUS_ONE" -> "1+1"
            "TWO_PLUS_ONE" -> "2+1"
            else -> "할인"
        }
        val brand = result.brand
        val brandColor = when (brand) {
            "CU" -> ContextCompat.getColor(this, R.color.cu)
            "GS25" -> ContextCompat.getColor(this, R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(this, R.color.seven)
            "EMART24" -> ContextCompat.getColor(this, R.color.emart24)
            else -> Color.BLACK
        }
        val borderColor = when (brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val imgData = result.imgUrl
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/$imgData"
        val price = result.pricePerUnit

        binding.apply {
            tvItemPromotion.text = formatPromotion
            tvConvName.text = brand
            tvConvName.setTextColor(brandColor)
            tvConvName.setBackgroundResource(borderColor)
            tvItemName.text = name
            tvItemPrice.text = price.toString() + "원"
            Glide.with(this@CameraSearchResultActivity).load(fullImgUrl).into(ivItemImage)
        }
    }
}