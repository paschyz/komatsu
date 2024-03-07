package com.komatsu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.komatsu.databinding.ActivityMainBinding
import com.komatsu.ui.fragments.MangaListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(com.komatsu.R.id.fragmentContainer, MangaListFragment())
                .commitNow()
        }
    }
}