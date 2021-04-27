package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.databinding.ItemCoinListBinding
import com.wonddak.coinaverage.room.CoinInfo

class ListRecyclerAdaper(
    val itemlist: List<CoinInfo>,
    val context: Context
) : RecyclerView.Adapter<ListRecyclerAdaper.ViewHolder>() {

    inner class ViewHolder(binding: ItemCoinListBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemName = binding.itemText

        val prefs: SharedPreferences = context.getSharedPreferences(
            "coindata",
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = itemlist[position].coinName
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}