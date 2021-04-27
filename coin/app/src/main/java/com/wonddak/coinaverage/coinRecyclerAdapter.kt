package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Query
import com.wonddak.coinaverage.databinding.ActivityMainBinding
import com.wonddak.coinaverage.databinding.ItemlistBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class coinRecylcerAdapter(
    val context: Context,
    var itemInfo: List<CoinDetail>,
    val dec: DecimalFormat,
    val activity: MainActivity,
    val prefs: SharedPreferences
) : RecyclerView.Adapter<coinRecylcerAdapter.ViewHolder>() {

    inner class ViewHolder(binding: ItemlistBinding) : RecyclerView.ViewHolder(binding.root) {
        val itemcount: EditText = binding.itemcounts
        val itemprice: EditText = binding.itemprices
        val itemsum: TextView = binding.itemsums
        val itemrank: TextView = binding.itemrank

        val db = AppDatabase.getInstance(context)
        val iddata = prefs.getInt("iddata", 1)


        init {
            // 합계 클릭시 개별 초기화
            binding.itemtotalprice.setOnClickListener {
                Toast.makeText(
                    context,
                    (layoutPosition + 1).toString() + "번 항목을 초기화 했습니다.",
                    Toast.LENGTH_SHORT
                ).show()
                itemcount.setText("0.0")
                itemprice.setText("0.0")
                itemprice.requestFocus(itemprice.length())
            }

            binding.itemtotalprice.setOnLongClickListener {
                if (itemInfo.size == 1) {
                    Toast.makeText(context, "최소 하나는 존재해야합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Deleteitem(db, iddata, layoutPosition)
                }
                return@setOnLongClickListener true
            }

            binding.itemAll.setOnLongClickListener {
                if (itemInfo.size == 1) {
                    Toast.makeText(context, "최소 하나는 존재해야합니다.", Toast.LENGTH_SHORT).show()
                } else {
                    Deleteitem(db, iddata, layoutPosition)
                }
                return@setOnLongClickListener true
            }


            itemcount.doOnTextChanged { text, _, _, _ ->
                if (text.toString() != "") {
                    try {
                        val temp1 = text.toString().toFloat()
                        val temp2 = itemprice.text.toString().toFloat()
                        val result = temp1 * temp2
                        if (result.toInt() == 0) {
                            itemsum.text = 0.toString()
                        } else {
                            itemsum.text = dec.format(temp1 * temp2).toString()
                        }
                    } catch (e: NumberFormatException) {
                        itemcount.setText("")
                        itemcount.setSelection(itemcount.length())
                        Toast.makeText(context, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }

            }
            itemprice.doOnTextChanged { text, _, _, _ ->
                if (text.toString() != "") {
                    try {
                        val temp1 = text.toString().toFloat()
                        val temp2 = itemcount.text.toString().toFloat()
                        val result = temp1 * temp2
                        if (result.toInt() == 0) {
                            itemsum.text = 0.toString()
                        } else {
                            itemsum.text = dec.format(temp1 * temp2).toString()
                        }
                    } catch (e: NumberFormatException) {
                        itemprice.setText("")
                        itemprice.setSelection(itemprice.length())
                        Toast.makeText(context, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun Deleteitem(db: AppDatabase, iddata: Int, layoutPosition: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            val data = db.dbDao().getCoinDetailId(iddata)
            db.dbDao().deleteCoinDetailById(data[layoutPosition])
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemcount.setText(itemInfo[position].coinCount.toString())
        holder.itemprice.setText(itemInfo[position].coinPrice.toString())
        holder.itemrank.text = "#" + (position + 1)

        val db = AppDatabase.getInstance(context)
        val iddata = prefs.getInt("iddata", 1)


        holder.itemcount.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                if (position == itemInfo.size - 1) {
                    activity.hideKeyboard(holder.itemcount)
                }
                activity.selectedPostion = position + 1
                activity.priceorcount = false
                holder.itemcount.clearFocus()
                GlobalScope.launch(Dispatchers.IO) {
                    var data = db.dbDao().getCoinDetailId(iddata)[position]
                    db.dbDao().updateCoinDetailByCoinitemIdCount(
                        data,
                        holder.itemcount.text.toString().toFloat()
                    )
                }
            }
            return@setOnEditorActionListener true
        }

        holder.itemprice.setOnEditorActionListener {_, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                activity.selectedPostion = position
                activity.priceorcount = true
                holder.itemprice.clearFocus()
                GlobalScope.launch(Dispatchers.IO) {
                    var data = db.dbDao().getCoinDetailId(iddata)[position]
                    db.dbDao().updateCoinDetailByCoinitemIdPrice(
                        data,
                        holder.itemprice.text.toString().toFloat()
                    )
                }
            }
            return@setOnEditorActionListener true
        }


        if (activity.selectedPostion == position) {
            if (!activity.priceorcount) {
                holder.itemprice.requestFocus()
                activity.showKeyboard()
            } else {
                holder.itemcount.requestFocus()
                activity.showKeyboard()
            }
        }


    }


    override fun getItemCount(): Int {
        return itemInfo.size
    }

}
