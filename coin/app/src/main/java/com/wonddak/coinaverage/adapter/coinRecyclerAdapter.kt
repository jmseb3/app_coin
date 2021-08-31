package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.wonddak.coinaverage.databinding.ItemlistBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import kotlin.IndexOutOfBoundsException

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
                itemcount.setText((0.0).toString())
                itemprice.setText((0.0).toString())
                GlobalScope.launch(Dispatchers.IO) {
                    db.dbDao()
                        .updateCoinDetailByCoinitemId(itemInfo[layoutPosition].id!!, 0.0F, 0.0F)

                }
                itemprice.requestFocus(itemprice.length())

            }

            binding.itemtotalprice.setOnLongClickListener {
                nofiyDelete(db, iddata, layoutPosition)
                return@setOnLongClickListener true
            }

            binding.itemAll.setOnLongClickListener {
                nofiyDelete(db, iddata, layoutPosition)
                return@setOnLongClickListener true
            }
        }
    }

    fun nofiyDelete(db: AppDatabase, iddata: Int, layoutPosition: Int) {
        if (itemInfo.size == 1) {
            Toast.makeText(context, "최소 하나는 존재해야합니다.", Toast.LENGTH_SHORT).show()
        } else {
            GlobalScope.launch(Dispatchers.IO) {
                val data = db.dbDao().getCoinDetailId(iddata)
                db.dbDao().deleteCoinDetailById(data[layoutPosition])
            }
            notifyDataSetChanged()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemcount.setText(itemInfo[position].coinCount.toString())
        holder.itemprice.setText(itemInfo[position].coinPrice.toString())
        holder.itemsum.text =
            (dec.format(itemInfo[position].coinCount * itemInfo[position].coinPrice)).toString()
        holder.itemrank.text = "#" + (position + 1)

        val db = AppDatabase.getInstance(context)
        val iddata = prefs.getInt("iddata", 1)
        val next_check = prefs.getBoolean("next", false)

        if (position == activity.nowPosition) {
            if (activity.priceOrCount) {
                holder.itemcount.requestFocus()
                activity.showKeyboard()
            } else {
                holder.itemprice.requestFocus()
                activity.showKeyboard()
            }
        }


        holder.itemprice.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {


            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        var data = db.dbDao().getCoinDetailId(iddata)[position]
                        val temp = holder.itemprice.text.toString().toFloat()
                        db.dbDao().updateCoinDetailByCoinitemIdPrice(
                            data,
                            temp
                        )
                    } catch (e: java.lang.NumberFormatException) {

                    } catch (e2: IndexOutOfBoundsException) {

                    }
                }
            }

        }

        holder.itemcount.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {


            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    try {
                        var data = db.dbDao().getCoinDetailId(iddata)[position]
                        val temp = holder.itemcount.text.toString().toFloat()
                        db.dbDao().updateCoinDetailByCoinitemIdCount(
                            data,
                            temp
                        )
                    } catch (e: java.lang.NumberFormatException) {

                    } catch (e2: IndexOutOfBoundsException) {

                    }
                }
            }
        }

        holder.itemprice.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.toString() != "") {
                    try {
                        val temp1 = text.toString().toFloat()
                        val temp2 = holder.itemcount.text.toString().toFloat()
                        val result = temp1 * temp2
                        if (result.toInt() == 0) {
                            holder.itemsum.text = dec.format(0).toString()
                        } else {
                            holder.itemsum.text = dec.format(temp1 * temp2).toString()
                        }

                    } catch (e: NumberFormatException) {
                        holder.itemprice.setText("")
                        holder.itemprice.setSelection(holder.itemprice.length())
                        Toast.makeText(context, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun afterTextChanged(text: Editable?) {
                activity.nowPosition = position
                activity.priceOrCount = true

            }
        })

        holder.itemcount.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(text: CharSequence?, start: Int, before: Int, count: Int) {
                if (text.toString() != "") {
                    try {
                        val temp1 = text.toString().toFloat()
                        val temp2 = holder.itemprice.text.toString().toFloat()
                        val result = temp1 * temp2
                        if (result.toInt() == 0) {
                            holder.itemsum.text = dec.format(0).toString()
                        } else {
                            holder.itemsum.text = dec.format(temp1 * temp2).toString()
                        }

                    } catch (e: NumberFormatException) {
                        holder.itemcount.setText("")
                        holder.itemcount.setSelection(holder.itemcount.length())
                        Toast.makeText(context, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun afterTextChanged(text: Editable?) {
                activity.nowPosition = position + 1
                activity.priceOrCount = false

            }
        })


        if (position == itemInfo.size - 1) {
            holder.itemcount.imeOptions = EditorInfo.IME_ACTION_DONE
        } else {
            holder.itemcount.imeOptions = EditorInfo.IME_ACTION_NEXT
        }

        holder.itemcount.setOnEditorActionListener { v, actionId, event ->
            try {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    holder.itemcount.clearFocus()
                    activity.nowPosition = -1
                    activity.priceOrCount = false
                    return@setOnEditorActionListener true
                }
                return@setOnEditorActionListener false
            }finally {
                activity.hideKeyboard(holder.itemcount)
                if(!next_check){
                    holder.itemcount.clearFocus()
                }
            }


        }

        holder.itemprice.setOnEditorActionListener { v, actionId, event ->
            activity.hideKeyboard(holder.itemprice)
            if(!next_check){
                holder.itemprice.clearFocus()
            }
            return@setOnEditorActionListener false

        }



    }



    override fun getItemCount(): Int {
        return itemInfo.size
    }

}
