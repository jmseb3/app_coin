//package com.wonddak.coinaverage.ui.fragment
//
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.lifecycle.lifecycleScope
//import com.google.android.play.core.review.ReviewManagerFactory
//import com.wonddak.coinaverage.databinding.FragmentComposeBinding
//import com.wonddak.coinaverage.ui.view.CoinListView
//import com.wonddak.coinaverage.ui.view.GraphView
//import com.wonddak.coinaverage.ui.view.MainView
//import com.wonddak.coinaverage.ui.view.SettingView
//import kotlinx.coroutines.launch
//
//abstract class BaseFragmentForCompose() : BaseFragment() {
//    protected lateinit var binding: FragmentComposeBinding
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentComposeBinding.inflate(inflater, container, false)
//        init()
//        return binding.root
//    }
//
//    abstract fun init()
//}
//
//class GraphFragment : BaseFragmentForCompose() {
//    override fun init() {
//        setTitle("손절% 계산기")
//        binding.composeView.setContent {
//            GraphView()
//        }
//    }
//}
//
//class SettingFragment : BaseFragmentForCompose() {
//    override fun init() {
//        setTitle("설정")
//        binding.composeView.setContent {
//            SettingView(viewModel = viewModel)
//        }
//    }
//}
//
//class ListFragment : BaseFragmentForCompose() {
//    override fun init() {
//        setTitle("내 코인 리스트")
//        val manager = ReviewManagerFactory.create(mainActivity!!)
//        val request = manager.requestReviewFlow()
//        request.addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val reviewInfo = task.result
//                try {
//                    val flow = manager.launchReviewFlow(mainActivity!!, reviewInfo)
//                    flow.addOnCompleteListener { _ ->
//                        // The flow has finished. The API does not indicate whether the user
//                        // reviewed or not, or even whether the review dialog was shown. Thus, no
//                        // matter the result, we continue our app flow.
//                    }
//                } catch (e: NullPointerException) {
//
//                }
//            } else {
//                // There was some problem, log or handle the error code.
////                    @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
//            }
//        }
//        binding.composeView.setContent {
//            CoinListView(viewModel = viewModel)
//        }
//    }
//}
//
//class MainFragment : BaseFragmentForCompose() {
//    override fun init() {
//        lifecycleScope.launch {
//            viewModel.nowInfo.collect {
//                setTitle(it?.coinInfo?.coinName ?: "코인 평단 계산기")
//            }
//        }
//        binding.composeView.setContent {
//            MainView(viewModel = viewModel)
//        }
//    }
//}