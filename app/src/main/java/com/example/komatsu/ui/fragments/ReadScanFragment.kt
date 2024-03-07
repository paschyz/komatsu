package com.example.komatsu.ui.fragments
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.komatsu.R
import com.example.komatsu.databinding.FragmentReadScanBinding
import com.example.komatsu.ui.view.adapters.ScanPagerAdapter

import com.example.komatsu.ui.viewmodel.ReadScansViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReadScanFragment : Fragment() {

    private var _binding: FragmentReadScanBinding? = null
    private val binding get() = _binding!!

    private val scanImages = listOf(R.drawable.jjk_cover_1, R.drawable.jjk_cover_1, R.drawable.jjk_cover_1) // Example list of scan images

    private val viewModel: ReadScansViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReadScanBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val viewPager: ViewPager2 = binding.viewPager
        val adapter = ScanPagerAdapter(scanImages)
        viewPager.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}