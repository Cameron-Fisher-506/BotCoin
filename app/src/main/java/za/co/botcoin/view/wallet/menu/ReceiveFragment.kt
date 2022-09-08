package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.services.ClipBoardService.copyToClipBoard
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.view.wallet.WalletBaseFragment

class ReceiveFragment : WalletBaseFragment(R.layout.receive_fragment) {
    private lateinit var binding: ReceiveFragmentBinding
    private lateinit var receiveViewModel: ReceiveViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)

        this.receiveViewModel = ViewModelProviders.of(this).get(ReceiveViewModel::class.java)

        if (isApiKeySet(context)) {
            receiveAndObserveReceive()
        } else {
            createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()

            val action = ReceiveFragmentDirections.actionReceiveFragmentToLunoApiFragment()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun receiveAndObserveReceive() {
        this.receiveViewModel.receive(arguments?.getString("asset") ?: "", ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)
        this.receiveViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayReceiveOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        this.binding.addressEditText.setText(data.first().address)
                        this.binding.qrAddressImageView.setImageBitmap(createQRCode(data.first().qrCodeUri, this.binding.qrAddressImageView.width, this.binding.qrAddressImageView.height))

                        addBtnCopyListener()
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun addBtnCopyListener() {
        this.binding.copyImageButton.setOnClickListener { context?.let { context -> copyToClipBoard(context, this.binding.addressEditText.text.toString()) } }
    }

    private fun hideAllViews() {
        this.binding.copyImageButton.visibility = View.GONE
        this.binding.addressEditText.visibility = View.GONE
        this.binding.donateTextView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
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
    }
}