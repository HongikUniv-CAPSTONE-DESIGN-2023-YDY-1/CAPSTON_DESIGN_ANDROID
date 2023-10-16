package com.example.capstonedesign.ui.activity

import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ListAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.adapter.ItemSearchAdapter
import com.example.capstonedesign.data.adapter.RecommendAdapter
import com.example.capstonedesign.data.repository.Repository
import com.example.capstonedesign.data.response.ItemResponse
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModel
import com.example.capstonedesign.data.viewModel.item.ItemSearchViewModelProviderFactory
import com.example.capstonedesign.databinding.ActivityCameraSearchListBinding
import com.example.capstonedesign.databinding.ActivityCameraSearchResultBinding
import com.example.capstonedesign.utils.Resource

class CameraSearchListActivity: AppCompatActivity(){
    lateinit var binding: ActivityCameraSearchListBinding
    private lateinit var viewModel: ItemSearchViewModel
    lateinit var listAdapter: ItemSearchAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraSearchListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        //전달받은 사진 절대경로로 바꾸기
        val imgUri = intent.getStringExtra("imgUri")?.toUri()
        val absolutePath = absolutelyPath(imgUri!!)

        val itemRepository = Repository()
        val ItemSearchViewModelProviderFactory = ItemSearchViewModelProviderFactory(itemRepository)
        viewModel = ViewModelProvider(this, ItemSearchViewModelProviderFactory).get(ItemSearchViewModel::class.java)

        viewModel.uploadImgToServer(absolutePath)
        viewModel.CamerItems.observe(this, Observer { response ->
            when(response){
                is Resource.Success -> {
                    response.data?.let { response ->
                        Log.d("사진검색", "response: $response")
                        if (response.response.searchItems.isEmpty()){
                            Toast.makeText(this@CameraSearchListActivity, "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                            finish()
                        }else {
                            val itemList = response.response.searchItems
                            Log.d("사진검색", "itemList: $itemList")
                            listAdapter.differ.submitList(itemList)
                        }
                    }
                }
            }
        }
        )
        binding.ibGoBack.setOnClickListener {
            finish()
        }
        listAdapter.setOnItemLongClickListener {
            val intent = Intent(this, CameraSearchResultActivity::class.java)
            intent.putExtra("itemInfo" ,it)
            startActivity(intent)
        }


    }

    private fun absolutelyPath(contentUri : Uri): String {
        val result : String
        val cursor : Cursor? = this.contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            result = contentUri.path ?: ""
        } else {
            cursor.moveToFirst()
            val idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            result = cursor.getString(idx)
            cursor.close()
        }
        return result
    }
    private fun setupRecyclerView() {
        listAdapter = ItemSearchAdapter()
        binding.rvCameraSearchList.adapter = listAdapter
        binding.rvCameraSearchList.setHasFixedSize(true)
        binding.rvCameraSearchList.layoutManager = LinearLayoutManager(this)
    }

}