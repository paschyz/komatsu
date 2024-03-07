package com.example.komatsu.ui.view.adapters

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.komatsu.R

import com.example.komatsu.models.Manga
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.facebook.shimmer.ShimmerFrameLayout

class MangaListAdapter(private var mangas: List<Manga>, private val listener: OnMangaClickListener) :
    RecyclerView.Adapter<MangaListAdapter.MangaViewHolder>() {

    lateinit var context: Context

    interface OnMangaClickListener {
        fun onMangaClick(manga: Manga)
        fun onMangaLongClick(manga: Manga)
    }

    class MangaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val shimmerLayout: ShimmerFrameLayout =
            view.findViewById(R.id.shimmer_view_container)
        private val coverImage: ImageView = view.findViewById(R.id.coverImage)

        fun bind(manga: Manga, context: Context, listener: OnMangaClickListener) {
            itemView.findViewById<TextView>(R.id.title).apply {
                this.text = manga.attributes.title["en"] ?: "No title"
            }

            itemView.setOnClickListener {
                listener.onMangaClick(manga)
            }

            itemView.setOnLongClickListener {
                listener.onMangaLongClick(manga)
                true
            }


            shimmerLayout.startShimmer()

            val coverArtFilename = Manga.getCoverArtFilename(manga)
            val coverArtUrl =
                coverArtFilename?.let { context.getString(R.string.base_cover_url, manga.id, it) }
                    ?: "https://via.placeholder.com/150"


            Glide.with(context)
                .load(coverArtUrl)
                .placeholder(R.drawable.placeholder_150)
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>,
                        isFirstResource: Boolean,
                    ): Boolean {
                        shimmerLayout.stopShimmer()
                        shimmerLayout.setShimmer(null)
                        coverImage.setImageResource(R.drawable.placeholder_150)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MangaViewHolder {
        context = parent.context

        val view =
            LayoutInflater.from(context).inflate(R.layout.manga_list_item, parent, false)
        return MangaViewHolder(view)
    }

    override fun onBindViewHolder(holder: MangaViewHolder, position: Int) {
        holder.bind(mangas[position], context, listener)
    }

    override fun getItemCount() = mangas.size

    fun updateMangas(newMangas: List<Manga>) {
        Log.d("MangaListAdapter", "Updating mangas, new size: ${newMangas.size}")
        mangas = newMangas
        notifyDataSetChanged()
    }
}
