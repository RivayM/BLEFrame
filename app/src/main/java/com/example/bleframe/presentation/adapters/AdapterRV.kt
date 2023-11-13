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
    private val listener:(clickType: RvData.ClickType)-> Unit
) : ListAdapter<RvData, AdapterRV.Holder>(Holder.Comparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder = Holder(
        SampleRvBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    ){ listener }

    override fun onBindViewHolder(holder: Holder, position: Int) { getItem(position)?.let { holder.setData(it) } }

    class Holder(private val binding: SampleRvBinding, private val listener:(clickType: RvData.ClickType)-> Unit) : RecyclerView.ViewHolder(binding.root) {

        fun setData(data: RvData) = with(binding) {
            if (data.lottie == null) {
                customItemRvLottie.visibility = View.GONE
            } else customItemRvLottie.editorLottie { lottieAnimationView ->
                lottieAnimationView.setAnimation(data.lottie!!.lottieId)
            }

            if (data.image == null) {
                customItemRvImage.visibility = View.GONE
            } else customItemRvImage.setImageResource(data.image!!.imageId)

            if (data.clickType == RvData.ClickType.NONE) {
            } else {
                binding.root.setOnClickListener { listener(data.clickType) }
            }

            binding.customItemRvTextTop.text = data.textTop
            binding.customItemRvTextBottom.text = data.textBot
        }

        class Comparator <Data: RvData> : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean = oldItem == newItem
            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean = false
        }
    }
}
