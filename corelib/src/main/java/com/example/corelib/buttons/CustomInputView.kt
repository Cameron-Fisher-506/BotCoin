package com.example.corelib.buttons

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.corelib.R
import com.example.corelib.databinding.CustomInputViewBinding

class CustomInputView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: CustomInputViewBinding

    init {
        inflate(context, R.layout.custom_input_view, this)
        binding = CustomInputViewBinding.bind(this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomInputView, defStyleAttr, 0)
        binding.titleTextView.text = typedArray.getText(R.styleable.CustomInputView_title)
        binding.descriptionTextView.text = typedArray.getText(R.styleable.CustomInputView_description)
        //TODO: Set Input type
        typedArray.recycle()
    }

    fun setDescription(description: String) {
        binding.descriptionTextView.text = description
        binding.descriptionTextView.visibility = View.VISIBLE
    }

    fun setText(text: String) {
        binding.informationEditText.setText(text)
    }

    fun getText(): String = binding.informationEditText.text.toString()
}