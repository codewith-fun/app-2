package com.mcode.app2.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mcode.app2.R
import com.mcode.app2.databinding.ListQuickLinksBinding
import java.util.*
import kotlin.collections.ArrayList

/**
 *  /////////////////////////////////////////////////////////
 *  ////////// Quick links Adapters and interface  //////////
 *  /////////////////////////////////////////////////////////
 */


interface ClickOnQuickLinks{
    fun OnQuickLink(position: Int?)
}

class QuickLinksAdapter(val context: Context, val listeners:ClickOnQuickLinks):RecyclerView.Adapter<QuickLinksAdapter.ViewHolder>(){
    private var mData = ArrayList<QuickLinks>()

    @SuppressLint("NotifyDataSetChanged")
    fun upDateList(quickLink: List<QuickLinks>) {
        this.mData.clear()
        this.mData = (quickLink as ArrayList<QuickLinks>)
        notifyDataSetChanged()
    }
    class ViewHolder(val binding:ListQuickLinksBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListQuickLinksBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(mData[position]){
                val data = mData[position]
                binding.tvTitle.text = data.title
                Glide.with(context).load(data.ic_image).into(binding.ivQuick)
                holder.itemView.setOnClickListener { listeners.OnQuickLink(position) }
            }

        }
    }

    override fun getItemCount(): Int {
        return if (mData.size!=0)
            mData.size
        else
            0
    }
}

/**
 *  /////////////////////////////////////////////////////////
 *  //////////  Quick links Models  //////////
 *  /////////////////////////////////////////////////////////
 */

data class QuickLinks(
     val title:String?=null,
     val description:String?=null,
     val link:String?=null,
    @DrawableRes val ic_image:Int
)

/**
 *  /////////////////////////////////////////////////////////
 *  //////////  Quick Links ArrayList  //////////
 *  /////////////////////////////////////////////////////////
 */

class QuickLinksRepository{
    fun getAllQuickLinks():List<QuickLinks>{
        return listOf(
            QuickLinks("Google","","https://google.com/", R.drawable.ic_google),
            QuickLinks("YouTube","","https://youtube.com/", R.drawable.ic_youtube),
            QuickLinks("Twitter","","https://twitter.com/", R.drawable.ic_twitter),
            QuickLinks("Facebook","","https://facebook.com/", R.drawable.ic_facebook),

            QuickLinks("Google","","https://google.com/", R.drawable.ic_google),
            QuickLinks("YouTube","","https://youtube.com/", R.drawable.ic_youtube),
            QuickLinks("Twitter","","https://twitter.com/", R.drawable.ic_twitter),
            QuickLinks("Facebook","","https://facebook.com/", R.drawable.ic_facebook),

            QuickLinks("Google","","https://google.com/", R.drawable.ic_google),
            QuickLinks("YouTube","","https://youtube.com/", R.drawable.ic_youtube),
            QuickLinks("Twitter","","https://twitter.com/", R.drawable.ic_twitter),
            QuickLinks("Facebook","","https://facebook.com/", R.drawable.ic_facebook),

            QuickLinks("Google","","https://google.com/", R.drawable.ic_google),
            QuickLinks("YouTube","","https://youtube.com/", R.drawable.ic_youtube),
            QuickLinks("Twitter","","https://twitter.com/", R.drawable.ic_twitter),
            QuickLinks("Facebook","","https://facebook.com/", R.drawable.ic_facebook),

            )
    }
}