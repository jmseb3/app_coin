package com.wonddak.coinaverage.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonddak.coinaverage.databinding.FragmentComposeBinding
import com.wonddak.coinaverage.ui.view.GraphView
import com.wonddak.coinaverage.ui.view.SettingView

abstract class BaseFragmentForCompose() : BaseFragment() {
    protected lateinit var binding: FragmentComposeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComposeBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    abstract fun init()
}

class GraphFragment : BaseFragmentForCompose() {
    override fun init() {
        setTitle("손절% 계산기")
        binding.composeView.setContent {
            GraphView()
        }
    }
}

class SettingFragment : BaseFragmentForCompose() {
    override fun init() {
        setTitle("설정")
        binding.composeView.setContent {
            SettingView(viewModel = viewModel)
        }
    }
}