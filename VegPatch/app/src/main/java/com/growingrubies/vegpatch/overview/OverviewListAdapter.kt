package com.growingrubies.vegpatch.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.growingrubies.vegpatch.data.Plant
import com.growingrubies.vegpatch.databinding.ActivePlantListItemBinding
import com.growingrubies.vegpatch.databinding.PlantListItemBinding

class OverviewListAdapter(val clickListener: PlantListener): ListAdapter<Plant, OverviewListAdapter.ViewHolder>(PlantDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)!!
        holder.bind(clickListener, item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ActivePlantListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(clickListener: PlantListener, item: Plant) {
            binding.plant = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ActivePlantListItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class PlantDiffCallback : DiffUtil.ItemCallback<Plant>() {
    //Must override two methods; items the same and contents the same
    override fun areItemsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Plant, newItem: Plant): Boolean {
        //Will check the contents of these items because they are defined as data classes
        return oldItem == newItem
    }
}

class PlantListener(val clickListener: (id: Long) -> Unit) {
    fun onClick(plant: Plant) = clickListener(plant.id)
}