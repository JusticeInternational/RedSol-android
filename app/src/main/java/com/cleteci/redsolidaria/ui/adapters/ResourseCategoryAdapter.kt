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
import com.cleteci.redsolidaria.models.ResourceCategory

class ResourseCategoryAdapter
    (
    private val context: Context?, private val list: MutableList<ResourceCategory>,
    fragment: Fragment, typeInfl:Int, isFromScan:Boolean
) : RecyclerView.Adapter<ResourseCategoryAdapter.ListViewHolder>() {

    private val listener: ResourseCategoryAdapter.onItemClickListener

    private val inflaterLayType: Int

    private val isScan: Boolean

    init {
        this.listener = fragment as ResourseCategoryAdapter.onItemClickListener

        this.inflaterLayType = typeInfl as Int

        this.isScan=isFromScan
    }


    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = list[position]


        holder!!.title!!.setText(post.name)
        holder.body!!.setImageResource(post.photo)
        if (post.description!=null && post.description?.length!!>0){
            holder.tvDescription?.visibility= VISIBLE
            holder.tvDescription?.setText(post.description)

        }else{

            holder.tvDescription?.visibility= GONE

        }

        if (this.isScan){
            holder.ivNoUser?.visibility= VISIBLE
            holder.ivArrow?.setImageResource(R.drawable.ic_scan)

        }else{
            holder.ivNoUser?.visibility= GONE
            holder.ivArrow?.setImageResource(R.drawable.ic_right)

        }


        if (position==itemCount-1){
            holder.viewLine?.visibility=GONE
        }else{
            holder.viewLine?.visibility= VISIBLE
        }

        holder.ivArrow!!.setOnClickListener {
            listener.itemDetail(position)
        }

        holder.ivNoUser!!.setOnClickListener {
            listener.scanNoUserCategory(position)
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
        val viewLine = itemView.findViewById<View>(R.id.viewLine)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val ivArrow = itemView.findViewById<ImageView>(R.id.ivArrow)
        val ivNoUser = itemView.findViewById<ImageView>(R.id.ivNoUser)




        fun bind(item: ResourceCategory) {
            // title = item.post
            // body etc.
        }
    }

    interface onItemClickListener {

        fun clickScanCategory(postId: String)

        fun itemDetail(postId: Int)

        fun scanNoUserCategory(position: Int)
    }

}