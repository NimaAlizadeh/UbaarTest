package com.example.myapplication.ui.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.data.models.AddressResponse
import com.example.myapplication.databinding.AddressItemLayoutBinding
import javax.inject.Inject

class AddressesCustomAdapter @Inject constructor(): RecyclerView.Adapter<AddressesCustomAdapter.CustomViewHolder>() {

    private lateinit var binding: AddressItemLayoutBinding
    private lateinit var context: Context

    private var addressesList = emptyList<AddressResponse>()


    inner class CustomViewHolder(private val binding1: AddressItemLayoutBinding): RecyclerView.ViewHolder(binding.root)
    {
        @SuppressLint("SetTextI18n")
        fun bindItems(address: AddressResponse, context1: Context) {
            binding1.apply {
                addressTxt.text = address.address
                fullNameTxt.text = address.first_name + " " + address.last_name
                mobileCoordinateTxt.text = address.coordinate_mobile
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        binding = AddressItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CustomViewHolder(binding)
    }

    override fun getItemCount(): Int = addressesList.size

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        val music = addressesList[position]
        holder.bindItems(music, holder.itemView.context)
    }

    fun setData(newListData: List<AddressResponse>)
    {
        val notesDiffUtils = MusicDiffUtils(addressesList, newListData)
        val diffUtils = DiffUtil.calculateDiff(notesDiffUtils)
        addressesList = newListData
        diffUtils.dispatchUpdatesTo(this)
    }

    class MusicDiffUtils(private val oldItem: List<AddressResponse>, private val newItem: List<AddressResponse>) : DiffUtil.Callback(){
        override fun getOldListSize() = oldItem.size

        override fun getNewListSize() = newItem.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition].address == newItem[newItemPosition].address
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean
        {
            return oldItem[oldItemPosition].address == newItem[newItemPosition].address && oldItem[oldItemPosition].first_name == newItem[newItemPosition].first_name &&
                    oldItem[oldItemPosition].coordinate_mobile == newItem[newItemPosition].coordinate_mobile && oldItem[oldItemPosition].last_name == newItem[newItemPosition].last_name
        }
    }

}