package com.nunop.rickandmorty.ui.characters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunop.rickandmorty.data.api.models.character.ResultCharacter
import com.nunop.rickandmorty.databinding.CharacterItemListBinding

class CharacterAdapter(private val context: Context?) :
    PagingDataAdapter<ResultCharacter, CharacterAdapter.CharacterViewHolder>(
        CharacterComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterViewHolder(
            CharacterItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            context
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CharacterViewHolder(private val binding: CharacterItemListBinding,
    private val context: Context?) :
        RecyclerView.ViewHolder(binding.root) {

        init {
//            itemView.setOnClickListener {
//                characterClickListener?.onCharacterClicked(
//                    binding,
//                    getItem(absoluteAdapterPosition) as com.nunop.rickandmorty.data.database.entities.Character
//                )
//            }
        }

        fun bind(item: ResultCharacter) = binding.apply {
            tvName.text = item.name
            tvGender.text = item.gender
            tvSpecies.text = item.species
            tvType.text = item.type
            context?.let {
                Glide.with(it)
                    .load(item.image)
                    .into(ivPhoto)
            }

        }
    }

    object CharacterComparator :
        DiffUtil.ItemCallback<ResultCharacter>() {
        override fun areItemsTheSame(
            oldItem: ResultCharacter,
            newItem: ResultCharacter
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ResultCharacter,
            newItem: ResultCharacter
        ) =
            oldItem == newItem
    }
}