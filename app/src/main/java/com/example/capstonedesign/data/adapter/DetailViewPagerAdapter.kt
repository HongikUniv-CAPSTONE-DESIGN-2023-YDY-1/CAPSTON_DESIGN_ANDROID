package com.example.capstonedesign.data.adapter

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.capstonedesign.ui.fragment.MapsFragment
import com.example.capstonedesign.ui.fragment.ReviewFragment


class DetailViewPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val brand: String,
    private val itemId: String
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {

        return 2
    }

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> {

                MapsFragment().apply {
                    arguments = Bundle().apply {
                        putString("brand", brand)
                    }
                }
            }

            1 -> {
                ReviewFragment().apply {
                    arguments = Bundle().apply {
                        putString("itemId", itemId)
                    }
                }
            }

            else -> throw IllegalArgumentException("Invalid position")
        }
    }
}
