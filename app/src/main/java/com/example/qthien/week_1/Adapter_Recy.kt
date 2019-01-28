package com.example.qthien.week_1

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.qthien.week_1.R.id.txtXemthem
import kotlinx.android.synthetic.main.item_recy_landscape1.view.*
import kotlinx.android.synthetic.main.item_recy_portrait1.view.*
import android.text.method.TextKeyListener.clear
import android.widget.*
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.request.RequestOptions
import com.google.android.youtube.player.internal.v
import jp.wasabeef.glide.transformations.*
import com.example.qthien.week_1.R.id.recyclerView




class Adapter_Recy(var con : Context ,var l: ArrayList<Result>? ,var ori : Boolean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        val ORI = 0
        val HOR = 1
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        val inflater = LayoutInflater.from(viewGroup.getContext())
//        when(viewType) {
//            ORI -> {
//                var v1 = inflater.inflate(R.layout.item_recy_portrait1, viewGroup, false)
//                return ViewHolderPortrail(v1)
//            }
//
//            HOR -> {
//                var v2 = inflater.inflate(R.layout.item_recy_landscape1, viewGroup, false)
//                return ViewHolderLancescapel(v2)
//            }
//            else -> {
//                var v3 = inflater.inflate(R.layout.item_recy_portrait1, viewGroup, false)
//                return ViewHolderPortrail(v3)
//            }
//        }

        if(viewType == ORI){
            var v1 : View?
            v1 = inflater.inflate(R.layout.item_recy_portrait1, viewGroup , false)
            return ViewHolderPortrail(v1)
        }
        else{
                var v2 : View?
                v2 = inflater.inflate(R.layout.item_recy_landscape1, viewGroup , false)
                return ViewHolderLancescapel(v2)
            }
    }

    fun setOrii(b : Boolean){
        ori = b
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = l?.size ?: 0

    override fun getItemViewType(position: Int): Int {
        if (ori) return HOR
        else return ORI
    }

    fun convertDpToPx(context: Context, dp: Float): Float {
        return dp * context.resources.displayMetrics.density
    }


    override fun onBindViewHolder(p0: RecyclerView.ViewHolder, p1: Int) {
        var r : Result? = l?.get(p1)
        var i = Intent(con , DetailActivity::class.java)

        p0.setIsRecyclable(false);
        when(p0){
            is ViewHolderPortrail -> {
                p0.txtDecrip.setText(r?.overview)
                p0.txtName.setText(r?.title)

//                val multi = MultiTransformation<Bitmap>(
//                    BlurTransformation(25),
//                    RoundedCornersTransformation(128, 0, RoundedCornersTransformation.CornerType.BOTTOM)
//                )

                if(r!!.voteAverage > 6.5F) {

                    var layouuParams = RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT , convertDpToPx(con , 220f).toInt())
                    p0.img.layoutParams = layouuParams
                    GlideApp.with(con).load("http://image.tmdb.org/t/p/w500/${r.backdropPath}")
                        .placeholder(R.drawable.img_place)
                        .into(p0.img)

                    p0.imgPlay.visibility = View.VISIBLE
                }else
                    GlideApp.with(con).load("http://image.tmdb.org/t/p/w500/${r?.posterPath}")
                        .apply(RequestOptions.bitmapTransform(CropCircleTransformation()))
                        .placeholder(R.drawable.img_place)
                        .into(p0.img)


                p0.txtXemThem.setOnClickListener{
                    i.putExtra("Result" , r)
                    (con as MainActivity).startActivity(i)
                }

                p0.layout.setOnClickListener{
                    i.putExtra("Result" , r)
                    (con as MainActivity).startActivity(i)
                }
            }

            is ViewHolderLancescapel ->{
                p0.txtDecrip.setText(r?.overview)
                p0.txtName.setText(r?.title)

                if(r!!.voteAverage <= 6.5F)
                    p0.imgPlay.visibility = View.GONE

                GlideApp.with(con).load("http://image.tmdb.org/t/p/w500/${r?.backdropPath}")
                    .placeholder(R.drawable.img_place)
                    .into(p0.img)

                p0.txtXemThem.setOnClickListener{
                    i.putExtra("Result" , r)
                    (con as MainActivity).startActivity(i)
                }
                p0.layout.setOnClickListener{
                    i.putExtra("Result" , r)
                    (con as MainActivity).startActivity(i)
                }
            }

            else -> p0
        }

//        if(ori){
//            Toast.makeText(con as MainActivity , "Ngang" , Toast.LENGTH_LONG)
//        }
//        else
//            Toast.makeText(con as MainActivity , "Dọc" , Toast.LENGTH_LONG)

    }

    inner class ViewHolderPortrail(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView = itemView.findViewById(R.id.img_port)
        var txtName : TextView = itemView.findViewById(R.id.txtName_port)
        var txtDecrip : TextView = itemView.findViewById(R.id.txtDecrip)
        var txtXemThem : TextView = itemView.findViewById(R.id.txtXemthem)
        var layout : RelativeLayout = itemView.layoutRela
        var imgPlay : ImageView = itemView.imgPlay
    }

    inner class ViewHolderLancescapel(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var img : ImageView = itemView.img_port1
        var txtName : TextView = itemView.txtName_port1
        var txtDecrip : TextView = itemView.txtDecrip1
        var txtXemThem : TextView = itemView.txtXemthem1
        var layout : RelativeLayout = itemView.layoutRela1
        var imgPlay : ImageView = itemView.imgPlay1
    }

//    inner class ViewHolderLandscape(itemView: View) : RecyclerView.ViewHolder(itemView) {
//
//    }
}