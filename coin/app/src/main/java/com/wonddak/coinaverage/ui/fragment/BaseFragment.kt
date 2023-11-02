package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import androidx.fragment.app.Fragment
import com.wonddak.coinaverage.ui.MainActivity

abstract class BaseFragment() :Fragment() {
    private var mainActivity: MainActivity? = null

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