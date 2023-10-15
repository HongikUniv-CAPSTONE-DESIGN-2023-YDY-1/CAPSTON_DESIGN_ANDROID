package com.example.capstonedesign.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.capstonedesign.R
import com.example.capstonedesign.utils.UserPreferences
import com.example.capstonedesign.databinding.ActivityMainBinding
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModel
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModel
import com.example.capstonedesign.data.viewModel.reviews.ReviewViewModelProviderFactory
import com.google.android.material.bottomsheet.BottomSheetDialog


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ItemSearchViewModel
    lateinit var userPreferences: UserPreferences
    lateinit var reviewViewModel: ReviewViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        val itemRepository = Repository()
        val viewModelProviderFactory = ItemSearchViewModelProviderFactory(itemRepository)
        viewModel =
            ViewModelProvider(this, viewModelProviderFactory).get(ItemSearchViewModel::class.java)
        userPreferences = UserPreferences(this)

        val viewModelProviderFactory2 = ReviewViewModelProviderFactory(itemRepository)
        reviewViewModel =
            ViewModelProvider(this, viewModelProviderFactory2).get(ReviewViewModel::class.java)
    }
}