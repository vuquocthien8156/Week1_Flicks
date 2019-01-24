package com.example.qthien.week_1

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.R.attr.orientation
import android.content.pm.ActivityInfo
import android.graphics.drawable.Drawable
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem

import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    var video : Video? = null
    var videoSearch : ArrayList<Result> = ArrayList()
    var orientationLand : Boolean = false
    var adapter : Adapter_Recy? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle("The movie")
        setContentView(R.layout.activity_main)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setDisplayUseLogoEnabled(true)
        supportActionBar?.setLogo(R.drawable.ic_logo_moive)

//       recyclerView.getRecycledViewPool().setMaxRecycledViews(0, 0);

        // Doc dữ liệu
        ReadData()

        var divi = DividerItemDecoration(this@MainActivity , DividerItemDecoration.VERTICAL)
        divi.setDrawable(ContextCompat.getDrawable(applicationContext , R.drawable.divi_recy)!!)

        recyclerView.addItemDecoration(divi)

        // Khởi tạo swipeRefreshLayout and run
        swipeRefreshLayout.setOnRefreshListener{
                ReadData()
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu , menu)
        var menuItem : MenuItem = menu!!.findItem(R.id.menuSearch)
        var searchView : SearchView = menuItem.actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                var listResult : ArrayList<Result> = ArrayList()
                if(p0 != null && p0.length > 0){
                    listResult.clear()
                    listResult.addAll(videoSearch.filter{ it.title.toLowerCase().contains(p0.toLowerCase()) })
                    if(listResult.size > 0){
                        video?.results?.clear()
                        video?.results?.addAll(listResult)
                        adapter?.notifyDataSetChanged()
                    }
                    else{
                        video?.results?.clear()
                        adapter?.notifyDataSetChanged()
                        Toast.makeText(applicationContext , "Không tìm thấy" , Toast.LENGTH_LONG ).show()
                    }
                }
                else{
                    video?.results?.clear()
                    video?.results?.addAll(videoSearch)
                    adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun ReadData(){
        swipeRefreshLayout.setRefreshing(true)

        val re = RetrofitClientInstance.getRetrofit
        val call = re.getAllVideo()
        call.enqueue(object : Callback<Video> {
            override fun onFailure(call: Call<Video>, t: Throwable) {
                Toast.makeText(applicationContext , "Error"  , Toast.LENGTH_LONG).show()
                Log.e("Error" , t.toString())
                swipeRefreshLayout.setRefreshing(false)
            }

            override fun onResponse(call: Call<Video>, response: Response<Video>) {

                video = response.body()!!
                videoSearch.addAll(video!!.results)
                val orientation = resources.configuration.orientation
                orientationLand = Configuration.ORIENTATION_LANDSCAPE == orientation
                adapter = Adapter_Recy(this@MainActivity , video?.results  , orientationLand)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter

                swipeRefreshLayout.setRefreshing(false)
            }

        })
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
        orientationLand = if (newConfig?.orientation === Configuration.ORIENTATION_LANDSCAPE)
            true
        else
            false
        Log.d("AAAA" , "onCon :" + orientationLand.toString())
        adapter?.setOrii(orientationLand)
    }
}
