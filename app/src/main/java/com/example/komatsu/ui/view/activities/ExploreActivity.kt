package com.example.komatsu.ui.view.activities


import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager2.widget.ViewPager2
import com.example.komatsu.ui.view.adapters.SectionsPagerAdapter
import com.example.komatsu.databinding.ActivityExploreBinding
import com.example.komatsu.ui.view.adapters.TAB_TITLES
import com.google.android.material.tabs.TabLayoutMediator

class ExploreActivity : AppCompatActivity() {

    private lateinit var binding: ActivityExploreBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityExploreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, viewPager) { tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()

    }
}