package com.example.komatsu.ui.view.activities


import android.os.Bundle
import com.google.android.material.tabs.TabLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.komatsu.ui.view.adapters.SectionsPagerAdapter
import com.example.komatsu.databinding.ActivityExploreBinding
import com.example.komatsu.ui.view.adapters.BUILTIN_TAB_TITLES
import com.example.komatsu.ui.viewmodel.ExploreViewModel
import com.google.android.material.tabs.TabLayoutMediator
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding
    private val viewModel: ExploreViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.allMangaCollections.observe(this) { collections ->
            val sectionsPagerAdapter = SectionsPagerAdapter(this, collections)
            val viewPager: ViewPager2 = binding.viewPager
            viewPager.adapter = sectionsPagerAdapter
            // HACK: This is a workaround for a bug in ViewPager2 where some pages
            // would get regenerated with the wrong data
            viewPager.offscreenPageLimit = 10


            val tabLayout: TabLayout = binding.tabs
            TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                if (position < BUILTIN_TAB_TITLES.size) {
                    tab.text = BUILTIN_TAB_TITLES[position]
                } else {
                    tab.text = collections[position - BUILTIN_TAB_TITLES.size].name
                }
            }.attach()
        }


    }
}