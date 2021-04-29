package com.wonddak.coinaverage

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonddak.coinaverage.databinding.FragmentMainBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private var adapter: coinRecylcerAdapter? = null
    private lateinit var binding: FragmentMainBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val prefs: SharedPreferences = requireContext().getSharedPreferences(
            "coindata",
            Context.MODE_PRIVATE
        )
        val db = AppDatabase.getInstance(requireContext())
        val iddata = prefs.getInt("iddata", 1)
        val format = prefs.getString("dec", "#,###.00")

        val dec = DecimalFormat(format)

        binding.coinrecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )

        var sum: Float
        var count: Float

        GlobalScope.launch(Dispatchers.IO) {
            var name = db.dbDao().getCoinInfoNameById(iddata)
            launch(Dispatchers.Main) {
                mainActivity!!.binding.mainTitle.text = name
            }
        }

        db.dbDao().getCoinDetailById(iddata).observe(this, Observer {

            adapter = coinRecylcerAdapter(requireContext(), it, dec, mainActivity!!, prefs)
            binding.coinrecycler.adapter = adapter

            sum = 0.0F
            count = 0.0F


            for (i in it.indices) {
                sum += (it[i].coinPrice * it[i].coinCount)
                count += it[i].coinCount
            }

            var avg = sum / count
            if (sum != 0.0F) {
                binding.totalprice.text = dec.format(sum).toString() + "원"
            } else {
                binding.totalprice.text = 0.toString() + "원"
            }
            binding.totalcount.text = count.toString() +"개"

            if (count != 0.0F) {
                binding.totalavg.text = dec.format(avg).toString() + "원"
            } else {
                binding.totalavg.text = 0.toString() + "원"

            }
        })


        //추가 버튼 클릭시
        binding.addbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                mainActivity!!.selectedPostion = -1
                mainActivity!!.priceorcount = false
            }
        }


        //초기화 버튼 클릭시
        binding.cleatbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.dbDao().clearCoinDetail(iddata)
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                mainActivity!!.selectedPostion = -1
                mainActivity!!.priceorcount = false
            }
        }



        return binding.root
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainActivity = activity as MainActivity

    }

    override fun onDetach() {
        super.onDetach()
        mainActivity = null
    }
}