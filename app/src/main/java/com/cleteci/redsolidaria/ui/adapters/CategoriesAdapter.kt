package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Category

class CategoriesAdapter(private val context: Context?, private val list: MutableList<Category>,
                        fragment: Fragment, inflaterLayType: Int, isFromScan:Boolean): RecyclerView.Adapter<CategoriesAdapter.ListViewHolder>() {

    private val listener: OnItemClickListener
    private val inflaterLayType: Int
    private val isScan: Boolean

    init {
        this.listener = fragment as OnItemClickListener
        this.inflaterLayType = inflaterLayType
        this.isScan = isFromScan
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val category = list[position]

        holder.itemView.setOnClickListener { listener.clickScanCategory(position) }
        holder.title!!.text = category.name
        holder.body!!.setImageResource(category.iconId)
        if (category.description!= null && category.description.isNotEmpty()) {
            holder.tvDescription?.visibility= VISIBLE
            holder.tvDescription?.text = category.description
        } else {
            holder.tvDescription?.visibility= GONE
        }

        if (this.isScan) {
            holder.ivNoUser?.visibility= VISIBLE
            holder.ivArrow?.setImageResource(R.drawable.ic_scan)
        } else {
            holder.ivNoUser?.visibility= GONE
            holder.ivArrow?.setImageResource(R.drawable.ic_right)
        }

        if (position == itemCount - 1) {
            holder.viewLine?.visibility=GONE
        } else {
            holder.viewLine?.visibility= VISIBLE
        }

        if (holder.ivArrow != null) {
            holder.ivArrow.setOnClickListener {
                listener.itemDetail(position)
            }
        } else {
            holder.itemView.setOnClickListener {
                listener.itemDetail(position)
            }
        }

        holder.ivNoUser?.setOnClickListener {
            listener.scanNoUserCategory(position)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView: View = when (inflaterLayType) {
            1 -> {
                LayoutInflater.from(context).inflate(R.layout.item_resource_category, parent, false)
            }
            2 -> {
                LayoutInflater.from(context).inflate(R.layout.item_resource_cat_advanced, parent, false)
            }
            else -> {
                LayoutInflater.from(context).inflate(R.layout.item_my_resourse_provider, parent, false)
            }
        }
        return ListViewHolder(itemView)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout: RelativeLayout? = itemView.findViewById(R.id.relativeCategory)
        val title: TextView? = itemView.findViewById(R.id.tvOrganizationName)
        val body: ImageView? = itemView.findViewById(R.id.imgOrganization)
        val viewLine: View? = itemView.findViewById<View>(R.id.viewLine)
        val tvDescription: TextView? = itemView.findViewById(R.id.tvDescription)
        val ivArrow: ImageView? = itemView.findViewById(R.id.ivArrow)
        val ivNoUser: ImageView? = itemView.findViewById(R.id.ivNoUser)
    }

    interface OnItemClickListener {

        fun clickScanCategory(position: Int)

        fun itemDetail(postId: Int)

        fun scanNoUserCategory(position: Int)
    }

}