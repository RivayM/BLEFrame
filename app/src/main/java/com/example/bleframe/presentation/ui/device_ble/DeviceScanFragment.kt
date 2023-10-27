package com.example.bleframe.presentation.ui.device_ble

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.BLEFrame.databinding.FragmentSampleContentBinding
import com.example.bleframe.presentation.adapters.AdapterRV
import com.example.bleframe.presentation.adapters.FactoryViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DeviceScanFragment : Fragment() {

    @Inject
    lateinit var factory: DeviceScanViewModel.Factory
    private val viewModel: DeviceScanViewModel by viewModels {
        FactoryViewModel(this){ factory.build() }
    }

    private lateinit var adapterRV: AdapterRV // адаптер RV

    private var _binding: FragmentSampleContentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSampleContentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}