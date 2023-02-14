package com.example.corelib.informational

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.corelib.R
import com.example.corelib.databinding.InformationViewBinding

class InformationView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: InformationViewBinding

    init {
        inflate(context, R.layout.information_view, this)
        binding = InformationViewBinding.bind(this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.InformationView, defStyleAttr, 0)
        binding.informationTextView.text = typedArray.getText(R.styleable.InformationView_informationText)
        typedArray.recycle()
    }

    fun setInformationTextView(text: String) {
        binding.informationTextView.text = text
    }
}