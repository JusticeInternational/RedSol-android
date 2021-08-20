package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.Service
import com.cleteci.redsolidaria.models.User


class UserAdapter(
    private val context: Context?, private val list: MutableList<User>
) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {



    init {

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        var post = list[position]

        holder!!.title?.text = post.name+ " "+post.lastName
        holder!!.tvEmail?.text=post.email


        if (position==itemCount-1){
            holder.viewLine?.visibility= View.GONE
        }else{
            holder.viewLine?.visibility= View.VISIBLE
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {

        val itemView: View

        itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)


        return UserAdapter.ListViewHolder(itemView)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var layout = itemView.findViewById<RelativeLayout>(R.id.relativeCategory)
        val title: TextView? = itemView.findViewById<TextView>(R.id.tvOrganizationName)
        val tvEmail: TextView? = itemView.findViewById<TextView>(R.id.tvEmail)


        val viewLine = itemView.findViewById<View>(R.id.viewLine)

        fun bind(item: Service) {

        }
    }


}
