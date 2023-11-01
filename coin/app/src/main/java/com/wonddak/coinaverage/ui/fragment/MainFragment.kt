package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.preference.Preference
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.wonddak.coinaverage.coinRecylcerAdapter
import com.wonddak.coinaverage.databinding.FragmentMainBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.room.CoinDetail
import com.wonddak.coinaverage.ui.MainActivity
import com.wonddak.coinaverage.util.Config
import com.wonddak.coinaverage.viewmodel.CoinViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.DecimalFormat

class MainFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private var adapter: coinRecylcerAdapter? = null
    private lateinit var binding: FragmentMainBinding
    private val config by lazy { Config.getInstance(requireContext()) }
    private val viewModel :CoinViewModel  by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)

        val prefs = config.prefs
        val db = AppDatabase.getInstance(requireContext())

        val iddata = config.getIdData()
        val format = config.getDec()
        val dec = DecimalFormat(format)

        binding.coinrecycler.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                LinearLayoutManager.VERTICAL
            )
        )
        var sum: Float
        var count: Float
        lifecycleScope.launch {
            viewModel.nowInfo.collect {
                Log.d("data",it.toString())
            }
        }

        GlobalScope.launch(Dispatchers.IO) {
            var name = db.dbDao().getCoinInfoNameById(iddata)
            launch(Dispatchers.Main) {
                try {
                    mainActivity!!.binding.mainTitle.text = name

                } catch (e: NullPointerException) {

                }

            }
        }
        db.dbDao().getCoinDetailById(iddata).observe(viewLifecycleOwner, Observer {

            sum = 0.0f
            count = 0.0f
            val next_check = config.getNext()
            binding.coinrecycler.scrollToPosition(it.size - 1)
            binding.coinrecycler.scrollToPosition(0)
            if (!next_check) {
                mainActivity!!.nowPosition = -1
                mainActivity!!.priceOrCount = false
            }

            adapter = coinRecylcerAdapter(mainActivity!!, it, dec, mainActivity!!, prefs)
            binding.coinrecycler.adapter = adapter
            val checkpos = mainActivity!!.nowPosition
            if (checkpos != -1) {
                binding.coinrecycler.scrollToPosition(checkpos)
            }
            for (i in it.indices) {
                sum += (it[i].coinPrice * it[i].coinCount)
                count += it[i].coinCount
            }

            var avg = sum / count
            if (sum != 0.0F) {
                binding.totalprice.text = WonText(sum)
            } else {
                binding.totalprice.text = WonText(0.0f)
            }
            binding.totalcount.text = count.toString() + "개"

            if (count != 0.0F) {
                binding.totalavg.text = WonText(avg)
            } else {
                binding.totalavg.text = WonText(0.0f)

            }
        })


        //추가 버튼 클릭시
        binding.addbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                mainActivity!!.nowPosition = -1
                mainActivity!!.priceOrCount = false
            }
        }


        //초기화 버튼 클릭시
        binding.cleatbtn.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                db.dbDao().clearCoinDetail(iddata)
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                db.dbDao().insertCoinDetailData(CoinDetail(null, iddata))
                mainActivity!!.nowPosition = -1
                mainActivity!!.priceOrCount = false
            }
        }



        return binding.root
    }

    fun WonText(value: Float): String {
        val dec = DecimalFormat(config.getDec())
        return dec.format(value).toString() + "원"
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