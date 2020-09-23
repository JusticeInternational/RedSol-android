package com.cleteci.redsolidaria.ui.adapters

import java.lang.CharSequence
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resource

class ResourseAdapter(
    private val context: Context?, private val list: MutableList<Resource>,
    fragment: Fragment, typeInfl: Int
) : RecyclerView.Adapter<ResourseAdapter.ListViewHolder>() {

    private val listener: ResourseAdapter.onItemClickListener
    private val inflaterLayType: Int

    init {
        this.listener = fragment as ResourseAdapter.onItemClickListener
        this.inflaterLayType = typeInfl as Int

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = list[position]

        holder!!.title?.text = post.name
        holder!!.body?.setImageResource(post.photo)
        holder!!.tvCategory?.text = post.cate
        holder!!.tvHourHand?.text = post.hourHand
        holder!!.rbGeneral?.rating = post.ranking.toFloat()

        holder.layout?.setOnClickListener {
            listener.clickDetailResource(post.id.toString()!!)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val itemView: View

        if (inflaterLayType == 1) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_resourse, parent, false)

        } else  {

            itemView = LayoutInflater.from(context).inflate(R.layout.item_my_resourse, parent, false)
        }

        return ResourseAdapter.ListViewHolder(itemView)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<RelativeLayout>(R.id.parentr)
        val title: TextView? = itemView.findViewById<TextView>(R.id.tvName)
        val tvCategory: TextView? = itemView.findViewById<TextView>(R.id.tvCategory)
        val tvHourHand: TextView? = itemView.findViewById<TextView>(R.id.tvHourHand)
        val rbGeneral: RatingBar? = itemView.findViewById<RatingBar>(R.id.rbGeneral)
        val body: ImageView? = itemView.findViewById<ImageView>(R.id.imageview)

        fun bind(item: Resource) {

        }
    }

    interface onItemClickListener {

        fun clickDetailResource(postId: String)
    }

}