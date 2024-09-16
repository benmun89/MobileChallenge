package com.mobilechallenge.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobilechallenge.R
import com.mobilechallenge.core.model.data.MovieModel
import com.mobilechallenge.core.network.BuildConfig

class MovieAdapter(
    private val onItemClick: (MovieModel) -> Unit
) : PagingDataAdapter<MovieModel, MovieAdapter.ItemViewHolder>(MovieDiffCallback()) {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemPoster: ImageView = itemView.findViewById(R.id.itemImage)

        init {
            itemView.setOnClickListener {
                getItem(absoluteAdapterPosition)?.let { movie ->
                    onItemClick(movie)
                }
            }
        }

        fun bind(movie: MovieModel?) {
            movie?.let {
                Glide.with(itemView.context)
                    .load("${BuildConfig.TMDB_IMAGE_ORIGINAL_URL}${it.posterPath}")
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .error(R.drawable.ic_launcher_foreground)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .centerInside()
                    .into(itemPoster)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val movie = getItem(position)
        holder.bind(movie)
    }
}

class MovieDiffCallback : DiffUtil.ItemCallback<MovieModel>() {
    override fun areItemsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: MovieModel, newItem: MovieModel): Boolean {
        return oldItem == newItem
    }
}