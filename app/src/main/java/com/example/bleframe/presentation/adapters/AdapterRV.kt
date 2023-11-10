package com.example.bleframe.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.bleframe.databinding.SampleRvBinding
import com.example.bleframe.entities.RvData

class AdapterRV (
    private val listener:()-> Unit
) : ListAdapter<RvData, AdapterRV.Holder>(Holder.Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        SampleRvBinding.inflate(LayoutInflater.from(parent.context), parent, false).root
    ){ listener() }

    override fun onBindViewHolder(holder: Holder, position: Int) { getItem(position)?.let { holder.setData(it) } }

    class Holder(private val view: View, private val clickListener:()-> Unit) : RecyclerView.ViewHolder(view) {

        fun setData(data: RvData) {

        }

        private fun setAsSetting(){}
        private fun setAsDeviceScan(){}
        private fun setAsDevice(){}
        private fun setAsLog(){}

        class Comparator <Data: RvData> : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = false
        }
    }
}
