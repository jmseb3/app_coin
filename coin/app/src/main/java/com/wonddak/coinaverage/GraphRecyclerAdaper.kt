package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.databinding.ItemCoinListBinding
import com.wonddak.coinaverage.databinding.ItemGraphBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat

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