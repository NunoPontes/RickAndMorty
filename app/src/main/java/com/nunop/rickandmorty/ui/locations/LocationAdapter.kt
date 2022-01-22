package com.nunop.rickandmorty.ui.locations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.databinding.LocationItemListBinding

class LocationAdapter(private val context: Context?) :
    PagingDataAdapter<ResultLocation, LocationAdapter.CharacterViewHolder>(
        LocationComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterViewHolder(
            LocationItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            context
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CharacterViewHolder(
        private val binding: LocationItemListBinding,
        private val context: Context?
    ) :
        RecyclerView.ViewHolder(binding.root) {

        init {
//            itemView.setOnClickListener {
//                characterClickListener?.onCharacterClicked(
//                    binding,
//                    getItem(absoluteAdapterPosition) as com.nunop.rickandmorty.data.database.entities.Character
//                )
//            }
        }

        fun bind(item: ResultLocation) = binding.apply {
            tvName.text = item.name
            tvGender.text = item.dimension
        }
    }

    object LocationComparator :
        DiffUtil.ItemCallback<ResultLocation>() {
        override fun areItemsTheSame(
            oldItem: ResultLocation,
            newItem: ResultLocation
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ResultLocation,
            newItem: ResultLocation
        ) =
            oldItem == newItem
    }
}