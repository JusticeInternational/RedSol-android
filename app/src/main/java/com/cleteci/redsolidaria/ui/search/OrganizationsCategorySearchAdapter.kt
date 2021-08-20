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
        holder.categoryName.text = organizationCategory.categoryName
        holder.distance.text = organizationCategory.organizationDistance
        holder.schedule.text = organizationCategory.organizationSchedule
        holder.imgCategory.setImageResource(organizationCategory.categoryIcon)

        clearRanking(holder)
         when (organizationCategory.organizationRanking) {
             1 -> {
                 holder.start1.setImageResource(R.drawable.ic_star_full_24)
             }
             2 -> {
                 holder.start1.setImageResource(R.drawable.ic_star_full_24)
                 holder.start2.setImageResource(R.drawable.ic_star_full_24)
             }
             3 -> {
                 holder.start1.setImageResource(R.drawable.ic_star_full_24)
                 holder.start2.setImageResource(R.drawable.ic_star_full_24)
                 holder.start3.setImageResource(R.drawable.ic_star_full_24)
             }
             4 -> {
                 holder.start1.setImageResource(R.drawable.ic_star_full_24)
                 holder.start2.setImageResource(R.drawable.ic_star_full_24)
                 holder.start3.setImageResource(R.drawable.ic_star_full_24)
                 holder.start4.setImageResource(R.drawable.ic_star_full_24)
             }
             5 -> {
                 holder.start1.setImageResource(R.drawable.ic_star_full_24)
                 holder.start2.setImageResource(R.drawable.ic_star_full_24)
                 holder.start3.setImageResource(R.drawable.ic_star_full_24)
                 holder.start4.setImageResource(R.drawable.ic_star_full_24)
                 holder.start5.setImageResource(R.drawable.ic_star_full_24)
             }
         }
    }

    private fun clearRanking(holder: ListViewHolder) {
        holder.start1.setImageResource(R.drawable.ic_star_24)
        holder.start2.setImageResource(R.drawable.ic_star_24)
        holder.start3.setImageResource(R.drawable.ic_star_24)
        holder.start4.setImageResource(R.drawable.ic_star_24)
        holder.start5.setImageResource(R.drawable.ic_star_24)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_organization_category_search, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val organizationName: TextView = itemView.findViewById(R.id.tvOrganizationName)
        val imgOrganization: ImageView = itemView.findViewById(R.id.imgOrganization)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val distance: TextView = itemView.findViewById(R.id.tvDistance)
        val schedule: TextView = itemView.findViewById(R.id.tvSchedule)
        val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
        val start1: ImageView = itemView.findViewById(R.id.start_1)
        val start2: ImageView = itemView.findViewById(R.id.start_2)
        val start3: ImageView = itemView.findViewById(R.id.start_3)
        val start4: ImageView = itemView.findViewById(R.id.start_4)
        val start5: ImageView = itemView.findViewById(R.id.start_5)
    }

    class OrganizationCategory (val organizationId: String,
                                val organizationIcon: Int,
                                val organizationName: String,
                                val organizationRanking: Int,
                                val organizationSchedule: String,
                                val organizationDistance: String,
                                val categoryName: String,
                                val categoryIcon: Int)

    interface OnItemClickListener {
        fun onOrganizationCategorySearchClicked(position: Int)
    }

}