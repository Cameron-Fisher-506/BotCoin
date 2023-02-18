package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.services.clipBoardService.BaseClipBoardService.copyToClipBoard
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.view.wallet.WalletBaseFragment

class WalletMenuReceiveFragment : WalletBaseFragment(R.layout.receive_fragment) {
    private lateinit var binding: ReceiveFragmentBinding
    private val walletMenuReceiveViewModel by viewModels<WalletMenuReceiveViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)

        binding.receiveInformationView.setOnClickListener {
            walletMenuReceiveViewModel.displayReceiveDescriptionAlertDialog()
        }

        if (isApiKeySet(context)) {
            receiveAndObserveReceive()
        } else {
            walletViewModel.displayLunoApiCredentialsAlertDialog()
            Navigation.findNavController(view).navigate(WalletMenuReceiveFragmentDirections.actionReceiveFragmentToLunoApiFragment())
        }
    }

    private fun receiveAndObserveReceive() {
        this.walletMenuReceiveViewModel.receive(arguments?.getString("asset") ?: "", ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY)
        this.walletMenuReceiveViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayWalletMenuReceiveOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        this.binding.addressEditText.setText(data.first().address)
                        this.binding.qrAddressImageView.setImageBitmap(
                            createQRCode(
                                data.first().qrCodeUri,
                                this.binding.qrAddressImageView.width,
                                this.binding.qrAddressImageView.height
                            )
                        )

                        setUpOnClickListeners()
                    } else {
                        displayErrorMessage()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayErrorMessage()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    hideWalletMenuReceiveOptions()
                    hideErrorMessage()
                }
            }
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.copyImageButton.setOnClickListener {
            context?.let { context -> copyToClipBoard(context, this.binding.addressEditText.text.toString()) }
        }
    }

    private fun hideWalletMenuReceiveOptions() {
        this.binding.walletMenuReceiveGroup.visibility = View.GONE
    }

    private fun displayWalletMenuReceiveOptions() {
        hideErrorMessage()
        this.binding.walletMenuReceiveGroup.visibility = View.VISIBLE
    }

    private fun displayErrorMessage() {
        hideWalletMenuReceiveOptions()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        this.binding.errorTextView.visibility = View.GONE
    }
}