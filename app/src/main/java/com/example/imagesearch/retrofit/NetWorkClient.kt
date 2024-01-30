package com.example.imagesearch.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetWorkClient {

    private const val IMAGE_BASE_URL = "https://dapi.kakao.com/"
    private const val AUTH_HEADER = "KakaoAK 41f6e1715030fe96cedc2fcaffe483c9"

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val authInterceptor = Interceptor { chain ->
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", AUTH_HEADER)
                .build()
            chain.proceed(newRequest)
        }

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .addInterceptor(authInterceptor)
            .build()
    }

    private val ImageRetrofit by lazy {
        Retrofit.Builder()
            .baseUrl(IMAGE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(createOkHttpClient())
            .build()
    }

    val imageNetWork: NetWorkInterface by lazy {
        ImageRetrofit.create(NetWorkInterface::class.java)
    }
}