package com.yasser.nearby.ui.places.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.yasser.nearby.R
import com.yasser.nearby.core.model.AppPlace
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_place.*


class PlacesAdapter(private val placesList: ArrayList<AppPlace>) :
    RecyclerView.Adapter<PlacesAdapter.ItemPlaceViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemPlaceViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_place, parent, false)
        return ItemPlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemPlaceViewHolder, position: Int) {
        holder.bind(placesList[position])
    }

    override fun getItemCount(): Int {
        return placesList.count()
    }

    fun addItem(appPlace: AppPlace) {
        placesList.add(appPlace)
        notifyItemInserted(placesList.size - 1)
    }

    fun clear() {
        placesList.clear()
        notifyDataSetChanged()
    }

    class ItemPlaceViewHolder(override val containerView: View) : LayoutContainer,
        RecyclerView.ViewHolder(containerView) {

        fun bind(appPlace: AppPlace) {
            tvPlaceName.text = appPlace.name
            tvAddress.text = appPlace.address
            Glide.with(containerView.context)
                .load(appPlace.imageUrl)
                .placeholder(R.drawable.placeholder)
                .into(ivPlaceImage)
        }

    }
}