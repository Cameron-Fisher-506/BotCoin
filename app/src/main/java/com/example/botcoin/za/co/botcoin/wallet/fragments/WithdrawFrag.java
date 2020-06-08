package com.example.botcoin.za.co.botcoin.wallet.fragments;

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

public class WithdrawFrag extends Fragment implements WSCallUtilsCallBack
{
    private final int REQ_CODE_WITHDRAW = 101;

    private EditText edTxtAmount;
    private EditText edTxtBeneficiaryId;
    private Button btnWithdraw;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_withdraw, container, false);

        initUI(view);
        addWithdrawListener(view);

        return view;
    }

    private void initUI(View view)
    {
        this.edTxtAmount = view.findViewById(R.id.edTxtAmount);
        this.edTxtBeneficiaryId = view.findViewById(R.id.edTxtBeneficiaryId);
        this.btnWithdraw = view.findViewById(R.id.btnWithdraw);
    }

    private void addWithdrawListener(View view)
    {
        this.btnWithdraw = view.findViewById(R.id.btnWithdraw);
        this.btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!edTxtAmount.getText().toString().equals("") && !edTxtAmount.getText().toString().equals("0") && !edTxtBeneficiaryId.getText().toString().equals(""))
                {
                    if(GeneralUtils.isApiKeySet(getContext()))
                    {
                        withdrawal(edTxtAmount.getText().toString(), edTxtBeneficiaryId.getText().toString());
                    }else
                    {
                        GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();
                    }
                }else
                {
                    GeneralUtils.createAlertDialog(getActivity(),"Withdrawal","Please provide an amount more than 0 and a valid beneficiary ID!", false).show();
                }
            }
        });
    }

    private void withdrawal(String amount, String beneficiaryId)
    {
        WSCallsUtils.post(this, REQ_CODE_WITHDRAW, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_WITHDRAWALS + GeneralUtils.buildWithdrawal(amount, beneficiaryId), "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == REQ_CODE_WITHDRAW)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null)
                    {
                        notify("Withdrew " + this.edTxtAmount.getText().toString() + " Rands.", jsonObject.toString());
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: DonateFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
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
