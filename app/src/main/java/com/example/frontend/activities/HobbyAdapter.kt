package com.example.frontend.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.frontend.R
import com.example.frontend.models.HobbyModel

class HobbyAdapter(var hobbies: List<HobbyModel>) : RecyclerView.Adapter<HobbyAdapter.HobbyViewHolder>() {

    // ViewHolder für die einzelnen Listenelemente
    inner class HobbyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.mTitle)
        val numberOfPlayersTextView: TextView = itemView.findViewById(R.id.subTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HobbyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_hobbies, parent, false)
        return HobbyViewHolder(view)
    }

    override fun onBindViewHolder(holder: HobbyViewHolder, position: Int) {
        val hobby = hobbies[position]
        holder.titleTextView.text = hobby.title
        holder.numberOfPlayersTextView.text = "Players: ${hobby.number}"
    }

    override fun getItemCount(): Int = hobbies.size
}
