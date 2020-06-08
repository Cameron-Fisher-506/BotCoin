package com.example.botcoin.za.co.botcoin.wallet.fragments.wallet.menu.fragment;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.utils.ConstantUtils;
import com.example.botcoin.za.co.botcoin.utils.GeneralUtils;
import com.example.botcoin.za.co.botcoin.utils.StringUtils;
import com.example.botcoin.za.co.botcoin.utils.WSCallUtilsCallBack;
import com.example.botcoin.za.co.botcoin.utils.WSCallsUtils;

import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;

public class SendFrag extends Fragment implements WSCallUtilsCallBack
{
    private final int REQ_CODE_SEND = 101;

    private EditText edTxtAmount;
    private EditText edTxtAddress;
    private EditText edTxtTag;

    private Button btnSend;

    private String asset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_send, container, false);

        initUI(view);

        addBtnSend(view);

        return view;
    }

    private void addBtnSend(View view)
    {
        this.btnSend = view.findViewById(R.id.btnSend);
        this.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!edTxtAmount.getText().toString().equals("") && !edTxtAddress.getText().toString().equals(""))
                {
                    if(!edTxtAmount.getText().toString().equals("0"))
                    {
                        send(edTxtAmount.getText().toString(), edTxtAddress.getText().toString(), edTxtTag.getText().toString().equals("") ? null : edTxtTag.getText().toString());
                    }else
                    {
                        GeneralUtils.createAlertDialog(getContext(), "Invalid amount entered!", "Please note that you cannot send 0 " + asset + ".", false).show();
                    }

                }else
                {
                    GeneralUtils.createAlertDialog(getContext(), "Send", "Please enter the amount of " + asset + " You would like to send. Please enter a valid recipient account address and tag.", false).show();
                }

            }
        });
    }

    private void send(String amount, String address, String tag)
    {
        WSCallsUtils.post(this,REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + GeneralUtils.buildSend(amount, this.asset, address, tag), "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    private void initUI(View view)
    {
        this.edTxtAddress = view.findViewById(R.id.edTxtAddress);
        this.edTxtAmount = view.findViewById(R.id.edTxtAmount);
        this.edTxtTag = view.findViewById(R.id.edTxtTag);

        this.asset = getArguments().getString("asset");
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == REQ_CODE_SEND)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null)
                    {
                        notify("Sent " + this.edTxtAmount.getText().toString() + " " + this.asset + " to " + this.edTxtAddress + ".", jsonObject.toString());
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: DonateFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {

        }
    }

    public void notify(String title, String message)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,intent,0);
            Notification notification = new Notification.Builder(getContext())
                    .setTicker(title)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .addAction(R.drawable.luno_icon,"Action 1", pendingIntent)
                    .setContentIntent(pendingIntent).getNotification();

            notification.flags = Notification.FLAG_AUTO_CANCEL;

            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            Intent intent = new Intent();
            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0,intent,0);

            Notification notification  = new Notification.Builder(getContext())
                    .setContentTitle(title)
                    .setContentText(message)
                    .setSmallIcon(R.drawable.botcoin)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                    .setAutoCancel(true)
                    .build();

            NotificationManager notificationManager = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(0, notification);
        }

    }
}
