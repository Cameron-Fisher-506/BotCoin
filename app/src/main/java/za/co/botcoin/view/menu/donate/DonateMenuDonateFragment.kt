package za.co.botcoin.view.menu.donate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.BuildConfig
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateFragmentBinding
import za.co.botcoin.state.ServiceState
import za.co.botcoin.utils.*
import za.co.botcoin.view.menu.MenuBaseFragment

class DonateMenuDonateFragment : MenuBaseFragment(R.layout.donate_fragment) {
    private lateinit var binding: DonateFragmentBinding
    private val donateMenuDonateViewModel by viewModels<DonateMenuDonateViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateFragmentBinding.bind(view)
        setUpOnClickListeners()
        receiveAndObserveReceive()
    }

    private fun receiveAndObserveReceive() {
        this.donateMenuDonateViewModel.receive(menuViewModel.selectedDonateAsset, BuildConfig.KEY_ID, BuildConfig.SECRET_KEY)
        this.donateMenuDonateViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.serviceState) {
                ServiceState.Success -> {
                    menuActivity.dismissProgressBar()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayDonateOptions()
                        this.binding.addressCustomInputView.setText(data.first().address)
                        /*if (tagValue != null) {
                            this.binding.edTxtTag.setText(data.first().)
                        } else {
                            this.binding.edTxtTag.visibility = View.INVISIBLE
                            this.binding.btnCopyTag.visibility = View.INVISIBLE
                        }*/
                        this.binding.qrAddressImageView.setImageBitmap(GeneralUtils.createQRCode(data.first().qrCodeUri, this.binding.qrAddressImageView.width, this.binding.qrAddressImageView.height))
                    } else {
                        displayErrorMessage()
                    }
                }
                ServiceState.Error -> {
                    menuActivity.dismissProgressBar()
                    displayErrorMessage()
                }
                ServiceState.Loading -> {
                    menuActivity.displayProgressBar()
                    hideDonateOptions()
                    hideErrorMessage()
                }
            }
        }
    }

    private fun sendAndObserveSend(amount: String, asset: String, address: String, destinationTag: String) {
        this.donateMenuDonateViewModel.send(amount, asset, address, destinationTag)
        this.donateMenuDonateViewModel.sendResponse.observe(viewLifecycleOwner) {
            when (it.serviceState) {
                ServiceState.Success -> {
                    menuActivity.dismissProgressBar()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        displayDonateOptions()
                        data.map { response ->
                            if (response.success) {
                                donateMenuDonateViewModel.displaySentAmountOfAssetToAddressNotification(amount, asset, address)
                            } else {
                                donateMenuDonateViewModel.displaySendFailedNotification()
                            }
                        }
                    } else {
                        displayErrorMessage()
                        donateMenuDonateViewModel.displaySendFailedNotification()
                    }
                }
                ServiceState.Error -> {
                    menuActivity.dismissProgressBar()
                    displayErrorMessage()
                    donateMenuDonateViewModel.displaySendFailedNotification()
                }
                ServiceState.Loading -> {
                    menuActivity.displayProgressBar()
                    hideDonateOptions()
                    hideErrorMessage()
                }
            }
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.copyImageButton.setOnClickListener {
            donateMenuDonateViewModel.copyToClipBoard(this.binding.addressCustomInputView.getText())
        }
        this.binding.copyTagImageButton.setOnClickListener {
            donateMenuDonateViewModel.copyToClipBoard(this.binding.tagCustomInputView.getText())
        }
        this.binding.donateButton.setOnClickListener {
            if (GeneralUtils.isApiKeySet(context)) {
                val amount: String = this.binding.amountCustomInputView.getText()
                val address: String = this.binding.addressCustomInputView.getText()
                val destinationTag: String = this.binding.tagCustomInputView.getText()
                if (amount.isNotBlank() && amount != "0") {
                    sendAndObserveSend(amount, menuViewModel.selectedDonateAsset, address, destinationTag)
                } else {
                    donateMenuDonateViewModel.displayInvalidAmountEnteredAlertDialog(menuViewModel.selectedDonateAsset)
                }
            } else {
                donateMenuDonateViewModel.displayLunoApiCredentialsAlertDialog()
            }
        }
        binding.donateInformationView.setOnClickListener {
            donateMenuDonateViewModel.displayDonateDescriptionAlertDialog()
        }
    }

    private fun hideDonateOptions() {
        this.binding.donateGroup.visibility = View.GONE
    }

    private fun displayDonateOptions() {
        hideErrorMessage()
        this.binding.donateGroup.visibility = View.VISIBLE
    }

    private fun displayErrorMessage() {
        hideDonateOptions()
        this.binding.errorTextView.visibility = View.VISIBLE
    }

    private fun hideErrorMessage() {
        this.binding.errorTextView.visibility = View.GONE
    }
}