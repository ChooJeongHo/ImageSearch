package com.example.imagesearch

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagesearch.data.Document
import com.example.imagesearch.databinding.FragmentSearchBinding
import com.example.imagesearch.retrofit.NetWorkClient
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch

class SearchFragment() : Fragment() {
    private val binding by lazy { FragmentSearchBinding.inflate(layoutInflater) }
    private val imageAdapter by lazy { ImageAdapter(mutableListOf(), mainActivity, this) }
    private val mainActivity: MainActivity by lazy { activity as MainActivity }
    private val gson = Gson()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return binding.root // inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val pref = requireActivity().getSharedPreferences("pref", 0)
        val lastWord = pref.getString("lastWord", "")
        binding.etSearch.setText(lastWord)
        loadSearchResults()

        binding.rvImageList.layoutManager = GridLayoutManager(context, 2)
        binding.rvImageList.adapter = imageAdapter

        // 검색 버튼 클릭 리스너 설정
        binding.btnSearch.setOnClickListener {
            val word = binding.etSearch.text.toString()
            val edit = pref.edit()
            edit.putString("lastWord", word)
            edit.apply()
            communicateNetWork(word)

            // 키보드 내리기
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etSearch.windowToken, 0)
        }


    }

    // 검색버튼을 누르면 네트워크 통신을 하여 이미지를 가져온다.
    private fun communicateNetWork(query: String) = lifecycleScope.launch(){
        val response = NetWorkClient.imageNetWork.getImage(query, "accuracy", 1, 80)
        Log.d("aa", "response: $response")

        imageAdapter.imageList.clear()
        imageAdapter.imageList.addAll(response.documents)
        imageAdapter.notifyItemRangeChanged(0, imageAdapter.imageList.size)
        saveSearchResults()

    }

    // 검색 결과를 저장
    private fun saveSearchResults() {
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        val json = gson.toJson(imageAdapter.imageList)
        editor.putString("results", json)
        editor.apply()
    }

    // 앱을 껐다 켜도 검색 결과를 호출
    private fun loadSearchResults() {
        val prefs = requireActivity().getSharedPreferences("prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("results", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Document>>() {}.type
            val newList = gson.fromJson<MutableList<Document>>(json, type)
            imageAdapter.imageList.clear()
            imageAdapter.imageList.addAll(newList)
        }
    }

}
