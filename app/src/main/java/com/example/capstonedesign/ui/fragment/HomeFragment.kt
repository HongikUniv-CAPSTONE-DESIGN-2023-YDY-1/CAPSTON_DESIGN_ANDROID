package com.example.capstonedesign.ui.fragment



import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.example.capstonedesign.data.adapter.RecommendAdapter
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.databinding.FragmentHomeBinding
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModel
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.ui.activity.RecommendItemActivity
import com.example.capstonedesign.utils.Resource
import com.example.capstonedesign.utils.UserPreferences

class HomeFragment: Fragment(){

    private lateinit var binding: FragmentHomeBinding
    private lateinit var userPreferences: UserPreferences
    lateinit var viewModel: ItemSearchViewModel
    private lateinit var recommendAdapter: RecommendAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        val itemSearchRepository = Repository()
        val itemSearchViewModelProviderFactory = ItemSearchViewModelProviderFactory(itemSearchRepository)
        viewModel = ViewModelProvider(this, itemSearchViewModelProviderFactory).get(ItemSearchViewModel::class.java)
        userPreferences = UserPreferences(requireContext())


        userPreferences.accessToken.asLiveData().observe(viewLifecycleOwner) {
            val accessToken = it ?: ""
            Log.d("accessToken", accessToken)
            if (accessToken == "") {
                binding.recyclerView.visibility = View.GONE
                binding.tvNotLogin.visibility = View.VISIBLE
                Log.d("추천목록", "토큰 없음")
            } else {
                binding.recyclerView.visibility = View.VISIBLE
                binding.tvNotLogin.visibility = View.GONE

                val accessTokenHeader = "Bearer ${accessToken}"
                viewModel.getRecommendList(accessTokenHeader)
                viewModel.RecommendItems.observe(viewLifecycleOwner, Observer { response ->
                    when (response) {
                        is Resource.Success -> {
                            response.data.let { response ->
                                Log.d("추천목록", "성공")
                                Log.d("추천목록", response.toString())
                                recommendAdapter.differ.submitList(response!!.data)
                            }
                        }

                        is Resource.Error -> {
                            response.data?.message.let {
                                Log.d("추천목록", "실패")
                                Log.d("추천목록", response.data.toString())
                                Log.d("추천목록", response.message.toString())
                            }

                        }

                        is Resource.Loading -> {
                            Log.d("추천목록 로딩", "로딩")
                        }
                    }
                })
            }
        }
        recommendAdapter.setOnItemClickListener {
            val intent = Intent(activity, RecommendItemActivity::class.java)
            intent.putExtra("itemInfo", it)
            startActivity(intent)
        }
    }
    private fun setupRecyclerView() {
        recommendAdapter = RecommendAdapter()
        binding.recyclerView.adapter = recommendAdapter
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.layoutManager = GridLayoutManager(activity, 2)
    }

}