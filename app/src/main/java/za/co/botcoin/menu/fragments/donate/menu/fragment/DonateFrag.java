package za.co.botcoin.menu.fragments.donate.menu.fragment;

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
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import za.co.botcoin.R;
import za.co.botcoin.utils.ClipBoardUtils;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.FragmentUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;
import za.co.botcoin.MainActivity;
import za.co.botcoin.menu.fragments.LunoApiFrag;

import org.json.JSONArray;
import org.json.JSONObject;

import static android.content.Context.NOTIFICATION_SERVICE;

public class DonateFrag extends Fragment implements WSCallUtilsCallBack
{
    private final int REQ_CODE_FUNDING_ADDRESS = 101;
    private final int REQ_CODE_SEND = 102;

    private EditText edTxtAddress;
    private EditText edTxtTag;

    private Button btnCopy;
    private Button btnCopyTag;


    private ImageView imgQRAddress;
    private EditText edTxtAmount;
    private Button btnDonate;

    private String address;
    private String tag;
    private String qrCode;
    private String asset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_donate, container, false);

        initUI(view);

        if(GeneralUtils.isApiKeySet(getContext()))
        {
            getBotCoinAccountDetails();
        }else
        {
            GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();

            LunoApiFrag lunoApiFrag = new LunoApiFrag();
            FragmentUtils.startFragment(((MainActivity)getActivity()).getSupportFragmentManager(), lunoApiFrag, R.id.fragContainer, ((MainActivity)getActivity()).getSupportActionBar(), "Luno API",true, false, true, null);
        }


        return view;
    }


    private void initUI(View view)
    {
        this.asset = getArguments().getString("asset");

        this.edTxtAddress =  view.findViewById(R.id.edTxtAddress);
        this.edTxtTag = view.findViewById(R.id.edTxtTag);
        this.btnCopy =  view.findViewById(R.id.btnCopy);
        this.btnCopyTag = view.findViewById(R.id.btnCopyTag);
        this.imgQRAddress = view.findViewById(R.id.imgQRAddress);
        this.edTxtAmount = view.findViewById(R.id.edTxtAmount);
        this.btnDonate = view.findViewById(R.id.btnDonate);

        this.address = null;
        this.tag = null;

        addBtnCopyListener(view.findViewById(R.id.btnCopy));
        addBtnDonateListener(view.findViewById(R.id.btnDonate));
        addBtnCopyTagListener(view.findViewById(R.id.btnCopyTag));
    }

    private void getBotCoinAccountDetails()
    {
        WSCallsUtils.get(this, REQ_CODE_FUNDING_ADDRESS, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_FUNDING_ADDRESS + "?asset=" + this.asset, GeneralUtils.getAuth(ConstantUtils.KEY_ID, ConstantUtils.SECRET_KEY));
    }

    private void addBtnCopyListener(View view)
    {
        this.btnCopy = (Button) view;
        this.btnCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipBoardUtils.copyToClipBoard(getActivity(), address);
            }
        });
    }

    private void addBtnCopyTagListener(View view)
    {
        this.btnCopyTag = (Button) view;
        this.btnCopyTag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipBoardUtils.copyToClipBoard(getActivity(), tag);
            }
        });
    }

    private void addBtnDonateListener(View view)
    {
        this.btnDonate = (Button) view;
        this.btnDonate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edTxtAmount.getText() != null && !edTxtAmount.getText().toString().equals(""))
                {
                    if(!edTxtAmount.getText().toString().equals("0"))
                    {
                        send(edTxtAmount.getText().toString(), address, tag);
                    }else
                    {
                        GeneralUtils.createAlertDialog(getContext(), "Invalid amount entered!", "Please note that you cannot donate 0 " + asset + ".", false).show();
                    }

                }else
                {
                    GeneralUtils.createAlertDialog(getContext(), "No amount entered!", "Please enter the amount of " + asset + " You would like to donate.", false).show();
                }
            }
        });
    }

    private void send(String amount, String address, String tag)
    {
        WSCallsUtils.post(this,REQ_CODE_SEND, StringUtils.GLOBAL_LUNO_URL + GeneralUtils.buildSend(amount, this.asset, address, tag), "", GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == REQ_CODE_FUNDING_ADDRESS)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null && jsonObject.has("error"))
                    {
                        GeneralUtils.createAlertDialog(getActivity().getApplicationContext(), "Oops!", jsonObject.getString("error"), false);
                    }else
                    {
                        JSONArray address_meta = jsonObject.getJSONArray("address_meta");
                        if(address_meta != null && address_meta.length() > 0)
                        {
                            for(int i = 0; i < address_meta.length(); i++)
                            {
                                JSONObject jsonObjectAddressMeta = address_meta.getJSONObject(i);


                                if(jsonObjectAddressMeta.getString("label").equals("Address"))
                                {
                                    this.address = jsonObjectAddressMeta.getString("value");
                                }

                                if(jsonObjectAddressMeta.getString("label").equals(this.asset + " Tag"))
                                {
                                    this.tag = jsonObjectAddressMeta.getString("value");
                                }
                            }

                        }

                        this.edTxtAddress.setText(this.address);

                        if(this.tag != null)
                        {
                            this.edTxtTag.setText(this.tag);
                        }else
                        {
                            this.edTxtTag.setVisibility(View.INVISIBLE);
                            this.btnCopyTag.setVisibility(View.INVISIBLE);
                        }


                        this.qrCode = jsonObject.getString("qr_code_uri");
                        this.imgQRAddress.setImageBitmap(GeneralUtils.createQRCode(this.qrCode, this.imgQRAddress.getWidth(), this.imgQRAddress.getHeight()));
                    }

                }catch (Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: DonateFrag - taskCompleted"
                            + "\nRequest Code: " + reqCode
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }

            if(reqCode == REQ_CODE_SEND)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null)
                    {
                        notify("Donated " + this.edTxtAmount.getText().toString() + " " + this.asset + ".", jsonObject.toString());
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
