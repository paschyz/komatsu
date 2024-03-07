package com.komatsu.ui.view.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import com.komatsu.R
import com.komatsu.databinding.ActivityReadScanBinding
import com.komatsu.ui.fragments.ReadScanFragment

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
