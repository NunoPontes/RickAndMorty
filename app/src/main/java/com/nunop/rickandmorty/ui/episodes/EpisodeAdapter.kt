package com.nunop.rickandmorty.ui.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.data.database.entities.Episode
import com.nunop.rickandmorty.databinding.EpisodeItemListBinding

class EpisodeAdapter :
    PagingDataAdapter<Episode, EpisodeAdapter.EpisodeViewHolder>(
        EpisodeComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeViewHolder(
            EpisodeItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EpisodeViewHolder(
        private val binding: EpisodeItemListBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episode) = binding.apply {
            tvName.text = item.name
            tvGender.text = item.air_date
        }
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