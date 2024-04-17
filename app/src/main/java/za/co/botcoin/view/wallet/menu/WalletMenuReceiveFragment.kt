package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.state.ServiceState
import za.co.botcoin.utils.ConstantUtils.USER_KEY_ID
import za.co.botcoin.utils.ConstantUtils.USER_SECRET_KEY
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.utils.services.clipBoardService.BaseClipBoardService.copyToClipBoard
import za.co.botcoin.view.wallet.WalletBaseFragment

class WalletMenuReceiveFragment : WalletBaseFragment(R.layout.receive_fragment) {
    private lateinit var binding: ReceiveFragmentBinding
    private val walletMenuReceiveViewModel by viewModels<WalletMenuReceiveViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)
        setUpOnClickListeners()
        if (isApiKeySet(context)) {
            receiveAndObserveReceive()
        } else {
            walletViewModel.displayLunoApiCredentialsAlertDialog()
            Navigation.findNavController(view)
                .navigate(WalletMenuReceiveFragmentDirections.actionReceiveFragmentToLunoApiFragment())
        }
    }

    private fun receiveAndObserveReceive() {
        this.walletMenuReceiveViewModel.receive(walletViewModel.selectedAsset, USER_KEY_ID, USER_SECRET_KEY)
        this.walletMenuReceiveViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.serviceState) {
                ServiceState.Success -> {
                    walletActivity.dismissProgressBar()
                    displayWalletMenuReceiveOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        binding.addressCustomInputView.setText(data.first().address)
                        binding.qrAddressImageView.setImageBitmap(createQRCode(data.first().qrCodeUri, binding.qrAddressImageView.width, binding.qrAddressImageView.height))

                        setUpOnClickListeners()
                    } else {
                        displayErrorMessage()
                    }
                }
                ServiceState.Error -> {
                    walletActivity.dismissProgressBar()
                    displayErrorMessage()
                }
                ServiceState.Loading -> {
                    walletActivity.displayProgressBar()
                    hideWalletMenuReceiveOptions()
                    hideErrorMessage()
                }
            }
        }
    }

    private fun setUpOnClickListeners() = with(binding) {
        receiveInformationView.setOnClickListener {
            walletMenuReceiveViewModel.displayReceiveDescriptionAlertDialog()
        }
        copyImageButton.setOnClickListener {
            context?.let { context -> copyToClipBoard(context, addressCustomInputView.getText()) }
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