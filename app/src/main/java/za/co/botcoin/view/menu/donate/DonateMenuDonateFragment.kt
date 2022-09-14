package za.co.botcoin.view.menu.donate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import za.co.botcoin.R
import za.co.botcoin.databinding.DonateFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.*
import za.co.botcoin.utils.services.ClipBoardService
import za.co.botcoin.view.menu.MenuBaseFragment

class DonateMenuDonateFragment : MenuBaseFragment(R.layout.donate_fragment) {
    private lateinit var binding: DonateFragmentBinding
    private val donateViewModel by viewModels<DonateMenuDonateViewModel>(factoryProducer = { menuActivity.getViewModelFactory })

    private var asset: String = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = DonateFragmentBinding.bind(view)

        asset = arguments?.getString("asset") ?: ""

        wireUI()
        receiveAndObserveReceive()
    }

    private fun receiveAndObserveReceive() {
        this.donateViewModel.receive(asset, ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY)
        this.donateViewModel.receiveResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    menuActivity.dismissProgressBar()
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
                    menuActivity.dismissProgressBar()
                    displayErrorTextView()
                }
                Status.LOADING -> {
                    menuActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun sendAndObserveSend(amount: String, asset: String, address: String) {
        this.donateViewModel.sendResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    menuActivity.dismissProgressBar()
                    displayDonateOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { response -> if (response.success) GeneralUtils.notify(context, "Sent $amount $asset to $address.", response.withdrawalId) else GeneralUtils.notify(context, "Send failed.", "") }
                    } else {
                        GeneralUtils.notify(context, "Send failed.", "")
                    }
                }
                Status.ERROR -> {
                    menuActivity.dismissProgressBar()
                    displayDonateOptions()
                    GeneralUtils.notify(context, "Send failed.", "")
                }
                Status.LOADING -> {
                    menuActivity.displayProgressBar()
                    displayProgressBar()
                }
            }
        }
    }

    private fun wireUI() {
        addBtnCopyListener()
        addBtnDonateListener()
        addBtnCopyTagListener()
    }

    private fun addBtnCopyListener() {
        this.binding.copyImageButton.setOnClickListener { activity?.let { context -> ClipBoardService.copyToClipBoard(context, this.binding.addressEditText.text.toString()) } }
    }

    private fun addBtnCopyTagListener() {
        this.binding.copyTagImageButton.setOnClickListener { activity?.let { context -> ClipBoardService.copyToClipBoard(context, this.binding.tagEditText.text.toString()) } }
    }

    private fun addBtnDonateListener() {
        this.binding.donateButton.setOnClickListener {
            if (GeneralUtils.isApiKeySet(context)) {
                val amount: String = this.binding.amountEditText.text.toString()
                val address: String = this.binding.addressEditText.text.toString()
                val destinationTag: String = this.binding.tagEditText.text.toString()
                if (amount.isNotBlank()) {
                    if (amount != "0") {
                        this.donateViewModel.send(amount, asset, address, destinationTag)
                        sendAndObserveSend(amount, asset, address)
                    } else {
                        GeneralUtils.createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot donate 0 $asset.", false).show()
                    }
                } else {
                    GeneralUtils.createAlertDialog(context, "No amount entered!", "Please enter the amount of $asset You would like to donate.", false).show()
                }
            } else {
                GeneralUtils.createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false).show()
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