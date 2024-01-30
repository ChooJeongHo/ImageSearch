package com.example.imagesearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearch.data.Document
import com.example.imagesearch.databinding.ItemImageBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageAdapter(val imageList: MutableList<Document>, private val likedImages: MutableList<Document>) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageAdapter.ImageViewHolder {
        val binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageAdapter.ImageViewHolder, position: Int) {
        val currentItem = imageList[position]
        Glide.with(holder.itemView.context)
            .load(currentItem.thumbnailUrl)
            .into(holder.ivImage)
        holder.tvSource.text = currentItem.siteName

        // 날짜 형식 변경
//        이 방식은 날짜를 String으로 받아온 경우애 씀
//        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX", Locale.getDefault())
//        val date = inputFormat.parse(currentItem.datetime)
        val date = currentItem.datetime
        val outputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val formattedDate = outputFormat.format(date)

        holder.tvDate.text = formattedDate

        // iv_like 클릭하면, 이미지 바뀌고, 보관함에 저장
        holder.ivLike.setOnClickListener {
            holder.isLiked = !holder.isLiked
            if (holder.isLiked) {
                likedImages.add(currentItem)
                R.drawable.img_like
            } else {
                likedImages.remove(currentItem)
                R.drawable.img_unlike
            }
            notifyDataSetChanged()
        }

        // 좋아요 상태에 따라 iv_like의 이미지 설정
        holder.isLiked = likedImages.contains(currentItem)
        val imageResource = if (holder.isLiked) R.drawable.img_like else R.drawable.img_unlike
        holder.ivLike.setImageResource(imageResource)
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    inner class ImageViewHolder(binding: ItemImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage = binding.ivImage
        val ivLike = binding.ivLike
        val tvSource = binding.tvSource
        val tvDate = binding.tvDate
        var isLiked = false
    }

}