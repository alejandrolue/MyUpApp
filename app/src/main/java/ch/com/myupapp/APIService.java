package ch.com.myupapp;

import android.app.Service;
import android.content.Context;

import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class APIService extends Service {

    // Binder given to clients
    private final IBinder binder = new LocalBinder();

    public class LocalBinder extends Binder {
        APIService getService() {
            return APIService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }


    public void doRequest(String url, final VolleyCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonArrayRequest jsonObjectRequest = new JsonArrayRequest
                (Request.Method.GET, url, null,
                        // Call onSuccess after request returns successfully
                        callback::onSuccess,
                        // Print the error, if there is one, to the console
                        System.out::println){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<>();
                params.put("AUTHORIZATION", "Token 5609d30fe0fdf95d3d9661d9c7e1e051a14fb783");

                return params;
            }
        };

        queue.add(jsonObjectRequest);
    }
}