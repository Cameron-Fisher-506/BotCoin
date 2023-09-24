package com.example.corelib.buttons

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.corelib.R
import com.example.corelib.databinding.OptionActionMultilineViewBinding

class OptionActionMultilineView @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0): ConstraintLayout(context, attrs, defStyleAttr) {
    private lateinit var binding: OptionActionMultilineViewBinding

    init {
        inflate(context, R.layout.option_action_multiline_view, this)
        binding = OptionActionMultilineViewBinding.bind(this)

        val typedArray: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.OptionActionMultiLineView, defStyleAttr, 0)
        binding.optionActionInformationOneTextView.text = typedArray.getText(R.styleable.OptionActionMultiLineView_optionActionMultiLineViewInformationOneText)
        binding.optionActionInformationTwoTextView.text = typedArray.getText(R.styleable.OptionActionMultiLineView_optionActionMultilineViewInformationTwoText)
        binding.optionActionDescriptionOneTextView.text = typedArray.getText(R.styleable.OptionActionMultiLineView_optionActionMultilineViewDescriptionOneText)
        binding.optionActionDescriptionTwoTextView.text = typedArray.getText(R.styleable.OptionActionMultiLineView_optionActionMultilineViewDescriptionTwoText)
        binding.informationView.setInformationTextView(typedArray.getText(R.styleable.OptionActionMultiLineView_optionActionMultilineViewInformationViewText))
        binding.optionActionIconImageView.setImageResource(typedArray.getResourceId(R.styleable.OptionActionMultiLineView_optionActionMultiLineViewIcon, R.drawable.ic_launcher_foreground))
        typedArray.recycle()
    }

    fun setInformationOneText(text: String) {
        binding.optionActionInformationOneTextView.text = text
    }

    fun setInformationTwoText(text: String) {
        binding.optionActionInformationTwoTextView.text = text
    }

    fun setDescriptionOneText(text: String) {
        binding.optionActionDescriptionOneTextView.text = text
    }

    fun setDescriptionTwoText(text: String) {
        binding.optionActionDescriptionTwoTextView.text = text
    }

    fun hideOptionActionImageView() {
        binding.optionActionImageView.visibility = View.GONE
    }

    fun hideOptionActionDividerView() {
        binding.optionActionDividerView.visibility = View.GONE
    }
}