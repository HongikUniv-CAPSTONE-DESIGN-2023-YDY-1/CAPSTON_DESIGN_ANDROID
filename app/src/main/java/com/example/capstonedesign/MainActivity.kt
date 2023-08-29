package com.example.capstonedesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.capstonedesign.databinding.ActivityMainBinding
import com.example.capstonedesign.itemViewModel.ItemSearchViewModel
import com.example.capstonedesign.itemViewModel.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.repository.Repository


class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: ItemSearchViewModel



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
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ItemSearchViewModel::class.java)

        val sharedPreferences = getSharedPreferences("user_Token", MODE_PRIVATE)
        val accessToken = sharedPreferences.getString("accessToken", null)
        val refreshToken = sharedPreferences.getString("refreshToken", null)
        if (accessToken != null && refreshToken != null) {
            Log.d("엑세스토큰", "onCreate: $accessToken")
            Log.d("리프레시토큰", "onCreate: $refreshToken")
        } else {
            Log.d("토큰", "onCreate: 토큰없음")
        }


    }
}