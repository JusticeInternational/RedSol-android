package com.cleteci.redsolidaria.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category
import com.cleteci.redsolidaria.models.Organization
import com.cleteci.redsolidaria.models.Service

class ServicesSearchAdapter(private val context: Context?,
                            private val list: MutableList<ServiceSearch>,
                            private val listener: OnItemClickListener): RecyclerView.Adapter<ServicesSearchAdapter.ListViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val serviceSearch = list[position]
        holder.itemView.setOnClickListener { listener.onServiceSearchClicked(position) }
        holder.name.text = serviceSearch.service.name
        holder.icon.setImageResource(serviceSearch.service.category.iconId)
        holder.categoryName.text = serviceSearch.service.category.name
        holder.organizationName.text = serviceSearch.organizationName
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_service_search, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val icon: ImageView = itemView.findViewById(R.id.imgCategory)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategory)
        val organizationName: TextView = itemView.findViewById(R.id.tvOrganizationName)


    }

    class ServiceSearch (val service: Service,
                         val organizationId: String,
                         val organizationName: String)

    interface OnItemClickListener {
        fun onServiceSearchClicked(position: Int)
    }

}