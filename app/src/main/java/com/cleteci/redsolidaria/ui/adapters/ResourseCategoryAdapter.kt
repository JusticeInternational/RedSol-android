package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.ResourseCategory

class ResourseCategoryAdapter
    (
    private val context: Context?, private val list: MutableList<ResourseCategory>,
    fragment: Fragment, typeInfl:Int
) : RecyclerView.Adapter<ResourseCategoryAdapter.ListViewHolder>() {

    private val listener: ResourseCategoryAdapter.onItemClickListener

    private val inflaterLayType: Int

    init {
        this.listener = fragment as ResourseCategoryAdapter.onItemClickListener

        this.inflaterLayType = typeInfl as Int
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = list[position]


        holder!!.title!!.setText(post.name)
        holder.body!!.setImageResource(post.photo)

        holder.layout!!.setOnClickListener {
            listener.itemDetail(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View

        if (inflaterLayType==1) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_resource_category, parent, false)

        } else if (inflaterLayType==2){
            itemView = LayoutInflater.from(context).inflate(R.layout.item_resource_cat_advanced, parent, false)
        }else if (inflaterLayType==3){

            itemView = LayoutInflater.from(context).inflate(R.layout.item_resource_cat_map_list, parent, false)
        }else {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_my_resourse_provider, parent, false)
        }


        return ResourseCategoryAdapter.ListViewHolder(itemView)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<RelativeLayout>(R.id.parentr)
        val title = itemView.findViewById<TextView>(R.id.tvName)
        val body = itemView.findViewById<ImageView>(R.id.imageview)

        fun bind(item: ResourseCategory) {
            // title = item.post
            // body etc.
        }
    }

    interface onItemClickListener {

        fun itemDetail(postId: Int)
    }

}