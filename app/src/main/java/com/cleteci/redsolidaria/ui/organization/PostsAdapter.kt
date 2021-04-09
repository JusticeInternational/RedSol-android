package com.cleteci.redsolidaria.ui.organization

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Post

class PostsAdapter(private val dataSet: java.util.ArrayList<Post>, private val context: Context) :
    RecyclerView.Adapter<PostsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var date: TextView
        var description: TextView
        var logo: ImageView
        init {
            title = view.findViewById(R.id.title)
            date = view.findViewById(R.id.date)
            description = view.findViewById(R.id.description)
            logo = view.findViewById(R.id.logo)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.post_item, viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.title.text = dataSet[position].title
        viewHolder.date.text = dataSet[position].date
        viewHolder.description.text = dataSet[position].description
//        Glide.with(context)
//            .load(R.drawable.organization_logo_sample)
//            .apply(RequestOptions().circleCrop())
//            .into(viewHolder.logo)
    }

    override fun getItemCount() = dataSet.size

}