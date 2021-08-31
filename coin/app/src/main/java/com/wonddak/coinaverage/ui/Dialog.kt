package com.wonddak.coinaverage.ui

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.databinding.DialogDeleteBinding
import com.wonddak.coinaverage.databinding.DialogNewGameBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.room.CoinInfo
import com.wonddak.coinaverage.ui.fragment.MainFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Dialog(
    context: Context,
    val activity: MainActivity,
    val fragmentManager: FragmentManager
) {
    val dialog = Dialog(context)
    val params = dialog.window!!.attributes

    val prefs: SharedPreferences = context.getSharedPreferences("coindata", Context.MODE_PRIVATE)
    val editor = prefs.edit()
    val db = AppDatabase.getInstance(context)


    fun newGameStart(type: Int, iddata: Int?) {
        val context: Context = this.dialog.context
        val binding = DialogNewGameBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)
        if (type == 1) {
            binding.mtdialogtitle.text = "새 코인 추가"
        } else if (type == 2) {

            GlobalScope.launch(Dispatchers.IO) {
                var name = db.dbDao().getCoinInfoNameById(iddata!!)
                launch(Dispatchers.Main) {
                    binding.mtdialogtitle.text = "수정할 이름을 설정해주세요"
                    binding.newCoinName.setText(name)
                    binding.ok.text = "수정"
                }
            }

        }

        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        dialog.show()

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.ok.setOnClickListener {
            var text_temp = binding.newCoinName.text
            if (text_temp.isBlank() || text_temp.isEmpty()) {
                Toast.makeText(context, "이름을 입력해 주세요", Toast.LENGTH_SHORT).show()
            } else {
                if (type == 1) {
                    GlobalScope.launch(Dispatchers.IO) {
                        var id = db.dbDao().insertCoinInfoData(CoinInfo(null, text_temp.toString()))
                        db.dbDao().insertCoinDetailData(CoinDetail(null, id.toInt()))
                        db.dbDao().insertCoinDetailData(CoinDetail(null, id.toInt()))
                        editor.putInt("iddata", id.toInt())
                        editor.commit()
                        launch(Dispatchers.Main) {
                            fragmentManager
                                .beginTransaction()
                                .replace(R.id.main_frag_area, MainFragment())
                                .commit()
                            dialog.dismiss()
                        }


                    }
                } else if (type == 2) {
                    GlobalScope.launch(Dispatchers.IO) {
                        db.dbDao().updateCoinInfoName(iddata!!, text_temp.toString())
                    }
                    dialog.dismiss()
                }
            }

        }
    }

    fun delete(iddata: Int?) {
        val context: Context = this.dialog.context
        val binding = DialogDeleteBinding.inflate(LayoutInflater.from(context))

        dialog.setContentView(binding.root)
        params.width = ViewGroup.LayoutParams.MATCH_PARENT
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT

        dialog.window!!.attributes = params as android.view.WindowManager.LayoutParams
        dialog.show()

        binding.cancel.setOnClickListener {
            dialog.dismiss()
        }

        binding.ok.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.dbDao().deleteCoinInfolById(iddata!!)
            }
            dialog.dismiss()
        }
    }
}