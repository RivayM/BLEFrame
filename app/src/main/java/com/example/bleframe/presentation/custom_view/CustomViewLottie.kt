package com.example.bleframe.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import com.example.bleframe.R
import com.example.bleframe.databinding.CustomViewLottieBinding

class CustomViewLottie @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr) {

    private var binding: CustomViewLottieBinding

    init { binding = CustomViewLottieBinding.bind(
        inflate(context, R.layout.custom_view_lottie, this))}

    fun editorLottie(editor: (LottieAnimationView) -> Unit) = editor(binding.customViewLoadLottie)
}

