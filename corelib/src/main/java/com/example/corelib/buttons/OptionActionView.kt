package com.example.corelib.buttons

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.corelib.R
import com.example.corelib.databinding.OptionActionViewBinding

class OptionActionView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: com.example.corelib.databinding.OptionActionViewBinding

    init {
        inflate(context, R.layout.option_action_view, this)
        binding = OptionActionViewBinding.bind(this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionActionView, defStyleAttr, 0)
        binding.optionActionTextView.text = typedArray.getText(R.styleable.OptionActionView_optionActionViewText)
        binding.optionActionIconImageView.setImageResource(typedArray.getResourceId(R.styleable.OptionActionView_optionActionViewIcon, R.drawable.ic_launcher_foreground))
        typedArray.recycle()
    }

    fun setText(text: String) {
        binding.optionActionTextView.text = text
    }

    fun hideOptionActionImageView() {
        binding.optionActionImageView.visibility = View.GONE
    }

    fun hideOptionActionDividerView() {
        binding.optionActionDividerView.visibility = View.GONE
    }
}