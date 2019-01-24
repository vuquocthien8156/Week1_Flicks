package com.example.qthien.week_1

import com.google.gson.annotations.SerializedName

data class Video(
    @SerializedName("results")
    var results : ArrayList<Result> = ArrayList(),
    @SerializedName("page")
    var page : Int,
    @SerializedName("total_results")
    var totalResults : Int,
    @SerializedName("dates")
    var dates : Dates,
    @SerializedName("total_pages")
    var totalPages : Int
)