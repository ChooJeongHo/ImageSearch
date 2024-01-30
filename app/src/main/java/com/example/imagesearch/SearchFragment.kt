package com.example.imagesearch

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.data.Document
import com.example.imagesearch.databinding.FragmentSearchBinding
import com.example.imagesearch.retrofit.NetWorkClient.imageNetWork
import kotlinx.coroutines.launch

class SearchFragment(private val likedImages: MutableList<Document>) : Fragment() {

    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val imageAdapter by lazy { ImageAdapter(mutableListOf(), likedImages) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return binding.root // inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("pref", 0)
        val lastWord = pref.getString("lastWord", "")
        binding.etSearch.setText(lastWord)

        binding.rvImageList.layoutManager = GridLayoutManager(context, 2)
        binding.rvImageList.adapter = imageAdapter

        // 검색 버튼 클릭 리스너 설정
        binding.btnSearch.setOnClickListener {
            val word = binding.etSearch.text.toString()
            val edit = pref.edit()
            edit.putString("lastWord", word)
            edit.apply()
            communicateNetWork(binding.etSearch.text.toString())
        }


    }

    // 검색버튼을 누르면 네트워크 통신을 하여 이미지를 가져온다.
    private fun communicateNetWork(query: String) = lifecycleScope.launch(){
        val response = imageNetWork.getImage(query, "accuracy", 1, 80)
        Log.d("aa", "response: $response")

        imageAdapter.imageList.clear()
        imageAdapter.imageList.addAll(response.documents)
        imageAdapter.notifyDataSetChanged()
    }

}
