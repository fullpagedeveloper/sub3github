package com.fullpagedeveloper.submission3githubusers.data

import com.fullpagedeveloper.submission3githubusers.data.request.ApiClient
import com.fullpagedeveloper.submission3githubusers.utils.Constant.Companion.AUTHKEY
import com.fullpagedeveloper.submission3githubusers.utils.Constant.Companion.BASE_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ServiceGenerator {

    private val client by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Authorization", AUTHKEY)
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .build()
    }

    private val retrofitBuilder: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiClient: ApiClient by lazy {
        retrofitBuilder.build().create(ApiClient::class.java)
    }
}
