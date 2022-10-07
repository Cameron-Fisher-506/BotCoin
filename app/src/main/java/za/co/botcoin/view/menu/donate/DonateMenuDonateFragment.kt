package za.co.botcoin.view.menu.donate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.BuildConfig
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.*
import za.co.botcoin.view.menu.MenuBaseFragment

class DonateMenuDonateFragment : MenuBaseFragment(R.layout.donate_fragment) {
    private lateinit var binding: DonateFragmentBinding
    private val donateViewModel by viewModels<DonateMenuDonateViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    private var asset: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateFragmentBinding.bind(view)

        asset = arguments?.getString("asset") ?: ""

        setUpOnClickListeners()
        receiveAndObserveReceive()
    }

    private fun receiveAndObserveReceive() {
        this.donateViewModel.receive(asset, BuildConfig.KEY_ID, BuildConfig.SECRET_KEY)
        this.donateViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    menuActivity.displayProgressBar()
                    displayDonateOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        this.binding.addressEditText.setText(data.first().address)
                        /*if (tagValue != null) {
                            this.binding.edTxtTag.setText(data.first().)
                        } else {
                            this.binding.edTxtTag.visibility = View.INVISIBLE
                            this.binding.btnCopyTag.visibility = View.INVISIBLE
                        }*/
                        this.binding.qrAddressImageView.setImageBitmap(GeneralUtils.createQRCode(data.first().qrCodeUri, this.binding.qrAddressImageView.width, this.binding.qrAddressImageView.height))
                    } else {
                        displayErrorTextView()
                    }
                }
                Status.ERROR -> {
                    menuActivity.displayProgressBar()
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    menuActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun sendAndObserveSend(amount: String, asset: String, address: String, destinationTag: String) {
        this.donateViewModel.send(amount, asset, address, destinationTag)
        this.donateViewModel.sendResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    menuActivity.dismissProgressBar()
                    displayDonateOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { response ->
                            if (response.success) {
                                donateViewModel.displaySentAmountOfAssetToAddressNotification(amount, asset, address)
                            } else {
                                donateViewModel.displaySendFailedNotification()
                            }
                        }
                    } else {
                        donateViewModel.displaySendFailedNotification()
                    }
                }
                Status.ERROR -> {
                    menuActivity.dismissProgressBar()
                    displayDonateOptions()
                    donateViewModel.displaySendFailedNotification()
                }
                Status.LOADING -> {
                    menuActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun setUpOnClickListeners() {
        this.binding.copyImageButton.setOnClickListener {
            donateViewModel.copyToClipBoard(this.binding.addressEditText.text.toString())
        }
        this.binding.copyTagImageButton.setOnClickListener {
            donateViewModel.copyToClipBoard(this.binding.tagEditText.text.toString())
        }
        this.binding.donateButton.setOnClickListener {
            if (GeneralUtils.isApiKeySet(context)) {
                val amount: String = this.binding.amountEditText.text.toString()
                val address: String = this.binding.addressEditText.text.toString()
                val destinationTag: String = this.binding.tagEditText.text.toString()
                if (amount.isNotBlank() && amount != "0") {
                    sendAndObserveSend(amount, asset, address, destinationTag)
                } else {
                    donateViewModel.displayInvalidAmountEnteredAlertDialog(asset)
                }
            } else {
                donateViewModel.displayLunoApiCredentialsAlertDialog()
            }
        }
    }

    private fun hideAllViews() {
        this.binding.copyImageButton.visibility = View.GONE
        this.binding.copyTagImageButton.visibility = View.GONE
        this.binding.donateButton.visibility = View.GONE
        this.binding.addressEditText.visibility = View.GONE
        this.binding.amountEditText.visibility = View.GONE
        this.binding.tagEditText.visibility = View.GONE
        this.binding.donateTextView.visibility = View.GONE
        this.binding.errorTextView.visibility = View.GONE
    }

    private fun displayDonateOptions() {
        hideAllViews()
        this.binding.copyImageButton.visibility = View.VISIBLE
        this.binding.copyTagImageButton.visibility = View.VISIBLE
        this.binding.donateButton.visibility = View.VISIBLE
        this.binding.addressEditText.visibility = View.VISIBLE
        this.binding.amountEditText.visibility = View.VISIBLE
        this.binding.tagEditText.visibility = View.VISIBLE
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