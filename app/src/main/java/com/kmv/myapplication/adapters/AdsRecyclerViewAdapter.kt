package com.kmv.myapplication.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.databinding.AdListItemBinding

class AdsRecyclerViewAdapter(val auth: FirebaseAuth): RecyclerView.Adapter<AdsRecyclerViewAdapter.AdHolder>(){
    val adArray = ArrayList<AdData>()
    lateinit var binding: AdListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding, auth)
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

    class AdHolder(val binding: AdListItemBinding, val auth: FirebaseAuth): RecyclerView.ViewHolder(binding.root) {

        fun setData(ad: AdData){
            binding.apply {
                txVwDescription.text = ad.description
                txVwPrice.text = ad.price
                txVwAdListItemTitle.text = ad.title
            }
            showEditUserAdPanel(isOwner(ad))
        }

        private fun isOwner(ad: AdData): Boolean{
            return ad.uid == auth.uid
        }

        private fun showEditUserAdPanel(isOwner: Boolean){
            if (!isOwner){
                binding.editUserAdPanel.visibility = View.GONE
            }
        }
    }
}