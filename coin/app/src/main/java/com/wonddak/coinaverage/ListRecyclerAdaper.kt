package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.databinding.ItemCoinListBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IndexOutOfBoundsException
import java.text.DecimalFormat

class ListRecyclerAdaper(
    val itemlist: List<CoinInfo>,
    val context: Context,
    val fragmentManager: FragmentManager,
    val activity: MainActivity
) : RecyclerView.Adapter<ListRecyclerAdaper.ViewHolder>() {

    inner class ViewHolder(binding: ItemCoinListBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemName = binding.itemText
        val itemprice = binding.itemPrice
        val itemcount = binding.itemCount

        val prefs: SharedPreferences = context.getSharedPreferences(
            "coindata",
            Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        val db = AppDatabase.getInstance(context)

        init {

            binding.clearItem.setOnClickListener {
                if(itemlist.size <=1){
                    Toast.makeText(context,"최소 하나는 존재해야 합니다.",Toast.LENGTH_SHORT).show()
                }else{
                    Dialog(context,activity,fragmentManager).delete(itemlist[layoutPosition].coinId)
                    try {
                        editor.putInt("iddata",itemlist[layoutPosition-1].coinId!!)
                        editor.commit()
                    } catch (e:IndexOutOfBoundsException){
                        editor.putInt("iddata",itemlist[layoutPosition+1].coinId!!)
                        editor.commit()
                    }
                }

            }
            binding.itemListAll.setOnClickListener {
                changedata(db, layoutPosition, editor)
            }
            itemName.setOnClickListener {
                changedata(db, layoutPosition, editor)
            }

            binding.itemListAll.setOnLongClickListener {
                changename(db, layoutPosition)
                return@setOnLongClickListener true
            }

            itemName.setOnLongClickListener {
                changename(db, layoutPosition)
                return@setOnLongClickListener true
            }
        }
    }

    fun changedata(db: AppDatabase, layoutPosition: Int, editor: SharedPreferences.Editor) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = db.dbDao().getCoinInfoIdByName(itemlist[layoutPosition].coinName)
            editor.putInt("iddata", id)
            editor.commit()
            launch(Dispatchers.Main) {
                fragmentManager
                    .beginTransaction()
                    .replace(R.id.main_frag_area, MainFragment())
                    .commit()
            }
        }
    }

    fun changename(db: AppDatabase, layoutPosition: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            var id = db.dbDao().getCoinInfoIdByName(itemlist[layoutPosition].coinName)
            launch(Dispatchers.Main) {
                Dialog(context, activity, fragmentManager).newGameStart(2, id)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCoinListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemName.text = itemlist[position].coinName
        val prefs: SharedPreferences = context.getSharedPreferences(
            "coindata",
            Context.MODE_PRIVATE
        )
        val format = prefs.getString("dec", "#,###.00")
        val dec = DecimalFormat(format)
        val db = AppDatabase.getInstance(context)

        GlobalScope.launch(Dispatchers.IO) {
            var priceList = db.dbDao().getCoinDetailPriceById(itemlist[position].coinId!!)
            var countList = db.dbDao().getCoinDetailCountById(itemlist[position].coinId!!)
            var sum = 0.0
            for (i in priceList.indices) {
                sum += priceList[i] * countList[i]
            }

            launch(Dispatchers.Main) {
                holder.itemprice.text = dec.format(sum).toString() + "원"
                holder.itemcount.text = dec.format(countList.sum()).toString() + "개"
            }
        }
    }

    override fun getItemCount(): Int {
        return itemlist.size
    }
}