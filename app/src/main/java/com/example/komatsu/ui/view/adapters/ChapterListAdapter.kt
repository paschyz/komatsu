import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.komatsu.R
import com.example.komatsu.domain.models.Chapter

class ChapterListAdapter(private var chapters: List<Chapter>, private val listener: OnChapterClickListener) :
    RecyclerView.Adapter<ChapterListAdapter.ChapterViewHolder>() {

    lateinit var context: Context

    interface OnChapterClickListener {
        fun onChapterClick(chapter: Chapter)
    }

    inner class ChapterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(chapter: Chapter, listener: OnChapterClickListener) {
            itemView.findViewById<TextView>(R.id.title).apply {
                text = chapter.chapter ?: "No chapter"
            }
            itemView.setOnClickListener {
                listener.onChapterClick(chapter)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChapterViewHolder {
        context = parent.context
        val view = LayoutInflater.from(parent.context).inflate(R.layout.chapter_list_item, parent, false)
        return ChapterViewHolder(view)
    }

    override fun getItemCount() = chapters.size

    override fun onBindViewHolder(holder: ChapterViewHolder, position: Int) {
        holder.bind(chapters[position], listener)
    }

    fun submitList(chapters: List<Chapter>) {
        this.chapters = chapters
        notifyDataSetChanged()
    }
}
