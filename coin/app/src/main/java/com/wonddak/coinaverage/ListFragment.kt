package com.wonddak.coinaverage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.wonddak.coinaverage.databinding.FragmentListBinding
import com.wonddak.coinaverage.room.AppDatabase

class ListFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private var adapter: ListRecyclerAdaper? = null
    private lateinit var binding: FragmentListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)

        val db = AppDatabase.getInstance(requireContext())
        mainActivity!!.binding.mainTitle.text ="내 코인 리스트"

        db.dbDao().getCoinInfoLiveData().observe(this, Observer {
            adapter = ListRecyclerAdaper(it, requireContext(),requireFragmentManager(),mainActivity!!)
            binding.listRecylcer.adapter = adapter
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