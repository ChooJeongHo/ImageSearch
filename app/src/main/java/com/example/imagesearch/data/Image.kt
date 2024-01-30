package com.example.imagesearch.data

import com.google.gson.annotations.SerializedName
import java.util.Date

data class Image(
//    val response: ImageResponse
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("documents")
    val documents: List<Document>
)

//data class ImageResponse(
//    @SerializedName("meta")
//    val meta: Meta,
//    @SerializedName("documents")
//    val documents: List<Document>
//)

// 출력 결과
data class Meta(
    @SerializedName("total_count")
    val totalCount: Int?,
    @SerializedName("pageable_count")
    val pageableCount: Int?,
    @SerializedName("is_end")
    val isEnd: Boolean?
)

// 출력 결과
data class Document(
    val collection: String,
    @SerializedName("thumbnail_url")
    val thumbnailUrl: String,
    @SerializedName("image_url")
    val imageUrl: String,
    val width: Int,
    val height: Int,
    @SerializedName("display_sitename")
    val siteName: String,
    @SerializedName("doc_url")
    val docUrl: String,
    val datetime: Date
)
