package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category

class CategoriesSearchAdapter(private val context: Context?,
                              private val list: MutableList<Category>,
                              private val listener: OnItemClickListener): RecyclerView.Adapter<CategoriesSearchAdapter.ListViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val category = list[position]
        holder.itemView.setOnClickListener { listener.clickScanCategory(position) }
        holder.title.text = category.name
        if (category.iconId != -1) {
            holder.icon.setImageResource(category.iconId)
            holder.icon.visibility = View.VISIBLE
        } else {
            holder.icon.visibility = View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_category_search, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.tvName)
        val icon: ImageView = itemView.findViewById(R.id.imgCategory)
    }

    interface OnItemClickListener {
        fun clickScanCategory(position: Int)
    }

}