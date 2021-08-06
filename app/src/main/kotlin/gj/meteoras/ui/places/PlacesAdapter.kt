package gj.meteoras.ui.places

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import gj.meteoras.data.Place
import gj.meteoras.databinding.ItemPlaceBinding

class PlacesAdapter() : ListAdapter<Place, PlacesAdapter.ViewHolder>(PlacesDiff()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPlaceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            name.text = "${item.name} (${item.administrativeDivision ?: ""}) ${item.countryCode}"
        }
    }

    data class ViewHolder(
        val binding: ItemPlaceBinding
    ) : RecyclerView.ViewHolder(binding.root)
}

private class PlacesDiff : DiffUtil.ItemCallback<Place>() {

    override fun areItemsTheSame(oldItem: Place, newItem: Place): Boolean =
        oldItem.code == newItem.code

    override fun areContentsTheSame(oldItem: Place, newItem: Place): Boolean =
        oldItem == newItem
}
