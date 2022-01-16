package ch.com.myupapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;

import com.android.volley.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    Set<String> tickers;

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
}
