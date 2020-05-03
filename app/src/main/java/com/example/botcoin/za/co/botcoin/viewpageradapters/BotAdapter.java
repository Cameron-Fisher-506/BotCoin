package com.example.botcoin.za.co.botcoin.viewpageradapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.botcoin.R;
import com.example.botcoin.za.co.botcoin.objs.Trade;

import java.util.List;

public class BotAdapter extends RecyclerView.Adapter<BotAdapter.BotViewHolder>
{
    private Context context;
    List<Trade> trades;

    public BotAdapter(Context context, List<Trade> trades)
    {
        this.context = context;
        this.trades = trades;
    }

    @NonNull
    @Override
    public BotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(this.context).inflate(R.layout.frag_bot_trade, parent, false);
        return new BotViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BotViewHolder holder, int position)
    {
        Trade trade = this.trades.get(position);

        holder.btnTrade.setText(trade.getType());
        //holder.txtAmount.setText(trade.getAmount());
        holder.txtPrice.setText(trade.getPrice());

    }

    @Override
    public int getItemCount() {
        return this.trades.size();
    }

    public class BotViewHolder extends RecyclerView.ViewHolder{

        public Button btnTrade;
        public EditText txtAmount;
        public EditText txtPrice;

        public BotViewHolder(View view)
        {
            super(view);

            this.btnTrade = view.findViewById(R.id.btnTrade);
            this.txtAmount = view.findViewById(R.id.txtAmount);
            this.txtPrice = view.findViewById(R.id.txtPrice);
        }
    }
}
