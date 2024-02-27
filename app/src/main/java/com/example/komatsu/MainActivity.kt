package com.example.komatsu

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.komatsu.databinding.ActivityMainBinding
import com.example.komatsu.ui.fragments.MangaListFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, MangaListFragment())
                .commitNow()
        }
    }
}