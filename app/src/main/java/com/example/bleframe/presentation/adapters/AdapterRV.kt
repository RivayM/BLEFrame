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
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.setData(it){  listener()  } }
    }

    class Holder(private val view: View) : RecyclerView.ViewHolder(view) {

        fun setData(data: RvData, listener:()-> Unit) {
            when(true){
                (data is RvData.Device)->{

                }
                (data is RvData.Log)->{

                }
                (data is RvData.Settings)->{

                }
                else -> throw IllegalArgumentException("Holder. Unknown Type for data: RvData")
            }
        }

        class Comparator <Data: RvData> : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = false
        }
    }
}
