package com.example.qthien.week_1

import com.google.gson.annotations.SerializedName

data class Trailer(@SerializedName("id") var id : Int,@SerializedName("results") var listTrailers : ArrayList<TrailerVideos>)