package com.example.imagesearch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.imagesearch.data.Document
import com.example.imagesearch.databinding.ItemImageBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ImageAdapter(val imageList: MutableList<Document>, private val mainActivity: MainActivity, private val fragment: Fragment) : RecyclerView.Adapter<ImageAdapter.ImageViewHolder>() {

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

        holder.ivLike.visibility = View.GONE

        // iv_like 초기화
        holder.isLiked = mainActivity.likedImages.contains(currentItem)
        holder.ivLike.setImageResource(if (holder.isLiked) R.drawable.img_like else R.drawable.img_unlike)

        // iv_like 클릭하면, 이미지 바뀌고, MainActivity 가서 좋아요 리스트에 추가
        holder.ivLike.setOnClickListener {
            holder.isLiked = !holder.isLiked
            if (holder.isLiked) {
                mainActivity.likedImages.add(currentItem)
                holder.ivLike.setImageResource(R.drawable.img_like)
            } else {
                mainActivity.likedImages.remove(currentItem)
                holder.ivLike.setImageResource(R.drawable.img_unlike)
            }
            notifyItemChanged(position)
        }

        // 이미지 클릭 이벤트 처리
        if (fragment is StorageFragment) {
            holder.itemView.setOnClickListener {
                mainActivity.likedImages.remove(currentItem)
                notifyItemRemoved(position)
                notifyItemRangeChanged(position, imageList.size)
            }
            // StorageFragment에서는 좋아요 버튼을 숨김
            holder.ivLike.visibility = View.GONE
        } else {
            // SearchFragment에서는 좋아요 버튼을 보임
            holder.ivLike.visibility = View.VISIBLE
        }

        // 현재 Fragment에 따라 좋아요 버튼 설정
        holder.ivLike.visibility = if (fragment is SearchFragment) View.VISIBLE else View.GONE

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