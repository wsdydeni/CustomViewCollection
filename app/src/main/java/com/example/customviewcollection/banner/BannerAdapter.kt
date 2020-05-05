package com.example.customviewcollection.banner

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.customviewcollection.R

const val MAX_VALUE = 500

class BannerAdapter : RecyclerView.Adapter<BannerAdapter.MyViewHolder>() {

    private var mList = arrayListOf<BannerInfo>()

    fun setData(list : List<BannerInfo>) {
        mList.clear()
        mList.addAll(list)
        notifyDataSetChanged()
    }

    fun getListSize() : Int = mList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder =
        MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_banner,parent,false))

    override fun getItemCount(): Int = if (mList.size > 1) { MAX_VALUE } else { mList.size }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val realPosition = if(mList.size == 0) { 0 } else{ (position - 1 + mList.size) % mList.size }
        val image = holder.itemView.findViewById<ImageView>(R.id.banner_image)
        Glide.with(holder.itemView.context).
            load(mList[realPosition].image).
            transform(CenterCrop(),RoundedCorners(20)).
            into(image)
    }

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

}