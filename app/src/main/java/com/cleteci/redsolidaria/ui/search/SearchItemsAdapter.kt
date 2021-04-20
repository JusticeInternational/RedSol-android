package com.cleteci.redsolidaria.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R

class SearchItemsAdapter(private val context: Context?,
                              private var list: ArrayList<SearchItem>,
                              private val listener: OnItemClickListener):
    RecyclerView.Adapter<SearchItemsAdapter.ListViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val searchItem = list[position]
        holder.itemView.setOnClickListener { listener.onSearchItemClicked(searchItem.id) }// while is just by category
        holder.name.text = searchItem.name
        holder.icon.setImageResource(searchItem.iconId)
        holder.icon.visibility = View.VISIBLE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false)
        return ListViewHolder(itemView)
    }

    fun filterList(filterList: ArrayList<SearchItem>) {
        list = filterList
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val icon: ImageView = itemView.findViewById(R.id.icon)
    }

    class SearchItem(val id: String, // while is just by category
                     val name: String,
                     val iconId: Int)

    interface OnItemClickListener {
        fun onSearchItemClicked(text: String)
    }

}