package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wonddak.coinaverage.databinding.FragmentGraphBinding
import com.wonddak.coinaverage.ui.MainActivity
import java.text.DecimalFormat

class GraphFragment : BaseFragment() {
    private lateinit var binding: FragmentGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        setTitle("손절% 계산기")
        binding.graphView.setContent {
            GraphView()
        }
        return binding.root
    }
}