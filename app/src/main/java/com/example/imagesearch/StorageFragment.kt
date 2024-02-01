package com.example.imagesearch

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.databinding.FragmentStorageBinding

class StorageFragment(private val mainActivity: MainActivity) : Fragment() {
    private val binding by lazy { FragmentStorageBinding.inflate(layoutInflater) }
    private val imageAdapter by lazy { ImageAdapter(mainActivity.likedImages, mainActivity, this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvLikeList.layoutManager = GridLayoutManager(context, 2)
        binding.rvLikeList.adapter = imageAdapter

    }
}