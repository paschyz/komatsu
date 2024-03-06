package com.example.komatsu.ui.view.adapters


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.komatsu.data.database.entities.MangaCollectionEntity
import com.example.komatsu.ui.fragments.MangaListFragment
import com.example.komatsu.ui.fragments.PlaceholderFragment

public val BUILTIN_TAB_TITLES = arrayOf(
    "Discover",
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
class SectionsPagerAdapter(activity: FragmentActivity,
                           private val mangaCollections: List<MangaCollectionEntity>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int {
        return BUILTIN_TAB_TITLES.size + mangaCollections.size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MangaListFragment()
            else -> PlaceholderFragment.newInstance(position + 1)
        }
    }
}
