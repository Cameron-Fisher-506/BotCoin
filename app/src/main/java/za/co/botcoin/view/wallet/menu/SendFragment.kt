package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import za.co.botcoin.R
import za.co.botcoin.databinding.SendFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.model.repository.send.SendViewModel
import za.co.botcoin.utils.GeneralUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.view.wallet.WalletBaseFragment

class SendFragment : WalletBaseFragment(R.layout.send_fragment) {
    private lateinit var binding: SendFragmentBinding
    private lateinit var sendViewModel: SendViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = SendFragmentBinding.bind(view)

        this.sendViewModel = ViewModelProviders.of(this).get(SendViewModel::class.java)

        arguments?.let {
            addBtnSend(it.getString("asset") ?: "")
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
        this.sendViewModel.send(amount, asset, address, destinationTag)
        this.sendViewModel.sendLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    displaySendOptions()
                    val data = it.data
                    if (!data.isNullOrEmpty()) {
                        data.map { response -> if (response.success) GeneralUtils.notify(context,"Sent $amount $asset to $address.", response.withdrawalId) else GeneralUtils.notify(context,"Send failed.", "") }
                    } else {
                        GeneralUtils.notify(context,"Send failed.", "")
                    }
                }
                Status.ERROR -> {
                    displaySendOptions()
                    GeneralUtils.notify(context,"Send failed.", "")
                }
                Status.LOADING -> {
                    displayProgressBar()
                }
            }
        })
    }

    private fun hideAllViews() {
        this.binding.sendTextView.visibility = View.GONE
        this.binding.sendButton.visibility = View.GONE
        this.binding.addressEditText.visibility = View.GONE
        this.binding.amountEditText.visibility = View.GONE
        this.binding.tagEditText.visibility = View.GONE
        this.binding.progressBar.visibility = View.GONE
    }

    private fun displayProgressBar() {
        hideAllViews()
        this.binding.progressBar.visibility = View.VISIBLE
    }

    private fun displaySendOptions() {
        hideAllViews()
        this.binding.sendTextView.visibility = View.VISIBLE
        this.binding.sendButton.visibility = View.VISIBLE
        this.binding.addressEditText.visibility = View.VISIBLE
        this.binding.amountEditText.visibility = View.VISIBLE
        this.binding.tagEditText.visibility = View.VISIBLE
    }
}