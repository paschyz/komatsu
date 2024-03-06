package com.example.komatsu.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.komatsu.R
import com.example.komatsu.domain.models.Chapter
import com.example.komatsu.domain.models.Manga

class ChapterListAdapter(private val chapters: List<Chapter>) : RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder>() {


    lateinit var context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.manga_list_item, parent, false)
        return ChapterViewHolder(view)

    }

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        val chapter = chapters[position]
        // Bind chapter data to ViewHolder
        holder.bind(chapter, context)
    }

    override fun getItemCount(): Int {
        return chapters.size
    }

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chapter: Chapter, context: Context) {
            itemView.findViewById<TextView>(R.id.title).apply {
                this.text = chapter.chapter ?: "No chapter"
            }
        }
    
    }
}
