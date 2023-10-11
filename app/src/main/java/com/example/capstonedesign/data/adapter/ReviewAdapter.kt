package com.example.capstonedesign.data.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.capstonedesign.R
import com.example.capstonedesign.data.response.Content
import com.example.capstonedesign.data.response.SearchItem

class ReviewAdapter: RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {
    inner class ReviewViewHolder(reviewView: View): RecyclerView.ViewHolder(reviewView)

    private val differCallback = object : DiffUtil.ItemCallback<Content>(){
        override fun areItemsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem.commentId == newItem.commentId
        }

        override fun areContentsTheSame(oldItem: Content, newItem: Content): Boolean {
            return oldItem == newItem
        }
    }


    val differ = AsyncListDiffer(this, differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
       return ReviewViewHolder(
           LayoutInflater.from(parent.context).inflate(
               R.layout.review_layout,
               parent,
               false
           )
       )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = differ.currentList[position]
        val content =  review.content
        val rating = review.star
        val contentId = review.commentId
        val promotionId = review.promotionId

        holder.itemView.apply {
            findViewById<TextView>(R.id.tv_content_id).text = "리뷰${contentId}"
            findViewById<TextView>(R.id.tv_content).text = content
            findViewById<RatingBar>(R.id.ratingBar).rating = rating.toFloat()
            findViewById<TextView>(R.id.tv_promotion_id).text = "${promotionId}"
            setOnClickListener {
                onItemClickListener?.let { it(review) }
            }
        }
    }
    private var onItemClickListener: ((Content) -> Unit)? = null
    fun setOnItemClickListener(listener: (Content) -> Unit) {
        onItemClickListener = listener
    }
}