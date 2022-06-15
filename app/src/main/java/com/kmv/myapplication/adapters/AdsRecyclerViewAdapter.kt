package com.kmv.myapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.data.AdData
import com.kmv.myapplication.databinding.AdListItemBinding

class AdsRecyclerViewAdapter: RecyclerView.Adapter<AdsRecyclerViewAdapter.AdHolder>(){
    val adArray = ArrayList<AdData>()
    lateinit var binding: AdListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    fun updateAdapter(newList: List<AdData>){
        adArray.clear()
        adArray.addAll(newList)
        notifyDataSetChanged()
    }

    class AdHolder(val binding: AdListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun setData(ad: AdData){
            binding.apply {
                txVwDescription.text = ad.description
                txVwPrice.text = ad.price
                txVwAdListItemTitle.text = ad.title
            }
        }
    }
}