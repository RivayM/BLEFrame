package com.example.bleframe.presentation.custom

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.airbnb.lottie.LottieDrawable
import com.example.bleframe.R
import com.example.bleframe.databinding.CustomViewLottieBinding

class CustomViewLottie @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: CustomViewLottieBinding

    init {
        //binding = CustomViewLoadBinding.inflate(LayoutInflater.from(context))
        //addView(binding.root)
        val root = inflate(context, R.layout.custom_view_lottie, this)
        binding = CustomViewLottieBinding.bind(root)
    }

    fun play() = binding.customViewLoadLottie.apply {
        visibility = View.VISIBLE
        repeatCount = LottieDrawable.INFINITE
        playAnimation()
    }

    fun stop() = binding.customViewLoadLottie.apply {
        visibility = View.GONE
        pauseAnimation()
    }

    fun replaceLottie(rawId:Int)= binding.customViewLoadLottie.apply {
        setAnimation(rawId)
    }
}

