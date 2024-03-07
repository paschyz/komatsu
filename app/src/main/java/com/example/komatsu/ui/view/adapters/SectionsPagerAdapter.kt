package com.example.komatsu.ui.view.adapters

import android.os.Parcelable
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import com.example.komatsu.data.database.entities.MangaWithCollections
import com.example.komatsu.data.models.MangaCollection
import com.example.komatsu.ui.fragments.MangaListFragment


val BUILTIN_TAB_TITLES =
    arrayOf(
        "Discover",
        "Featured",
    )

class SectionsPagerAdapter(
    activity: FragmentActivity,
    private val mangaCollections: List<MangaCollection>,
    private val mangasWithCollections: List<MangaWithCollections>,
) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int = BUILTIN_TAB_TITLES.size + mangaCollections.size

    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> MangaListFragment.newInstance(null)
            // TODO: Add a featured manga collection
            1 -> MangaListFragment.newInstance(null)
            else -> {
                val collectionPosition = position - BUILTIN_TAB_TITLES.size
                if (collectionPosition < 0 || collectionPosition >= mangaCollections.size) {
                    throw IllegalArgumentException("Invalid position: $position")
                }

                val collection = mangaCollections[collectionPosition]
                val mangaIds = mangasWithCollections
                    .filter { it.collections.any { it.collectionId == collection.id } }
                    .map { it.manga.mangaId }


                MangaListFragment.newInstance(mangaIds)
            }
        }
    }
}
