package lyubomir.babev.countries.project.views.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import lyubomir.babev.countries.project.databinding.ItemCountryBinding
import lyubomir.babev.countries.project.models.CountryModel

class CountriesAdapter(val itemClicked: (CountryModel) -> Unit) : ListAdapter<CountryModel, CountriesAdapter.CountryViewHolder>(DiffCallback) {

    inner class CountryViewHolder(private var binding: ItemCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(countryModel: CountryModel) {
            itemView.setOnClickListener { itemClicked(countryModel) }
            binding.country = countryModel
            binding.executePendingBindings()
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<CountryModel>() {
        override fun areItemsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean = oldItem.flag == newItem.flag

        override fun areContentsTheSame(oldItem: CountryModel, newItem: CountryModel): Boolean = oldItem.flag == newItem.flag
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder =
        CountryViewHolder(ItemCountryBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}