package com.example.capstonedesign.data.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.capstonedesign.R
import com.example.capstonedesign.data.response.RecommendData


class RecommendAdapter() : RecyclerView.Adapter<RecommendAdapter.RecommendViewHolder>() {
    inner class RecommendViewHolder(recommendView: View) : RecyclerView.ViewHolder(recommendView)

    private val differCallback = object : DiffUtil.ItemCallback<RecommendData>() {
        override fun areItemsTheSame(oldItem: RecommendData, newItem: RecommendData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RecommendData, newItem: RecommendData): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendAdapter.RecommendViewHolder {
        return RecommendViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.recommend_layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendAdapter.RecommendViewHolder, position: Int) {
        val recommend = differ.currentList[position]
        val brand = recommend.brand
        val brandColor = when (brand) {
            "CU" -> ContextCompat.getColor(holder.itemView.context, R.color.cu)
            "GS25" -> ContextCompat.getColor(holder.itemView.context, R.color.gs25)
            "SEVENELEVEN" -> ContextCompat.getColor(holder.itemView.context, R.color.seven)
            "EMART24" -> ContextCompat.getColor(holder.itemView.context, R.color.emart24)
            else -> Color.BLACK
        }
        val borderColor = when (brand) {
            "CU" -> R.drawable.cu_border_color
            "GS25" -> R.drawable.gs25_border_color
            "SEVENELEVEN" -> R.drawable.seven_border_color
            "EMART24" -> R.drawable.emart24_border_color
            else -> R.drawable.black_border
        }
        val name = recommend.name
        val pricePerUnit = recommend.pricePerUnit
        val pricePerGroup = recommend.pricePerGroup
        val promotion = when (recommend.promotion) {
            "ONE_PLUS_ONE" -> "1 + 1"
            "TWO_PLUS_ONE" -> "2 + 1"
            else -> recommend.promotion
        }
        val fullImgUrl = "http://192.168.37.200:8081/image/${recommend.imgUrl}"

        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_promotion).text = promotion
            findViewById<TextView>(R.id.tv_brand_name).text = brand
            findViewById<TextView>(R.id.tv_brand_name).setTextColor(brandColor)
            findViewById<TextView>(R.id.tv_brand_name).setBackgroundResource(borderColor)
            findViewById<TextView>(R.id.tv_item_name).text = name
            Glide.with(this).load(fullImgUrl).into(findViewById(R.id.iv_item_image))
            setOnClickListener {
                onItemClickListener?.let { it(recommend) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((RecommendData) -> Unit)? = null

    fun setOnItemClickListener(listener: (RecommendData) -> Unit) {
        onItemClickListener = listener
    }
}


