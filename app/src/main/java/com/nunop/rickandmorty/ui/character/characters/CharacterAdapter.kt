package com.nunop.rickandmorty.ui.character.characters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nunop.rickandmorty.data.database.entities.Character
import com.nunop.rickandmorty.databinding.CharacterItemListBinding

class CharacterAdapter(
    private val context: Context?,
    private val characterClickListener: OnCharacterClickListener
) :
    PagingDataAdapter<Character, CharacterAdapter.CharacterViewHolder>(
        CharacterComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterViewHolder(
            CharacterItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            context,
            characterClickListener
        )

    override fun onBindViewHolder(holder: CharacterViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class CharacterViewHolder(
        private val binding: CharacterItemListBinding,
        private val context: Context?,
        private val characterClickListener: OnCharacterClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: Character) = binding.apply {
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

        override fun onClick(p0: View?) {
            characterClickListener.onCharacterClick(getItem(bindingAdapterPosition) as Character)
        }
    }

    interface OnCharacterClickListener {
        fun onCharacterClick(character: Character)
    }

    object CharacterComparator :
        DiffUtil.ItemCallback<Character>() {
        override fun areItemsTheSame(
            oldItem: Character,
            newItem: Character
        ) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: Character,
            newItem: Character
        ) =
            oldItem == newItem
    }
}