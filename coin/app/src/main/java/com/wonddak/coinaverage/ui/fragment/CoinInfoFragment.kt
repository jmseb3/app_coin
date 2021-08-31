package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonddak.coinaverage.API.upBitClient
import com.wonddak.coinaverage.API.upbitArr
import com.wonddak.coinaverage.R
import com.wonddak.coinaverage.adapter.InfoRecyclerAdaper
import com.wonddak.coinaverage.databinding.FragmentCoinInfoBinding
import com.wonddak.coinaverage.databinding.FragmentGraphBinding
import com.wonddak.coinaverage.ui.MainActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CoinInfoFragment : Fragment() {

    private var mainActivity: MainActivity? = null
    private lateinit var binding: FragmentCoinInfoBinding
    var infoadpaters: InfoRecyclerAdaper? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoinInfoBinding.inflate(inflater, container, false)

        mainActivity!!.binding.mainTitle.text = "실시간 코인 정보"
        upBitClient.api.getlist().enqueue(object : Callback<upbitArr> {

            override fun onResponse(
                call: Call<upbitArr>,
                response: Response<upbitArr>
            ) {
                if (response.isSuccessful) {
                    infoadpaters = InfoRecyclerAdaper(response.body()!!, mainActivity!!)
                    binding.recyclerTest.adapter = infoadpaters
                    binding.searchBox.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            s: CharSequence?,
                            start: Int,
                            count: Int,
                            after: Int
                        ) {

                        }

                        override fun onTextChanged(
                            s: CharSequence?,
                            start: Int,
                            before: Int,
                            count: Int
                        ) {
                            infoadpaters!!.filter.filter(s)
                        }

                        override fun afterTextChanged(s: Editable?) {

                        }
                    })
                }

            }

            override fun onFailure(call: Call<upbitArr>, t: Throwable) {

            }
        })


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