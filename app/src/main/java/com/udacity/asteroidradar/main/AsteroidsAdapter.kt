package com.udacity.asteroidradar.main


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.R

class AsteroidsAdapter: RecyclerView.Adapter<AsteroidsViewHolder>() {
    var data = listOf<Asteroid>()
    set(value){
        field = value
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidsViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.asteriod_list_item, parent, false)
        return AsteroidsViewHolder(view)
    }

    override fun onBindViewHolder(holder: AsteroidsViewHolder, position: Int) {
        val item = data[position]

        holder.codenameTextView.text = item.codename
        holder.dateTextView.text = item.closeApproachDate

        if(item.isPotentiallyHazardous) {
            holder.hazardImageView.setImageResource(R.drawable.ic_status_potentially_hazardous)
        } else {
            holder.hazardImageView.setImageResource(R.drawable.ic_status_normal)
        }
    }

    override fun getItemCount(): Int = data.size

}

class AsteroidsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

    val codenameTextView : TextView = itemView.findViewById(R.id.codename_text)
    val dateTextView : TextView = itemView.findViewById(R.id.date_text)
    val hazardImageView : ImageView = itemView.findViewById(R.id.hazard_image)

}