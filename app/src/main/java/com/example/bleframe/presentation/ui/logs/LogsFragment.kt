package com.example.bleframe.presentation.ui.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bleframe.R
import com.example.bleframe.databinding.FragmentAppBinding
import com.example.bleframe.presentation.adapters.AdapterRV
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogsFragment : Fragment() {

    private val viewModel by viewModels<LogsVM>()
    private var adapterRV: AdapterRV? = null
    private var _binding: FragmentAppBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAppBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        setContent()
        initAdapterRv()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        adapterRV = null
        _binding = null
    }

    private fun setContent(){
        binding.apply {
            fragmentDeviceLottie.visibility = View.GONE
            fragmentDeviceButton.visibility =View.GONE
            }
        }

    private fun initAdapterRv(){
        binding.fragmentDeviceRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = adapterRV

        }
    }



}