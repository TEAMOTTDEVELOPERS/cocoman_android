package com.example.cocoman.adapter

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.cocoman.R
import com.example.cocoman.activity.InitialRatingActivity
import com.example.cocoman.data.ContentRating
import com.example.cocoman.network.MasterApplication
import kotlinx.android.synthetic.main.rating_content_view.view.*

class ContentRatingAdaptor (
    val contentList: ArrayList<ContentRating>,
    val inflater: LayoutInflater
):RecyclerView.Adapter<ContentRatingAdaptor.ViewHolder>(){
    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val contentTitle: TextView
        val contentYear: TextView
        val contentScore: TextView
        val contentPoster: ImageView

        init {
            contentTitle=itemView.findViewById(R.id.content_title)
            contentYear = itemView.findViewById(R.id.content_year)
            contentScore = itemView.findViewById(R.id.content_rating)
            contentPoster = itemView.findViewById(R.id.content_poster)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflater.inflate(R.layout.rating_content_view,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentTitle.setText(contentList.get(position).title)
        holder.contentYear.setText(contentList.get(position).year.toString())
        holder.contentScore.setText(contentList.get(position).score.toString())

//        Glide.with(activity)
//            .load(holder.contentPoster)
//            .placeholder(R.drawable.harrypotterposter)
//            .into(holder.contentPoster)
    }
}