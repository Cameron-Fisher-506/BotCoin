package com.example.corelib.informational

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.corelib.R
import com.example.corelib.databinding.InformationLineItemViewBinding

class InformationLineItemView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: InformationLineItemViewBinding

    init {
        inflate(context, R.layout.information_line_item_view, this)
        binding = InformationLineItemViewBinding.bind(this)


        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.InformationLineItemView, defStyleAttr, 0)
        binding.labelTextView.text = typedArray.getText(R.styleable.InformationLineItemView_informationLineItemViewLabel)
        binding.valueTextView.text = typedArray.getText(R.styleable.InformationLineItemView_informationLineItemViewValue)
        typedArray.recycle()
    }

    fun setLabelTextView(text: String) {
        binding.labelTextView.text = text
    }

    fun setValueTextView(text: String) {
        binding.valueTextView.text = text
    }
}