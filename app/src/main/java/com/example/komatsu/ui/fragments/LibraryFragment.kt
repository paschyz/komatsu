package com.example.komatsu.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.komatsu.databinding.FragmentExploreLibraryBinding
import com.example.komatsu.ui.viewmodel.ExploreLibraryViewModel

class LibraryFragment : Fragment() {
    private var _binding: FragmentExploreLibraryBinding? = null

    private val binding get() = _binding!!

    private val viewModel: ExploreLibraryViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentExploreLibraryBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}