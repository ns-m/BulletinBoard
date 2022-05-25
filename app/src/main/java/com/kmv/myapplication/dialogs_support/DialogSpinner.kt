package com.kmv.myapplication.dialogs_support

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kmv.myapplication.R
import com.kmv.myapplication.utils.TreatmentCityList

class DialogSpinner {
    fun showSpinnerDialog(context: Context, list:ArrayList<String>){
        val builder = AlertDialog.Builder(context)
        val binding = LayoutInflater.from(context).inflate(R.layout.spinner_layout, null)
        val adapter = RCViewDialogSpinnerAdapter()
        val rcView = binding.findViewById<RecyclerView>(R.id.spinnerRecyclerView)
        val searchView = binding.findViewById<SearchView>(R.id.spinnerSeachLine)
        rcView.layoutManager = LinearLayoutManager(context)
        rcView.adapter = adapter
        adapter.updateAdapter(list)
        builder.setView(binding)
        setSearchViewListener(adapter,list, searchView)
        builder.show()
    }

    private fun setSearchViewListener(adapter: RCViewDialogSpinnerAdapter, list: ArrayList<String>, searchView: SearchView?) {
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                val tempList = TreatmentCityList.filterListData(list, newText)
                adapter.updateAdapter(tempList)
                return true
            }
        })
    }

}