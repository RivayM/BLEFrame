package com.example.bleframe.presentation.custom_view

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bleframe.R
import com.example.bleframe.databinding.CustomItemRvBinding

class CustomItemRv @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : ConstraintLayout(context, attrs, defStyleAttr) {

    private var binding: CustomItemRvBinding

    init { binding = CustomItemRvBinding.bind(
        inflate(context, R.layout.custom_view_lottie, this))}

    fun editorLottie(editor: (CustomViewLottie) -> Unit) = editor(binding.customItemRvLottie)
    fun editorTextTop(editor: (TextView) -> Unit) = editor(binding.customItemRvTextTop)
    fun editorTextBot(editor: (TextView) -> Unit) = editor(binding.customItemRvTextBottom)
    fun editorImage(editor: (ImageView) -> Unit) = editor(binding.customItemRvImage)
}

