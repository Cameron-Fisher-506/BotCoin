package za.co.botcoin.view.wallet.menu

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.ReceiveFragmentBinding
import za.co.botcoin.enum.Status
import za.co.botcoin.view.menu.LunoApiFrag
import za.co.botcoin.utils.ClipBoardUtils.copyToClipBoard
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.createQRCode
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime
import za.co.botcoin.utils.GeneralUtils.isApiKeySet
import za.co.botcoin.utils.StringUtils
import za.co.botcoin.utils.WSCallUtilsCallBack
import za.co.botcoin.utils.WSCallsUtils
import za.co.botcoin.view.wallet.WithdrawalViewModel

class ReceiveFrag : Fragment(R.layout.receive_fragment) {
    private lateinit var binding: ReceiveFragmentBinding
    private lateinit var withdrawalViewModel: WithdrawalViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = ReceiveFragmentBinding.bind(view)

        this.withdrawalViewModel = ViewModelProviders.of(this).get(WithdrawalViewModel::class.java)

        if (isApiKeySet(context)) {
            this.withdrawalViewModel.receive(true, arguments?.getString("asset") ?: "")
            attachReceiveObserver()
        } else {
            createAlertDialog(activity, "Luno API Credentials", "Please set your Luno API credentials in order to use BotCoin!", false)!!.show()

            val action = ReceiveFragDirections.actionReceiveFragToLunoApiFrag()
            Navigation.findNavController(view).navigate(action)
        }
    }

    private fun attachReceiveObserver() {
        this.withdrawalViewModel.receiveLiveData.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    val data = it.data
                    if(!data.isNullOrEmpty()) {
                        this.binding.edTxtAddress.setText(data.first().address)
                        this.binding.imgQRAddress.setImageBitmap(createQRCode(data.first().qrCodeUri, this.binding.imgQRAddress.width, this.binding.imgQRAddress.height))

                        addBtnCopyListener()
                    } else {

                    }
                }
                Status.ERROR -> {}
                Status.LOADING -> {}
            }
        })
    }

    private fun addBtnCopyListener() {
        this.binding.btnCopy.setOnClickListener { context?.let { context -> copyToClipBoard(context, this.binding.edTxtAddress.text.toString()) } }
    }
}