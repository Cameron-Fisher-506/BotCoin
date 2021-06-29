package za.co.botcoin.view.wallet.menu

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.json.JSONObject
import za.co.botcoin.R
import za.co.botcoin.databinding.OrdersFragmentBinding
import za.co.botcoin.model.models.Order
import za.co.botcoin.utils.ConstantUtils
import za.co.botcoin.utils.GeneralUtils.createAlertDialog
import za.co.botcoin.utils.GeneralUtils.getAuth
import za.co.botcoin.utils.GeneralUtils.getCurrentDateTime
import za.co.botcoin.utils.StringUtils
import za.co.botcoin.utils.WSCallUtilsCallBack
import za.co.botcoin.utils.WSCallsUtils
import java.util.*

class OrdersFrag : Fragment(R.layout.orders_fragment), WSCallUtilsCallBack {
    private lateinit var binding: OrdersFragmentBinding
    private val REQ_CODE_ORDERS = 101
    private val REQ_CODE_STOP_ORDER = 102

    var orders: List<Order>? = null
    var orderAdapter: OrderAdapter? = null
        private set


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        this.binding = OrdersFragmentBinding.bind(view)

        getListOrders()
    }

    private fun getListOrders() {
        WSCallsUtils.get(this, REQ_CODE_ORDERS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_LISTORDERS, getAuth(ConstantUtils.USER_KEY_ID ?: "", ConstantUtils.USER_SECRET_KEY ?: ""))
    }

    private fun cancelOrder(idOrder: String) {
        createAlertDialog(context, "Cancel Order", "Are you sure you would like to cancel your order?", true)!!.show()
        val url = StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_STOP_ORDER + "?order_id=" + idOrder
        WSCallsUtils.post(this, REQ_CODE_STOP_ORDER, url, "", getAuth(ConstantUtils.USER_KEY_ID!!, ConstantUtils.USER_SECRET_KEY!!))
    }

    inner class OrderAdapter(private val orders: List<Order>) : BaseAdapter() {
        private var txtType: TextView? = null
        private var txtState: TextView? = null
        private var txtLimitPrice: TextView? = null
        private var txtLimitVolume: TextView? = null
        private var txtPair: TextView? = null
        private var txtCreatedTime: TextView? = null
        private var txtCompletedTime: TextView? = null
        private var btnCancel: Button? = null
        override fun getCount(): Int {
            return this.orders.size
        }

        override fun getItem(position: Int): Any {
            return this.orders[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
            var convertView = convertView
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.order_list_item, null)
            }
            initUI(convertView)
            wireUI(position)
            return convertView
        }

        private fun initUI(view: View) {
            txtType = view.findViewById(R.id.txtType)
            txtState = view.findViewById(R.id.txtState)
            txtLimitPrice = view.findViewById(R.id.txtLimitPrice)
            txtLimitVolume = view.findViewById(R.id.txtLimitVolume)
            txtPair = view.findViewById(R.id.txtPair)
            txtCreatedTime = view.findViewById(R.id.txtCreatedTime)
            txtCompletedTime = view.findViewById(R.id.txtCompletedTime)
            btnCancel = view.findViewById(R.id.btnCancel)
        }

        private fun wireUI(position: Int) {
            txtType!!.text = this.orders[position].type
            txtState!!.text = this.orders[position].state
            txtLimitPrice!!.text = this.orders[position].limitPrice
            txtLimitVolume!!.text = this.orders[position].limitVolume
            txtPair!!.text = this.orders[position].pair
            txtCreatedTime!!.text = this.orders[position].createdTime
            txtCompletedTime!!.text = this.orders[position].completedTime
            if (txtState!!.text.toString() != "COMPLETE") {
                btnCancel!!.visibility = View.VISIBLE
                btnCancel!!.setOnClickListener {
                    cancelOrder(orders[position].id)
                    getListOrders()
                }
            } else {
                btnCancel!!.visibility = View.INVISIBLE
            }
        }
    }

    override fun taskCompleted(response: String?, reqCode: Int) {
        if (response != null) {
            if (reqCode == REQ_CODE_ORDERS) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null && jsonObject.has("orders")) {
                        val jsonObjectOrders = jsonObject.getJSONArray("orders")
                        if (jsonObjectOrders != null && jsonObjectOrders.length() > 0) {
                            val orders: MutableList<Order> = ArrayList()
                            for (i in 0 until jsonObjectOrders.length()) {
                                val jsonObjectOrder = jsonObjectOrders.getJSONObject(i)
                                val id = jsonObjectOrder.getString("order_id")
                                val type = jsonObjectOrder.getString("type")
                                val pair = jsonObjectOrder.getString("pair")
                                val state = jsonObjectOrder.getString("state")
                                val limitPrice = jsonObjectOrder.getString("limit_price")
                                val limitVolume = jsonObjectOrder.getString("limit_volume")
                                val createdTime = jsonObjectOrder.getString("creation_timestamp")
                                val completedTime = jsonObjectOrder.getString("completed_timestamp")
                                orders.add(Order(id, type, state, limitPrice, limitVolume, pair, createdTime, completedTime))
                            }
                            orderAdapter = OrderAdapter(orders)
                            this.binding.lvOrders.adapter = orderAdapter
                        }
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "Error: ${e.message} " +
                            "Method: OrdersFrag - taskCompleted " +
                            "Request Code: $reqCode " +
                            "CreatedTime: ${getCurrentDateTime()}")
                }
            }
            if (reqCode == REQ_CODE_STOP_ORDER) {
                try {
                    val jsonObject = JSONObject(response)
                    if (jsonObject != null) {
                        notify("Order Cancelled", jsonObject.toString())
                    }
                } catch (e: Exception) {
                    Log.e(ConstantUtils.BOTCOIN_TAG, """
     
     Error: ${e.message}
     Method: OrdersFrag - taskCompleted
     Request Code: $reqCode
     CreatedTime: ${getCurrentDateTime()}
     """.trimIndent())
                }
            }
        } else {
        }
    }

    fun notify(title: String?, message: String?) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = Notification.Builder(context)
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon, "Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).notification
            notification.flags = Notification.FLAG_AUTO_CANCEL
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            val intent = Intent()
            val pendingIntent = PendingIntent.getActivity(context, 0, intent, 0)
            val notification = Notification.Builder(context)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND or Notification.DEFAULT_LIGHTS or Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build()
            val notificationManager = context!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.notify(0, notification)
        }
    }
}