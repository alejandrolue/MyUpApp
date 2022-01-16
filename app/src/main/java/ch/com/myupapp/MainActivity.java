package ch.com.myupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Toast;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Set<String> tickers;
    APIService mService;
    ArrayList<Stock> stocks = new ArrayList<>();
    RecyclerView recyclerView;
    boolean mBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        tickers = preferences.getStringSet("tickers", new HashSet<>());
        Set<String> copyTickers = new HashSet<>(tickers);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            String ticker = extras.getString("ticker");
            boolean delete = extras.getBoolean("delete");
            if(!delete){
                copyTickers.add(ticker);
                preferences.edit().putStringSet("tickers", copyTickers).apply();
                tickers.add(ticker);
            } else {
                copyTickers.remove(ticker);
                preferences.edit().putStringSet("tickers", copyTickers).apply();
                tickers.remove(ticker);
            }
        }
    }
    private void addStockView(String ticker) {
        if (mBound) {
            getData(ticker);
        } else {
            Toast.makeText(this, "Service isn't bound", Toast.LENGTH_SHORT).show();
        }
    }

    private void getData(String ticker) {
        String url = "https://api.tiingo.com/tiingo/daily/" + ticker + "/prices?startDate=2021-11-20&endDate=2021-11-27&columns=date,close";
        mService.doRequest(url, json -> {
            try {
                Stock stock = new Stock();
                stock.setTitle(ticker);
                stock.setJsonChartData(json);
                stocks.add(stock);

                recyclerView = findViewById(R.id.recyclerView);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setItemAnimator(new DefaultItemAnimator());
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
