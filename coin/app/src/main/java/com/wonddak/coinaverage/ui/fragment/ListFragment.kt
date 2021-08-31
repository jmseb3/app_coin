package com.wonddak.coinaverage.ui.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.play.core.review.ReviewManagerFactory
import com.wonddak.coinaverage.adapter.ListRecyclerAdaper
import com.wonddak.coinaverage.databinding.FragmentListBinding
import com.wonddak.coinaverage.room.AppDatabase
import com.wonddak.coinaverage.ui.MainActivity
import java.lang.NullPointerException

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
        mainActivity!!.binding.mainTitle.text = "내 코인 리스트"

        val manager = ReviewManagerFactory.create(mainActivity!!)
        val request = manager.requestReviewFlow()

        request.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val reviewInfo = task.result
                try {
                    val flow = manager.launchReviewFlow(mainActivity!!, reviewInfo)
                    flow.addOnCompleteListener { _ ->
                        // The flow has finished. The API does not indicate whether the user
                        // reviewed or not, or even whether the review dialog was shown. Thus, no
                        // matter the result, we continue our app flow.
                    }
                } catch (e:NullPointerException){

                }
            } else {
                // There was some problem, log or handle the error code.
//                    @ReviewErrorCode val reviewErrorCode = (task.getException() as TaskException).errorCode
            }
        }


        db.dbDao().getCoinInfoLiveData().observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            adapter =
                ListRecyclerAdaper(it, requireContext(), parentFragmentManager, mainActivity!!)
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