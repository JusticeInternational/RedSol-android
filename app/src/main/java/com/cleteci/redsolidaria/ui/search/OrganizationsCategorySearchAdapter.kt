package com.cleteci.redsolidaria.ui.search

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R

class OrganizationsCategorySearchAdapter(private val context: Context?,
                                         private val list: MutableList<OrganizationCategory>,
                                         private val listener: OnItemClickListener): RecyclerView.Adapter<OrganizationsCategorySearchAdapter.ListViewHolder>() {


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val organizationCategory= list[position]
        holder.itemView.setOnClickListener { listener.onOrganizationCategorySearchClicked(position) }
        holder.organizationName.text = organizationCategory.organizationName
        holder.imgOrganization.setImageResource(organizationCategory.organizationIcon)
        holder.tvServiceName.text = organizationCategory.categoryName
        holder.categoryName.text = organizationCategory.categoryName
        holder.schedule.text = organizationCategory.organizationSchedule
        holder.imgCategory.setImageResource(organizationCategory.categoryIcon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_organization_category_search, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val organizationName: TextView = itemView.findViewById(R.id.tvOrganizationName)
        val imgOrganization: ImageView = itemView.findViewById(R.id.imgOrganization)
        val tvServiceName: TextView = itemView.findViewById(R.id.tvServiceName)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val schedule: TextView = itemView.findViewById(R.id.tvSchedule)
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
    }

    class OrganizationCategory (val organizationId: String,
                                val organizationIcon: Int,
                                val organizationName: String,
                                val organizationRanking: Int,
                                val organizationSchedule: String,
                                val categoryName: String,
                                val categoryIcon: Int)

    interface OnItemClickListener {
        fun onOrganizationCategorySearchClicked(position: Int)
    }

}