package com.example.bleframe.presentation.ui.logs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.bleframe.databinding.FragmentSampleContentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LogsFragment : Fragment() {

    private val viewModel by viewModels<LogsViewModel>()

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