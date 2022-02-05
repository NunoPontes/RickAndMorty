package com.nunop.rickandmorty.ui.location.locations

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.nunop.rickandmorty.data.api.models.location.ResultLocation
import com.nunop.rickandmorty.data.database.entities.Location
import com.nunop.rickandmorty.databinding.LocationItemListBinding
import com.nunop.rickandmorty.utils.toLocation

class LocationAdapter(private val locationClickListener: OnLocationClickListener) :
    PagingDataAdapter<ResultLocation, LocationAdapter.LocationViewHolder>(
        LocationComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        LocationViewHolder(
            LocationItemListBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            ),
            locationClickListener
        )

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class LocationViewHolder(
        private val binding: LocationItemListBinding,
        private val locationClickListener: OnLocationClickListener
    ) :
        RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        init {
            binding.root.setOnClickListener(this)
        }

        fun bind(item: ResultLocation) = binding.apply {
            tvName.text = item.name
            tvGender.text = item.type
        }

        override fun onClick(p0: View?) {
            locationClickListener.onLocationClick(
                (getItem(bindingAdapterPosition) as
                        ResultLocation).toLocation()
            )
        }
    }

    interface OnLocationClickListener {
        fun onLocationClick(location: Location)
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