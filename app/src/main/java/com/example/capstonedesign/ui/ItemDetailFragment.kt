package com.example.capstonedesign.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.databinding.FragmentItemDetailBinding
import com.example.capstonedesign.data.response.SearchItem


class ItemDetailFragment: Fragment() {

    private lateinit var binding: FragmentItemDetailBinding
    val args: ItemDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentItemDetailBinding.inflate(inflater, container, false)
        val item = args.item
        refactoringInfo(item)
        return binding.root
    }

    private fun refactoringInfo(item: SearchItem) {
        val promotion = when(item.promotion){
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> item.promotion
        }
        val brandColor = when(item.brand){
            "CU" -> ContextCompat.getColor(requireContext(), R.color.cu)
            "GS25" -> ContextCompat.getColor(requireContext(), R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(requireContext(), R.color.seven)
            "EMART24" -> ContextCompat.getColor(requireContext(), R.color.emart24)
            else -> ContextCompat.getColor(requireContext(), R.color.black)
        }
        val borderColor = when(item.brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/${item.imgUrl}"


        binding.tvPromotion.text = promotion
        binding.tvConvName.text = item.brand
        binding.tvConvName.setTextColor(brandColor)
        binding.tvConvName.setBackgroundResource(borderColor)
        binding.tvName.text = item.name
        binding.tvPrice.text = item.pricePerUnit.toString()
        Glide.with(requireContext()).load(fullImgUrl).into(binding.ivItemImage)
    }
}