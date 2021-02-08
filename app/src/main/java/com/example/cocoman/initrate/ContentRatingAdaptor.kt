package com.example.cocoman.initrate

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.cocoman.R
import com.example.cocoman.data.ContentRating

class ContentRatingAdaptor(
    val context: Context,
    val onContentRatingStatusChangeListener: onContentRatingStatusChangeListener
) : RecyclerView.Adapter<ContentRatingAdaptor.ViewHolder>() {
    val contentList: ArrayList<ContentRating> = ArrayList()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val contentTitle: TextView
        val contentYear: TextView
        val contentScore: RatingBar
        val contentPoster: ImageView

        init {
            contentTitle = itemView.findViewById(R.id.content_title)
            contentYear = itemView.findViewById(R.id.content_year)
            contentScore = itemView.findViewById(R.id.content_star)
            contentPoster = itemView.findViewById(R.id.content_poster)
        }

    }

    fun addContents(contents: List<ContentRating>) {
        contentList.addAll(contents)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.rating_content_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return contentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.contentTitle.setText(contentList.get(position).title)
        holder.contentYear.setText(contentList.get(position).year.toString())
        holder.contentScore.rating = contentList.get(position).score
        holder.contentScore.setOnRatingBarChangeListener(object :
            RatingBar.OnRatingBarChangeListener {
            override fun onRatingChanged(p0: RatingBar?, p1: Float, p2: Boolean) {
                if (contentList[position].score == 0.0F && p1 != 0.0F && p2 == true) {
                    onContentRatingStatusChangeListener.onContentRated(
                        position,
                        contentList.get(position).id,
                        p1
                    )
                } else if (contentList[position].score != 0.0F && p1 == 0.0F && p2 == true) {
                    onContentRatingStatusChangeListener.onContentUnrated(
                        position,
                        contentList.get(position).id,
                        p1
                    )
                }
                if (p2 == true) {
                    contentList[position].score = p1
                }
                Log.d("rate", "" + position + ":" + contentList[position].score)
            }
        })
    }

}