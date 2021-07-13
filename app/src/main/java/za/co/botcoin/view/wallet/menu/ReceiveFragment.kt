package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.ClipBoardUtils.copyToClipBoard
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.view.wallet.WithdrawalViewModel

class ReceiveFragment : Fragment(R.layout.receive_fragment) {
    private lateinit var binding: ReceiveFragmentBinding
    private lateinit var withdrawalViewModel: WithdrawalViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)

        this.withdrawalViewModel = ViewModelProviders.of(this).get(WithdrawalViewModel::class.java)

        if (isApiKeySet(context)) {
            this.withdrawalViewModel.receive(arguments?.getString("asset") ?: "")
            attachReceiveObserver()
        } else {
            createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()

            val action = ReceiveFragmentDirections.actionReceiveFragmentToLunoApiFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun attachReceiveObserver() {
        this.withdrawalViewModel.receiveLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displayReceiveOptions()
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
                        this.binding.addressEditText.setText(data.first().address)
                        this.binding.qrAddressImageView.setImageBitmap(createQRCode(data.first().qrCodeUri, this.binding.qrAddressImageView.width, this.binding.qrAddressImageView.height))

                        addBtnCopyListener()
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> { displayErrorTextView() }
                Status.LOADING -> { displayProgressBar() }
            }
        })
    }

    private fun addBtnCopyListener() {
        this.binding.copyImageButton.setOnClickListener { context?.let { context -> copyToClipBoard(context, this.binding.addressEditText.text.toString()) } }
    }

    private fun hideAllViews() {
        this.binding.copyImageButton.visibility = View.GONE
        this.binding.addressEditText.visibility = View.GONE
        this.binding.donateTextView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun displayReceiveOptions() {
        hideAllViews()
        this.binding.copyImageButton.visibility = View.VISIBLE
        this.binding.addressEditText.visibility = View.VISIBLE
        this.binding.donateTextView.visibility = View.VISIBLE
    }

    private fun displayErrorTextView() {
        hideAllViews()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun displayProgressBar() {
        hideAllViews()
        this.binding.progressBar.visibility = View.VISIBLE
    }
}