package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.util.getCategoryIconByIconString

class ResourceAdapter(
        private val context: Context?,
        private val list: MutableList<Service>,
        private val listener: OnItemClickListener,
        private val inflaterLayType: Int,
        private val isScan: Boolean
) : RecyclerView.Adapter<ResourceAdapter.ListViewHolder>() {

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val service = list[position]

        holder.title?.text = service.name
        holder.body!!.setImageResource(getCategoryIconByIconString(service.serviceCategory!!.icon()))
        holder.tvLocation?.text = service.location
        holder.tvHourHand?.text = service.hourHand
        holder.rbGeneral?.rating = service.ranking.toFloat()

        if (!service.description.isNullOrEmpty()) {
            holder.tvDescription?.visibility = VISIBLE
            holder.tvDescription?.text = service.description
        } else {
            holder.tvDescription?.visibility = GONE
        }

        if (this.isScan) {
            holder.ivNoUser?.visibility = VISIBLE
            holder.ivArrow?.setImageResource(R.drawable.ic_scan)
        } else {
            holder.ivNoUser?.visibility = GONE
            holder.ivArrow?.setImageResource(R.drawable.ic_right)
        }

        holder.ivArrow?.setOnClickListener {
            service.name?.let { it1 ->
                listener.clickDetailResource(position,
                    it1, service.isGeneric)
            }
        }

        holder.ivNoUser?.setOnClickListener {
            service.name?.let { it1 ->
                listener.scanNoUserResource(service.id,
                    it1, service.isGeneric)
            }
        }

        if (position == itemCount - 1) {
            holder.viewLine?.visibility = GONE
        } else {
            holder.viewLine?.visibility = VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = when (inflaterLayType) {
            1 -> LayoutInflater.from(context).inflate(R.layout.item_resourse, parent, false)
            3 -> LayoutInflater.from(context).inflate(R.layout.item_my_resourse_provider, parent, false)
            else -> LayoutInflater.from(context).inflate(R.layout.item_my_resourse, parent, false)
        }

        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout: RelativeLayout = itemView.findViewById(R.id.relativeCategory)
        val title: TextView? = itemView.findViewById(R.id.tvOrganizationName)
        val tvLocation: TextView? = itemView.findViewById(R.id.tvLocation)
        val tvHourHand: TextView? = itemView.findViewById(R.id.tvHourHand)
        val rbGeneral: RatingBar? = itemView.findViewById(R.id.rbGeneral)
        val body: ImageView? = itemView.findViewById(R.id.imgOrganization)
        val viewLine: View? = itemView.findViewById(R.id.viewLine)
        val tvDescription: TextView? = itemView.findViewById(R.id.tvDescription)
        val ivArrow: ImageView? = itemView.findViewById(R.id.ivArrow)
        val ivNoUser: ImageView? = itemView.findViewById(R.id.ivNoUser)
    }

    interface OnItemClickListener {
        fun clickScanResource(postId: String)
        fun clickDetailResource(postId: Int, name: String, isGeneric: Boolean)
        fun scanNoUserResource(postId: String, name: String, isGeneric: Boolean)
    }
}
