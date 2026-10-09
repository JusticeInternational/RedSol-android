package com.cleteci.redsolidaria.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cleteci.redsolidaria.R
import com.cleteci.redsolidaria.models.User

class UserAdapter(
    private val context: Context?,
    private val list: MutableList<User>
) : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = list[position]

        val fullName = listOfNotNull(
            user.name.takeIf { it.isNotBlank() },
            user.lastName?.takeIf { it.isNotBlank() }
        ).joinToString(" ")

        holder.title?.text = if (fullName.isNotBlank()) fullName else "Beneficiario"

        if (!user.email.isNullOrBlank()) {
            holder.tvEmail?.text = user.email
            holder.tvEmail?.visibility = View.VISIBLE
        } else {
            holder.tvEmail?.visibility = View.GONE
        }

        if (position == itemCount - 1) {
            holder.viewLine?.visibility = View.GONE
        } else {
            holder.viewLine?.visibility = View.VISIBLE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val itemView = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ListViewHolder(itemView)
    }

    fun removeAt(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layout: RelativeLayout? = itemView.findViewById(R.id.relativeCategory)
        val title: TextView? = itemView.findViewById(R.id.tvOrganizationName)
        val tvEmail: TextView? = itemView.findViewById(R.id.tvEmail)
        val viewLine: View? = itemView.findViewById(R.id.viewLine)
    }
}
