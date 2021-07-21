package com.annis.mydemos.ui.main.uiTest

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.annis.mydemos.databinding.FragmentUiTestBinding
import com.annis.mydemos.ui.main.CarNumberSelectorActivity
import com.annis.mydemos.ui.ui.RefreshAndLoadActivity

class UITestFragment : Fragment() {

    private lateinit var dashboardViewModel: UITestViewModel
    private var _binding: FragmentUiTestBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProvider(this).get(UITestViewModel::class.java)

        _binding = FragmentUiTestBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textDashboard
        binding.textDashboard.setOnClickListener {
            startActivity(Intent(activity, CarNumberSelectorActivity::class.java))
        }
        binding.btRefreshAndLoad.setOnClickListener {
            startActivity(Intent(activity, RefreshAndLoadActivity::class.java))
        }
        dashboardViewModel.text.observe(viewLifecycleOwner, Observer {

        })
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}