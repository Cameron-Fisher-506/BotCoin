package za.co.botcoin.navigation.fragments;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import za.co.botcoin.MainActivity;
import za.co.botcoin.R;
import za.co.botcoin.utils.ConstantUtils;
import za.co.botcoin.utils.GeneralUtils;
import za.co.botcoin.utils.StringUtils;
import za.co.botcoin.utils.WSCallUtilsCallBack;
import za.co.botcoin.utils.WSCallsUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.Timer;
import java.util.TimerTask;

public class HomeFrag extends Fragment implements WSCallUtilsCallBack
{

    private final int TICKERS_REQ_CODE = 101;
    public static final String TITLE = "Home";

    private TimerTask timerTask;
    private Timer timer;

    private TextView txtXrpZar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_home, container, false);

        this.txtXrpZar = (TextView) view.findViewById(R.id.txtXrpZar);

        if(GeneralUtils.isApiKeySet(getContext()))
        {
            this.timerTask = new TimerTask()
            {
                @Override
                public void run()
                {
                    getTickers();
                }
            };

            this.timer = new Timer();
            this.timer.scheduleAtFixedRate(timerTask,0, ConstantUtils.TICKER_RUN_TIME);
        }else
        {
            GeneralUtils.createAlertDialog(getActivity(),"Luno API Credentials","Please set your Luno API credentials in order to use BotCoin!", false).show();
        }

        return view;
    }

    private void getTickers()
    {
        WSCallsUtils.get(this, TICKERS_REQ_CODE, StringUtils.GLOBAL_LUNO_URL + StringUtils.GLOBAL_ENDPOINT_TICKERS, GeneralUtils.getAuth(ConstantUtils.USER_KEY_ID, ConstantUtils.USER_SECRET_KEY));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if(this.timer != null)
        {
            this.timer.cancel();
            this.timer.purge();
        }

    }

    @Override
    public void taskCompleted(String response, int reqCode)
    {
        if(response != null)
        {
            if(reqCode == TICKERS_REQ_CODE)
            {
                try
                {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject != null && jsonObject.has("tickers"))
                    {
                        JSONArray tickers = jsonObject.getJSONArray("tickers");

                        if(tickers != null && tickers.length() > 0)
                        {
                            for(int i = 0; i < tickers.length(); i++)
                            {
                                JSONObject ticker = tickers.getJSONObject(i);

                                String pair = ticker.getString("pair");

                                if(pair.equals(ConstantUtils.PAIR_XRPZAR))
                                {
                                    final String lastTrade = ticker.getString("last_trade");
                                    ((MainActivity)getActivity()).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            txtXrpZar.setText(R.string.XRPZAR);
                                            txtXrpZar.append(lastTrade);
                                        }
                                    });
                                }
                            }
                        }
                    }

                }catch(Exception e)
                {
                    Log.e(ConstantUtils.BOTCOIN_TAG, "\nError: " + e.getMessage()
                            + "\nMethod: HomeFrag - onCreateView"
                            + "\nURL: " + StringUtils.GLOBAL_ENDPOINT_TICKERS
                            + "\nCreatedTime: " + GeneralUtils.getCurrentDateTime());
                }
            }
        }else
        {
            GeneralUtils.createAlertDialog(((MainActivity)getActivity()), "No Signal", "Please check your network connection!", false);
        }



    }
}

