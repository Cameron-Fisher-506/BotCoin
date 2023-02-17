package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProviders
import za.co.botcoin.R
import za.co.botcoin.databinding.SendFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.view.wallet.WalletBaseFragment

class WalletMenuSendFragment : WalletBaseFragment(R.layout.send_fragment) {
    private lateinit var binding: SendFragmentBinding
    private val walletMenuSendViewModel by viewModels<WalletMenuSendViewModel>(factoryProducer = { walletActivity.getViewModelFactory })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SendFragmentBinding.bind(view)

        setUpOnClickListeners()
        arguments?.let {
            addBtnSend(it.getString("asset") ?: "")
        }

    }

    private fun setUpOnClickListeners() {
        binding.sendInformationView.setOnClickListener {
            walletMenuSendViewModel.displaySendDescriptionAlertDialog()
        }
    }

    private fun addBtnSend(asset: String) {
        this.binding.sendButton.setOnClickListener {
            val amount = this.binding.amountEditText.text.toString()
            val address = this.binding.addressEditText.text.toString()
            val destinationTag = this.binding.tagEditText.text.toString()
            if (amount.isNotBlank() && address.isNotBlank()) {
                if (amount != "0") {
                    sendAndObserveSend(amount, asset, address, destinationTag)
                } else {
                    createAlertDialog(context, "Invalid amount entered!", "Please note that you cannot send 0 $asset.", false).show()
                }
            } else {
                createAlertDialog(context, "Send", "Please enter the amount of $asset You would like to send. Please enter a valid recipient account address and tag.", false).show()
            }
        }
    }

    private fun sendAndObserveSend(amount: String, asset: String, address: String, destinationTag: String) {
        this.walletMenuSendViewModel.send(amount, asset, address, destinationTag)
        this.walletMenuSendViewModel.sendResponse.observe(viewLifecycleOwner) {
            when (it.status) {
                Status.SUCCESS -> {
                    walletActivity.dismissProgressBar()
                    displayWalletMenuSendOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { response ->
                            if (response.success) {
                                GeneralUtils.notify(context, "Sent $amount $asset to $address.", response.withdrawalId)
                            } else {
                                walletMenuSendViewModel.displaySendFailedNotification()
                            }
                        }
                    } else {
                        walletMenuSendViewModel.displaySendFailedNotification()
                    }
                }
                Status.ERROR -> {
                    walletActivity.dismissProgressBar()
                    displayWalletMenuSendOptions()
                    walletMenuSendViewModel.displaySendFailedNotification()
                }
                Status.LOADING -> {
                    walletActivity.displayProgressBar()
                    hideWalletMenuSendOptions()
                }
            }
        }
    }

    private fun hideWalletMenuSendOptions() {
        this.binding.walletMenuSendGroup.visibility = View.GONE
    }

    private fun displayWalletMenuSendOptions() {
        this.binding.walletMenuSendGroup.visibility = View.VISIBLE
    }
}