package com.udacity.asteroidradar.main


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.DatabaseAsteroids
import com.udacity.asteroidradar.databinding.AsteriodListItemBinding

class AsteroidsAdapter(val clickListener: AsteroidsListener):
    androidx.recyclerview.widget.ListAdapter<DatabaseAsteroids, AsteroidsViewHolder>
        (AsteriodsDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        return AsteroidsViewHolder.asteroidsViewHolder(parent)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}

class AsteroidsViewHolder private constructor(val binding: AsteriodListItemBinding)
    : RecyclerView.ViewHolder(binding.root){


    fun bind(item: DatabaseAsteroids, clickListener: AsteroidsListener) {
        binding.asteroid = item
        binding.executePendingBindings()
        binding.clickListener = clickListener
    }

    companion object {
        fun asteroidsViewHolder(parent: ViewGroup): AsteroidsViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = AsteriodListItemBinding.inflate(layoutInflater, parent, false)
            return AsteroidsViewHolder(binding)
        }
    }

}

class AsteriodsDiffCallback : DiffUtil.ItemCallback<DatabaseAsteroids>(){
    override fun areItemsTheSame(oldItem: DatabaseAsteroids, newItem: DatabaseAsteroids): Boolean {
        return oldItem.id ==newItem.id
    }

    override fun areContentsTheSame(oldItem: DatabaseAsteroids, newItem: DatabaseAsteroids): Boolean {
        return oldItem == newItem
    }

}

class AsteroidsListener (val clickListener: (asteroid: DatabaseAsteroids) -> Unit){
    fun onClick(asteroid: DatabaseAsteroids) = clickListener(asteroid)
}