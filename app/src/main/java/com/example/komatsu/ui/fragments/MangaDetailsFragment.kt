package com.example.komatsu.ui.fragments
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.komatsu.R
import com.example.komatsu.databinding.MangaDetailsFragmentBinding
import com.example.komatsu.domain.models.Manga
import com.example.komatsu.ui.viewmodel.MangaDetailsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout
import org.koin.androidx.viewmodel.ext.android.viewModel

class MangaDetailsFragment : Fragment() {

    private var _binding: MangaDetailsFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MangaDetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = MangaDetailsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Retrieve manga ID passed from previous fragment/activity
        val mangaId = arguments?.getString("mangaId") ?: return

        // Fetch manga details with viewModel
        viewModel.getManga(mangaId)

        val shimmerLayout: ShimmerFrameLayout = binding.shimmerViewContainer
        val coverImage = binding.coverImage

        shimmerLayout.startShimmer()

        // Observe manga details LiveData from ViewModel
        viewModel.manga.observe(viewLifecycleOwner) { manga ->
            // Set manga title
            binding.mangaTitle.text = manga.attributes.title["en"]
            binding.mangaDescription.text = manga.attributes.description["en"]
            // Construct coverArtUrl and load it with Glide
            val coverArtFilename =
                manga.relationships.find { it.type == "cover_art" }?.attributes?.get("fileName") as? String
            val coverArtUrl =
                coverArtFilename?.let { getString(R.string.base_cover_url, manga.id, it) }
                    ?: "https://via.placeholder.com/500"

            Glide.with(this)
                .load(coverArtUrl)
                .placeholder(R.drawable.placeholder_500)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.setShimmer(null)
                        coverImage.setImageResource(R.drawable.placeholder_500)
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable,
                        model: Any,
                        target: Target<Drawable>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean,
                    ): Boolean {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.setShimmer(null)
                        return false
                    }
                })
                .into(coverImage)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
