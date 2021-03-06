package com.cleteci.redsolidaria.ui.adapters

import java.lang.CharSequence
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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Resource

class ResourseAdapter(
    private val context: Context?, private val list: MutableList<Resource>,
    fragment: onItemClickListener, typeInfl: Int, isFromScan:Boolean
) : RecyclerView.Adapter<ResourseAdapter.ListViewHolder>() {

    private val listener: onItemClickListener
    private val inflaterLayType: Int
    private val isScan: Boolean

    init {
        this.listener = fragment
        this.inflaterLayType = typeInfl
        this.isScan=isFromScan

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = list[position]

        holder!!.title?.text = post.name
        holder!!.body?.setImageResource(post.photo)
        holder!!.tvLocation?.text = post.location
        holder!!.tvHourHand?.text = post.hourHand
        holder!!.rbGeneral?.rating = post.ranking.toFloat()

        if (post.description!=null && post.description?.length!!>0){
            holder.tvDescription?.visibility= View.VISIBLE
            holder.tvDescription?.setText(post.description)

        }else{

            holder.tvDescription?.visibility= View.GONE

        }

        if (this.isScan){
            holder.ivNoUser?.visibility=VISIBLE
            holder.ivArrow?.setImageResource(R.drawable.ic_scan)


        }else{
            holder.ivNoUser?.visibility= GONE
            holder.ivArrow?.setImageResource(R.drawable.ic_right)

        }

        holder.ivArrow?.setOnClickListener {
            listener.clickDetailResource(position!!, post.name, post.isGeneric)
        }

        holder.ivNoUser?.setOnClickListener {
            listener.scanNoUserResource(post.id, post.name, post.isGeneric)
        }

        if (position==itemCount-1){
            holder.viewLine?.visibility= GONE
        }else{
            holder.viewLine?.visibility= VISIBLE
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val itemView: View

        if (inflaterLayType == 1) {
            itemView = LayoutInflater.from(context).inflate(R.layout.item_resourse, parent, false)

        } else if (inflaterLayType == 3)  {

            itemView = LayoutInflater.from(context).inflate(R.layout.item_my_resourse_provider, parent, false)
        }else{
            itemView = LayoutInflater.from(context).inflate(R.layout.item_my_resourse, parent, false)
        }

        return ResourseAdapter.ListViewHolder(itemView)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<RelativeLayout>(R.id.parentr)
        val title: TextView? = itemView.findViewById<TextView>(R.id.tvName)
        val tvLocation: TextView? = itemView.findViewById<TextView>(R.id.tvLocation)
        val tvHourHand: TextView? = itemView.findViewById<TextView>(R.id.tvHourHand)
        val rbGeneral: RatingBar? = itemView.findViewById<RatingBar>(R.id.rbGeneral)
        val body: ImageView? = itemView.findViewById<ImageView>(R.id.imageview)
        val viewLine = itemView.findViewById<View>(R.id.viewLine)
        val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        val ivArrow = itemView.findViewById<ImageView>(R.id.ivArrow)

        val ivNoUser = itemView.findViewById<ImageView>(R.id.ivNoUser)


        fun bind(item: Resource) {

        }
    }

    interface onItemClickListener {
        fun clickScanresourse(postId: String)
        fun clickDetailResource(postId: Int,name: String, isGeneric:Boolean)
        fun scanNoUserResource(postId: String,name: String, isGeneric:Boolean)
    }
}
