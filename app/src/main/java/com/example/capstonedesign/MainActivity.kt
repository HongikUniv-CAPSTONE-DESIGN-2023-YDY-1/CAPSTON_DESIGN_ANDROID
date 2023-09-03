package com.example.capstonedesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.capstonedesign.data.UserPreferences
import com.example.capstonedesign.databinding.ActivityMainBinding
import com.example.capstonedesign.data.itemViewModel.ItemSearchViewModel
import com.example.capstonedesign.data.itemViewModel.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.data.repository.Repository


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

        val userPreferences = UserPreferences(this)
        userPreferences.accessToken.asLiveData().observe(this, Observer {
           Toast.makeText(this, it ?: "Token is Null", Toast.LENGTH_SHORT).show()
       })
        userPreferences.refreshToken.asLiveData().observe(this, Observer {
           Toast.makeText(this, it ?: "Token is Null", Toast.LENGTH_SHORT).show()
       })
        userPreferences.userEmail.asLiveData().observe(this, Observer {
           Toast.makeText(this, it ?: "Email is Null", Toast.LENGTH_SHORT).show()
       })
        userPreferences.userPassword.asLiveData().observe(this, Observer {
           Toast.makeText(this, it ?: "Password is Null", Toast.LENGTH_SHORT).show()
       })
    }
}