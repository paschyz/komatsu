package com.example.komatsu.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.komatsu.data.models.MangaCollection
import com.example.komatsu.databinding.FragmentMangaListBinding
import com.example.komatsu.domain.models.Manga
import com.example.komatsu.ui.view.activities.MangaDetailsActivity
import com.example.komatsu.ui.view.adapters.MangaListAdapter
import com.example.komatsu.ui.viewmodel.MangaListViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class MangaListFragment(
    private var mangaIds: List<String>? = null,
    private var currentCollectionId: String? = null,
) : Fragment(), CoroutineScope {

    private var _binding: FragmentMangaListBinding? = null
    private val binding
        get() = _binding!!

    private val viewModel: MangaListViewModel by viewModel()

    private var searchJob: Job? = null
    private val debouncePeriod: Long = 500

    private var spanCount = 3

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mangaIds = it.getStringArrayList(ARG_MANGA_IDS)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentMangaListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter =
            MangaListAdapter(
                emptyList(),
                object : MangaListAdapter.OnMangaClickListener {
                    override fun onMangaClick(manga: Manga) {
                        val intent = Intent(context, MangaDetailsActivity::class.java)
                        intent.putExtra("mangaId", manga.id)
                        startActivity(intent)
                    }

                    override fun onMangaLongClick(manga: Manga) {
                        viewModel.mangaCollections.observe(viewLifecycleOwner) { collections ->
                            showCollectionsDialog(manga, collections)
                        }
                    }
                }
            )
        binding.mangaListRecyclerView.layoutManager = GridLayoutManager(context, 3)
        binding.mangaListRecyclerView.adapter = adapter

        binding.mangaListRecyclerView.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    if (!recyclerView.canScrollVertically(1)) {
                        showLoadMore()
                    }
                }
            }
        )

        binding.searchEditText.addTextChangedListener { performSearch(it.toString()) }

        binding.searchButton.setOnClickListener {
            if (binding.searchEditText.text.isEmpty()) {
                searchJob?.cancel()
                viewModel.getMangas(mangaIds)
                return@setOnClickListener
            }

            performSearch(binding.searchEditText.text.toString())
        }

        binding.changeLayoutFab.setOnClickListener {
            spanCount -= 1
            if (spanCount < 1) {
                spanCount = 3
            }

            binding.mangaListRecyclerView.layoutManager =
                if (spanCount == 1) {
                    LinearLayoutManager(context)
                } else {
                    GridLayoutManager(context, spanCount)
                }
        }

        viewModel.mangas.observe(viewLifecycleOwner) { mangas: List<Manga> ->
            adapter.updateMangas(mangas)
        }

        viewModel.getMangas(mangaIds)
    }

    private fun showCollectionsDialog(manga: Manga, collections: List<MangaCollection>) {
        val collectionNames = collections.map { it.name }.toTypedArray()
        val mangasWithCollections = viewModel.mangasWithCollections.value ?: listOf()
        val checkedItems = BooleanArray(collections.size) { false }

        for (i in collections.indices) {
            val collectionId = collections[i].id
            mangasWithCollections
                .filter { it.collections.any { it.collectionId == collectionId } }
                .map { it.manga.mangaId }.also {
                    checkedItems[i] = it.contains(manga.id)
                }
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Add to Collection")
            .setMultiChoiceItems(collectionNames, checkedItems) { dialog, which, isChecked ->
                checkedItems[which] = isChecked
            }
            .setPositiveButton("OK") { dialog, which ->
                for (i in collections.indices) {
                    if (checkedItems[i]) {
                        viewModel.addMangaToCollection(manga.id, collections[i].id)
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun showLoadMore() {
        binding.loadMoreProgressBar.visibility = View.VISIBLE
        if (mangaIds != null) {
            viewModel.loadMoreMangas(mangaIds)
        } else {
            viewModel.loadMoreMangas()
        }
        binding.loadMoreProgressBar.visibility = View.GONE
    }

    private fun performSearch(query: String) {
        searchJob?.cancel()
        searchJob = launch {
            delay(debouncePeriod)
            viewModel.searchMangas(query)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        coroutineContext.cancel()
    }

    companion object {
        private const val ARG_MANGA_IDS = "mangaIds"
        private const val ARG_COLLECTION_ID = "collectionId"

        fun newInstance(mangaIds: List<String>?, collectionId: String?): MangaListFragment {
            val fragment = MangaListFragment()
            val args = Bundle()
            args.putStringArrayList(ARG_MANGA_IDS, mangaIds?.let { ArrayList(it) })
            args.putString(ARG_COLLECTION_ID, collectionId)
            fragment.arguments = args
            return fragment
        }
    }

}
