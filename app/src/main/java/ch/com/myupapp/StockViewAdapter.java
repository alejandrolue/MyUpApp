package ch.com.myupapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class StockViewAdapter extends RecyclerView.Adapter<StockViewAdapter.ViewHolder> {

    List<Stock> stockList;
    Context context;

    public StockViewAdapter(List<Stock> stockList) {
        if (stockList == null) {
            this.stockList = new ArrayList<>();
        } else {
            this.stockList = stockList;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_item_info, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        context = parent.getContext();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stock stock = stockList.get(position);

        holder.textStock.setText(stock.getTitle());

        holder.arrowButton.setOnClickListener(e -> {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("ticker", stock.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return stockList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textStock;
        Button arrowButton;

        public ViewHolder(View itemView) {
            super(itemView);
            textStock = itemView.findViewById(R.id.company_ticker);
            arrowButton = itemView.findViewById(R.id.btn_goTo);
        }

    }
}


