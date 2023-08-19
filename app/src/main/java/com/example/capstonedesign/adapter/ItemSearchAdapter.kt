package com.example.capstonedesign.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.response.ItemResponse
import com.example.capstonedesign.response.SearchItem

class ItemSearchAdapter: RecyclerView.Adapter<ItemSearchAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)


    private val differCallback = object : DiffUtil.ItemCallback<SearchItem>(){
        override fun areItemsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return oldItem.imgUrl == newItem.imgUrl
        }

        override fun areContentsTheSame(oldItem: SearchItem, newItem: SearchItem): Boolean {
            return  oldItem == newItem
        }

    }


    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_search_layout,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((SearchItem) -> Unit)? = null

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = differ.currentList[position]
        val promotion = item.promotion
        val formattedPromtion = when(promotion) {
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> promotion
        }
        val brand = item.brand
        val brandColor = when(brand) {
            "CU" -> Color.parseColor("@Color/cu")
            "GS25" -> Color.parseColor("@Color/gs25")
            "SEVENELEVEN" -> Color.parseColor("@Color/seven")
            "EMART24" -> Color.parseColor("@Color/emart24")
            else -> Color.BLACK
        }
        val borderColor = when(brand) {
            "CU" -> "R.drawable.cu_border_color"
            "GS25" -> "R.drawable.gs25_border_color"
            "SEVENELEVEN" -> "R.drawable.seven_border_color"
            "EMART24" -> "R.drawable.emart24_border_color"
            else -> "R.drawable.black_border"
        }
        val imgData = item.imgUrl
        val fullImgUrl = "http://nas.robinjoon.xyz:8080/image/$imgData"

        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_item_promotion).text = formattedPromtion
            findViewById<TextView>(R.id.tv_conv_name).text = item.brand
            findViewById<TextView>(R.id.tv_conv_name).setTextColor(brandColor)
            findViewById<TextView>(R.id.tv_conv_name).setBackgroundResource(borderColor.toInt())
            findViewById<TextView>(R.id.tv_item_name).text = item.name
            findViewById<TextView>(R.id.tv_price).text = item.pricePerUnit.toString()
            Glide.with(this).load(fullImgUrl).into(findViewById(R.id.iv_item_image))
            setOnClickListener {
                onItemClickListener?.let { it(item)}
            }
        }

    }


    fun setOnItemClickListener(listener: (SearchItem) -> Unit) {
        onItemClickListener = listener
    }
}


