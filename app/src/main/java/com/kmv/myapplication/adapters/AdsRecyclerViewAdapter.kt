package com.kmv.myapplication.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.kmv.myapplication.MainActivity
import com.kmv.myapplication.R
import com.kmv.myapplication.act.EditAdsAct
import com.kmv.myapplication.model.AdData
import com.kmv.myapplication.databinding.AdListItemBinding
import com.kmv.myapplication.utils.DiffUtilHelper

class AdsRecyclerViewAdapter(val activity: MainActivity): RecyclerView.Adapter<AdsRecyclerViewAdapter.AdHolder>(){
    val adArray = ArrayList<AdData>()
    lateinit var binding: AdListItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdHolder {
        binding = AdListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AdHolder(binding, activity)
    }

    override fun onBindViewHolder(holder: AdHolder, position: Int) {
        holder.setData(adArray[position])
    }

    override fun getItemCount(): Int {
        return adArray.size
    }

    fun updateAdapter(newList: List<AdData>){
        val diffResult = DiffUtil.calculateDiff(DiffUtilHelper(adArray, newList))
        diffResult.dispatchUpdatesTo(this)
        adArray.clear()
        adArray.addAll(newList)
        /*notifyDataSetChanged()*/
    }

    class AdHolder(val binding: AdListItemBinding, val activity: MainActivity): RecyclerView.ViewHolder(binding.root) {

        fun setData(ad: AdData) = with(binding) {
            txVwDescription.text = ad.description
            txVwPrice.text = ad.price
            txVwAdListItemTitle.text = ad.title
            txVwCounter.text = ad.viewsCounter
            showEditUserAdPanel(isOwner(ad))
            if (ad.isFavor){
                imgBttnFavorite.setImageResource(R.drawable.ic_favorite_pressed)
            }else{
                imgBttnFavorite.setImageResource(R.drawable.ic_favorite_normal)
            }
            imgBttnFavorite.setOnClickListener {
                activity.onFavorClicked(ad)
            }
            itemView.setOnClickListener {
                activity.onAdViewed(ad)
            }
            imgBttnEditAd.setOnClickListener(onClickEdit(ad))
            imgBttnDeleteAd.setOnClickListener {
                activity.onDelItem(ad)
            }
        }

        private fun onClickEdit(ad: AdData): View.OnClickListener{
            return View.OnClickListener {
                val editIntent = Intent(activity, EditAdsAct::class.java).apply {
                    putExtra(MainActivity.EDIT_STATE, true)
                    putExtra(MainActivity.ADS_DATA, ad)
                }
                activity.startActivity(editIntent)
            }
        }

        private fun isOwner(ad: AdData): Boolean{
            return ad.uid == activity.mainAuth.uid
        }

        private fun showEditUserAdPanel(isOwner: Boolean){
            if (!isOwner){
                binding.editUserAdPanel.visibility = View.GONE
            }
        }
    }

    interface ItemListener{
        fun onDelItem(ad: AdData)
        fun onAdViewed(ad: AdData)
        fun onFavorClicked(ad: AdData)
    }
}