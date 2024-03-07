package com.example.komatsu.ui.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.komatsu.R
import com.example.komatsu.databinding.ActivityReadScanBinding
import com.example.komatsu.ui.fragments.MangaDetailsFragment
import com.example.komatsu.ui.fragments.ReadScanFragment

class ReadScanActivity : AppCompatActivity() {
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var binding: ActivityReadScanBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReadScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragment = ReadScanFragment().apply {
            arguments = Bundle().apply {
                putString("chapterId", intent.getStringExtra("chapterId"))
            }
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()

    }
}
