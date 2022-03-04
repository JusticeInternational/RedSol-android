package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.BaseApp
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resource
import com.cleteci.redsolidaria.util.getCategoryIconByIconString

class ResourcesAdapter(private val context: Context,
                       private val list: ArrayList<Resource>,
                       private val listener: OnResourceClickListener,
                       private val isScan: Boolean) : RecyclerView.Adapter<ResourcesAdapter.ListViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val resource = list[position]
        val type: Resource.Type

        when {
            resource.service != null -> {
                type = Resource.Type.SERVICE
                val service = resource.service!!
                val category = service.category!!
                holder.name.text = service.name
                holder.icon.setImageResource(getCategoryIconByIconString(category.icon))
                holder.categoryName.text = category.name
                holder.description.visibility = VISIBLE
                holder.description.text = category.description
            }
            resource.category != null -> {
                type = Resource.Type.CATEGORY
                val category = resource.category!!
                holder.name.text = category.name
                holder.icon.setImageResource(getCategoryIconByIconString(category.icon))
                holder.categoryName.text = category.name
                holder.description.visibility = GONE
            }
            else -> {
                type = Resource.Type.OTHER
                holder.itemView.visibility = GONE
            }
        }

        holder.itemView.setOnClickListener {
            if (BaseApp.sharedPreferences.isProviderService) {
                listener.resourceDetail(position, type)
            } else {
                listener.resourceOrganizationProfile(resource, type)
            }
        }

        if (isScan) {
            holder.imgNoUser.visibility = VISIBLE
            holder.imgScan.visibility = VISIBLE


            holder.imgNoUser.setOnClickListener {
                listener.resourceScanNoUser(position, type)
            }
            holder.imgScan.setOnClickListener {
                listener.resourceScan(position, type)
            }
        } else {
            holder.imgNoUser.visibility = GONE
            holder.imgScan.visibility = GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = LayoutInflater.from(context).inflate(R.layout.item_resource, parent, false)
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val name: TextView = itemView.findViewById(R.id.tvName)
        val icon: ImageView = itemView.findViewById(R.id.imgCategory)
        val categoryName: TextView = itemView.findViewById(R.id.tvCategory)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val imgNoUser: ImageView = itemView.findViewById(R.id.imgNoUser)
        val imgScan: ImageView = itemView.findViewById(R.id.imgScan)

    }

    interface OnResourceClickListener {
        fun resourceDetail(position: Int, type: Resource.Type)

        fun resourceScan(position: Int, type: Resource.Type)

        fun resourceScanNoUser(position: Int, type: Resource.Type)

        fun resourceOrganizationProfile(resource: Resource, type: Resource.Type)
    }

}