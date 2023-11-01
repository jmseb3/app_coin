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

class GraphFragment : Fragment() {
    private var mainActivity: MainActivity? = null
    private lateinit var binding: FragmentGraphBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGraphBinding.inflate(inflater, container, false)
        mainActivity!!.binding.mainTitle.text = "손절% 계산기"
        val dec = DecimalFormat("###.00")
        binding.graphView.setContent {
            GraphView()
        }


//        inputbox.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//
//            }
//
//            override fun afterTextChanged(s: Editable?) {
//                try {
//                    when {
//
//                        s.toString().isEmpty() -> {
//                            binding.output.setText("")
//                        }
//                        s.toString().toFloat() > 100 -> {
//                            Toast.makeText(mainActivity!!, "손실은 100%보다 클수 없습니다", Toast.LENGTH_SHORT)
//                                .show()
//                            binding.input.setText("")
//                        }
//                        else -> {
//                            val minusPer = s.toString().toFloat() / 100
//                            val data = dec.format((minusPer / (1 - minusPer)) * 100)
//                            binding.output.setText(data.toString())
//
//
//                        }
//                    }
//
//                } catch (e: NumberFormatException) {
//                    inputbox.setText("")
//                    inputbox.setSelection(inputbox.length())
//                    Toast.makeText(mainActivity!!, "숫자만 입력해주세요", Toast.LENGTH_SHORT).show()
//                }
//            }
//
//
//        }
//        )

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