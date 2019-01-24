package com.example.qthien.week_1

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface GetData {
    @GET("now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    fun getAllVideo() : Call<Video>

    @GET ("{id}/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed")
    fun getAllTrailer(@Path("id") id : Int) : Call<Trailer>
}