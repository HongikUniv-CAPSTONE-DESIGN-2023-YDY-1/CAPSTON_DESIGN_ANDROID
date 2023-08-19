package com.example.capstonedesign

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.capstonedesign.databinding.ActivityMainBinding
import com.example.capstonedesign.model.ItemSearchViewModel
import com.example.capstonedesign.model.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.model.Repository


class MainActivity : AppCompatActivity() {


    private lateinit var Binding: ActivityMainBinding
    lateinit var viewModel: ItemSearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(Binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        NavigationUI.setupWithNavController(Binding.bottomNavigationView, navController)

        val itemRepository = Repository()
        val viewModelProviderFactory = ItemSearchViewModelProviderFactory(itemRepository)
        viewModel = ViewModelProvider(this, viewModelProviderFactory).get(ItemSearchViewModel::class.java)
    }
}