package ch.com.myupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Response;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Set<String> tickers;
    APIService mService;
    ArrayList<Stock> stocks = new ArrayList<>();
    RecyclerView recyclerView;
    boolean mBound = false;
    Button button;
    Button notifyBtn;
    StockViewAdapter stockViewAdapter;

    @SuppressLint("ObsoleteSdkInt")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tickers = preferences.getStringSet("tickers", new HashSet<>());
        Set<String> newTickers = new HashSet<>(tickers);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ticker = extras.getString("ticker");
            boolean delete = extras.getBoolean("delete");
            if(!delete){
                newTickers.add(ticker);
                preferences.edit().putStringSet("tickers", newTickers).apply();
                tickers.add(ticker);
            } else {
                newTickers.remove(ticker);
                preferences.edit().putStringSet("tickers", newTickers).apply();
                tickers.remove(ticker);
            }
        }


        //Notification Funktioniert nicht ganz
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("Up", "Up", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        notifyBtn = findViewById(R.id.test_notification);
        notifyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Notification.Builder builder = new Notification.Builder(MainActivity.this,"Stock Update");
                builder.setContentTitle("UP");
                builder.setContentText("Test Text");
                builder.setSmallIcon(R.mipmap.ic_launcher);
                builder.setAutoCancel(true);

                NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                managerCompat.notify(1, builder.build());
            }
        });



        button = findViewById(R.id.add_stock);
        button.setOnClickListener(e -> {
            Intent intent = new Intent(this, AddStock.class);
            startActivity(intent);
        });
    }

    private void addStockView(String ticker) {
        if (mBound) {
            getData(ticker);
        } else {
            Toast.makeText(this, "Service isn't bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(String ticker) {
        String url = "https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?startDate=2022-01-01&endDate=2022-01-16&columns=date,close";
        mService.doRequest(url, json -> {
            try {
                Stock stock = new Stock();
                stock.setTitle(ticker);
                stock.setJsonChartData(json);
                stocks.add(stock);

                stockViewAdapter = new StockViewAdapter(stocks);

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(stockViewAdapter);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, APIService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unbindService(connection);
        mBound = false;
    }

    private final ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            APIService.LocalBinder binder = (APIService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if(!stocks.isEmpty()) {
                stocks.clear();
            }
            tickers.forEach(ticker -> addStockView(ticker));
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
}
