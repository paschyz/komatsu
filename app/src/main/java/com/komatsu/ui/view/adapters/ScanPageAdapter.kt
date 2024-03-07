package com.komatsu.ui.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.komatsu.R

class ScanPagerAdapter(private val scanImages: List<Int>) :
    RecyclerView.Adapter<ScanPagerAdapter.ScanViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.scan_list_item, parent, false)
        return ScanViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        val scanImage = scanImages[position]
        holder.bind(scanImage)
    }

    override fun getItemCount(): Int = scanImages.size

    inner class ScanViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.scanView)

        fun bind(imageResId: Int) {
            Glide.with(itemView.context)
                .load(imageResId)
                .into(imageView)
        }
    }
}
