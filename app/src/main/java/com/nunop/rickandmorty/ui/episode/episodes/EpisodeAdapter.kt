package com.nunop.rickandmorty.ui.episode.episodes

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodeItemListBinding

class EpisodeAdapter(private val episodeClickListener: OnEpisodeClickListener) :
    PagingDataAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(
        EpisodeComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            EpisodeItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            episodeClickListener
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EpisodeViewHolder(
        private val binding: EpisodeItemListBinding,
        private val episodeClickListener: OnEpisodeClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: Episode) = binding.apply {
            tvName.text = item.name
            tvAirDate.text = item.air_date
            tvEpisode.text = item.episode
        }

        override fun onClick(p0: View?) {
            episodeClickListener.onEpisodeClick(getItem(bindingAdapterPosition) as Episode)
        }
    }

    interface OnEpisodeClickListener {
        fun onEpisodeClick(episode: Episode)
    }

    object EpisodeComparator :
        DiffUtil.ItemCallback<Episode>() {
        override fun areItemsTheSame(
            oldItem: Episode,
            newItem: Episode
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Episode,
            newItem: Episode
        ) =
            oldItem == newItem
    }
}