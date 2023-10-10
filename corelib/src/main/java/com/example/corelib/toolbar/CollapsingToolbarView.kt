package com.example.corelib.toolbar

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import com.example.corelib.R
import com.example.corelib.databinding.CollapsingToolbarViewBinding
import com.google.android.material.appbar.AppBarLayout

class CollapsingToolbarView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): AppBarLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: CollapsingToolbarViewBinding

    init {
        inflate(context, R.layout.collapsing_toolbar_view, this)
        binding = CollapsingToolbarViewBinding.bind(this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CollapsingToolbarView, defStyleAttr, 0)
        binding.collapsingToolbarLayout.title = typedArray.getText(R.styleable.CollapsingToolbarView_collapsingToolbarViewTitle)
        binding.backgroundImageView.setImageResource(typedArray.getResourceId(R.styleable.CollapsingToolbarView_collapsingToolbarViewIcon, R.drawable.ic_launcher_foreground))
        typedArray.recycle()
    }

    fun setTitle(title: String) {
        binding.collapsingToolbarLayout.title = title
    }
}