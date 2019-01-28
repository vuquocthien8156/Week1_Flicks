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
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Parcelable
import android.os.PersistableBundle
import android.support.v4.content.ContextCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.abc_search_view.view.*


class MainActivity : AppCompatActivity() {
    var video : Video? = null
    var videoSearch : ArrayList<Result> = ArrayList()
    var orientationLand : Boolean = false
    var adapter : Adapter_Recy? = null

    var stateList : Parcelable? = null
    private val LIST_KEY_STATE: String = "123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
//        supportActionBar?.setDisplayShowHomeEnabled(true)
//        supportActionBar?.setDisplayUseLogoEnabled(true)
//        supportActionBar?.setLogo(R.drawable.ic_logo_moive)


        supportActionBar?.setDisplayShowTitleEnabled(false)
//        supportActionBar?.setDisplayHomeAsUpEnabled(true)
//        supportActionBar?.setHomeAsUpIndicator(R.drawable.icon_movie)

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
        val searchEditText = searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text) as EditText
        searchEditText.setTextColor(Color.WHITE)
        searchEditText.setHintTextColor(Color.WHITE)
        searchView.maxWidth = 265
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
                        txtNotFind.visibility = View.GONE
                        video?.results?.clear()
                        video?.results?.addAll(listResult)
                        adapter?.notifyDataSetChanged()
                    }
                    else{
                        video?.results?.clear()
                        adapter?.notifyDataSetChanged()
                        txtNotFind.visibility = View.VISIBLE
                        txtNotFind.text = "Không có kết quả cho '${p0.toString()}' "
                    }
                }
                else{
                    txtNotFind.visibility = View.GONE
                    video?.results?.clear()
                    video?.results?.addAll(videoSearch)
                    adapter?.notifyDataSetChanged()
                }
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }



    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)

        stateList = recyclerView.layoutManager?.onSaveInstanceState()
        outState?.putParcelable( LIST_KEY_STATE , stateList)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {

        super.onRestoreInstanceState(savedInstanceState)
        if(savedInstanceState != null){
            stateList = savedInstanceState.getParcelable(LIST_KEY_STATE)

            Log.d("aaaaaaa" , "2 :" + stateList.toString())
        }

    }

    override fun onResume() {
        super.onResume()
        Log.d("aaaaaaa" , "1 :" + stateList.toString())

        if (stateList != null) {
            recyclerView.layoutManager?.onRestoreInstanceState(stateList);
        }
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
                videoSearch.clear()
                videoSearch.addAll(video!!.results)
                val orientation = resources.configuration.orientation
                orientationLand = Configuration.ORIENTATION_LANDSCAPE == orientation
                adapter = Adapter_Recy(this@MainActivity , video?.results  , orientationLand)
                recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
                recyclerView.adapter = adapter
                Log.d("aaaaaaa" , "ccccccccc")

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
