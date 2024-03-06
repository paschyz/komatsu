package com.example.komatsu.ui.view.adapters

import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import com.example.komatsu.ui.fragments.MangaListFragment


public val BUILTIN_TAB_TITLES =
    arrayOf(
        "Discover",
    )

class SectionsPagerAdapter(
    activity: FragmentActivity,
    private val mangaCollections: List<MangaCollectionEntity>,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = BUILTIN_TAB_TITLES.size + mangaCollections.size

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MangaListFragment.newInstance(null)
            else -> {
                val collectionPosition = position - BUILTIN_TAB_TITLES.size
                if (collectionPosition < 0 || collectionPosition >= mangaCollections.size) {
                    throw IllegalArgumentException("Invalid position: $position")
                }

                val mangaIds =
                    mangaCollections.getOrNull(collectionPosition)?.mangas ?: listOf()

                MangaListFragment.newInstance(mangaIds)
            }
        }
    }
}
