package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.wonddak.coinaverage.ui.MainActivity
import com.wonddak.coinaverage.viewmodel.CoinViewModel

abstract class BaseFragment() :Fragment() {
    protected var mainActivity: MainActivity? = null

    protected val viewModel by activityViewModels<CoinViewModel>()

    fun setTitle(title:String) {
        mainActivity?.setTitle(title)
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