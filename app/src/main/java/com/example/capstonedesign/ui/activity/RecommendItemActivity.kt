package com.example.capstonedesign.ui.activity

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.adapter.DetailViewPagerAdapter
import com.example.capstonedesign.data.response.RecommendData
import com.example.capstonedesign.databinding.ActivityRecommendItemBinding
import com.example.capstonedesign.utils.UserPreferences
import com.google.android.material.tabs.TabLayoutMediator

class RecommendItemActivity : AppCompatActivity() {
    lateinit var binding: ActivityRecommendItemBinding

    private lateinit var userPreferences: UserPreferences

    private val tableLayout = arrayOf(
        "지도",
        "리뷰"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecommendItemBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val itemInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("itemInfo", RecommendData::class.java)
        } else {
            intent.getParcelableExtra<RecommendData>("itemInfo")
        }
        refactoringInfo(itemInfo!!)

        userPreferences = UserPreferences(this)
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = DetailViewPagerAdapter(
            supportFragmentManager,
            lifecycle,
            itemInfo.brand,
            itemInfo.id.toString()
        )
        viewPager.adapter = adapter
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tableLayout[position]
        }.attach()

        binding.ibGoBack.setOnClickListener {
            finish()
        }


    }

    private fun refactoringInfo(item: RecommendData) {
        val promotion = when (item.promotion) {
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> item.promotion
        }
        val brandColor = when (item.brand) {
            "CU" -> ContextCompat.getColor(this, R.color.cu)
            "GS25" -> ContextCompat.getColor(this, R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(this, R.color.seven)
            "EMART24" -> ContextCompat.getColor(this, R.color.emart24)
            else -> ContextCompat.getColor(this, R.color.black)
        }
        val borderColor = when (item.brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val fullImgUrl = "http://192.168.37.200:8081/image/${item.imgUrl}"
        val price = item.pricePerUnit

        binding.tvItemPromotion.text = promotion
        binding.tvConvName.text = item.brand
        binding.tvConvName.setTextColor(brandColor)
        binding.tvConvName.setBackgroundResource(borderColor)
        binding.tvItemName.text = item.name
        binding.tvItemPrice.text = price.toString() + "원"
        Glide.with(this).load(fullImgUrl).into(binding.ivItemImage)
    }

}