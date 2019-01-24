package com.example.qthien.week_1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClientInstance {
    private var retrofit : Retrofit? = null
    private val baseUrl : String = "https://api.themoviedb.org/3/movie/"

    val getRetrofit : GetData
        get(){
            if(retrofit == null){
                retrofit = retrofit2.Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            }
            return retrofit!!.create(GetData::class.java)
        }

}