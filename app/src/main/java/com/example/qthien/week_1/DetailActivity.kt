package com.example.qthien.week_1

import android.databinding.DataBindingUtil
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.qthien.week_1.databinding.ActivityDetailBinding
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import kotlinx.android.synthetic.main.activity_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.google.android.youtube.player.internal.y
import com.google.android.youtube.player.internal.x
import android.view.Gravity
import android.R.attr.gravity
import android.view.WindowManager
import android.util.DisplayMetrics



class DetailActivity : YouTubeBaseActivity() {

    val API_KEY = "AIzaSyBOW102hvKU6PGAIxcs9g3D4mNneJbORvc"

    var bind : ActivityDetailBinding? = null
    lateinit var rDetail : Result

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bind = DataBindingUtil.setContentView(this , R.layout.activity_detail)
        //createPopupWindow()

        rDetail = intent.getParcelableExtra<Result>("Result")

        if(rDetail == null){
            finish()
        }

        bind?.result = rDetail

        var data = RetrofitClientInstance.getRetrofit
        var call = data.getAllTrailer(rDetail.id)
        call.enqueue(object : Callback<Trailer>{
            override fun onFailure(call: Call<Trailer>, t: Throwable) {
                Toast.makeText(this@DetailActivity , "Fail read json" , Toast.LENGTH_LONG)
            }

            override fun onResponse(call: Call<Trailer>, response: Response<Trailer>) {
                var trailer : Trailer? = response.body()
                if(trailer != null)
                    LoadVideo(trailer.listTrailers.get(0).key)
            }

        } )


    }

    private fun createPopupWindow() {
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)

        val width = displayMetrics.widthPixels
        val heigth = displayMetrics.heightPixels

        window.setLayout((width * .9).toInt(), (heigth * .8).toInt())

        val par = window.attributes
        par.gravity = Gravity.BOTTOM
        par.x = 0
        par.y = 60

        window.attributes = par
    }

    fun LoadVideo(key : String){
        ytb_play.initialize(API_KEY , object : YouTubePlayer.OnInitializedListener{
            override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, p1: YouTubePlayer?, p2: Boolean) {
                if(rDetail.voteAverage.compareTo(6.5f) > 0)
                {
                    p1?.loadVideo(key)
                }
                else
                    p1?.cueVideo(key)
            }

            override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {

            }

        })
    }
}
