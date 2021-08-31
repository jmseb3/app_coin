package com.wonddak.coinaverage.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.databinding.ItemGraphBinding

class GraphRecyclerAdaper(
    val itemList: List<String>
) : RecyclerView.Adapter<GraphRecyclerAdaper.ViewHolder>() {
    inner class ViewHolder(binding: ItemGraphBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemName = binding.itemText
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemGraphBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = itemList[position]

    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}