package com.wonddak.coinaverage.adapter

import android.content.Context

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.API.DetailArr
import com.wonddak.coinaverage.API.upBitClient
import com.wonddak.coinaverage.API.upbitList
import com.wonddak.coinaverage.databinding.ItemCoinInfoBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat

class InfoRecyclerAdaper(
    val itemlist: ArrayList<upbitList>,
    val context: Context,
    val ck_krw: CheckBox,
    val ck_btc: CheckBox,
    val ck_usd: CheckBox
) : RecyclerView.Adapter<InfoRecyclerAdaper.ViewHolder>(), Filterable {

    var unFilteredlist: ArrayList<upbitList>? = null
    var filteredList: ArrayList<upbitList>? = null

    init {
        this.filteredList = this.itemlist
        this.unFilteredlist = this.itemlist
    }

    inner class ViewHolder(binding: ItemCoinInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        val kor_name = binding.korName
        val eng_name = binding.engName
        val market = binding.marketInfo
        val now_price = binding.nowPrice

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCoinInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val myFormatter = DecimalFormat("###,###.##")
        val myFormatter2 = DecimalFormat("0.#########")
        holder.kor_name.text = filteredList!![position].korean_name
        holder.eng_name.text = filteredList!![position].english_name
        holder.market.text = filteredList!![position].market
        upBitClient.api.getinfo(filteredList!![position].market)
            .enqueue(object : Callback<DetailArr> {
                override fun onResponse(
                    call: Call<DetailArr>,
                    response: Response<DetailArr>
                ) {
                    if (response.isSuccessful) {
                        val temp = response!!.body()!![0]
                        when {
                            temp.market.slice(IntRange(0, 3)) == "BTC-" -> {
                                holder.now_price.text =
                                    myFormatter2.format(temp.trade_price).toString() + " BTC"
                            }
                            temp.market.slice(IntRange(0, 3)) == "KRW-" -> {
                                holder.now_price.text =
                                    myFormatter.format(temp.trade_price).toString() + " KRW"
                            }
                            else -> {
                                holder.now_price.text =
                                    myFormatter.format(temp.trade_price).toString() + " USD"
                            }
                        }

                    }

                }

                override fun onFailure(call: Call<DetailArr>, t: Throwable) {

                }
            })


    }

    override fun getItemCount(): Int {
        return filteredList!!.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty() ) {
                    filteredList = itemlist
                } else {
                    val filteringList: ArrayList<upbitList> = ArrayList()
                    for (name in unFilteredlist!!) {
                        if (
                            (name.korean_name.toLowerCase().contains(charString.toLowerCase())
                                    or
                                    name.english_name.toLowerCase()
                                        .contains(charString.toLowerCase()))
                        ) {
                            val temp = name.market.slice(IntRange(0, 2))
                            if ((ck_krw.isChecked and (temp == "KRW")) or (ck_btc.isChecked and (temp == "BTC")) or (ck_usd.isChecked and (temp == "USD"))) {
                                filteringList.add(name)
                            }

                        }
                    }
                    filteredList = filteringList
                }
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence, results: FilterResults) {
                filteredList = results.values as ArrayList<upbitList>
                notifyDataSetChanged()
            }
        }
    }
}