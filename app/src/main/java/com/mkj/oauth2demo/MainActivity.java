package com.mkj.oauth2demo;

import androidx.appcompat.app.AppCompatActivity;

import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;
import com.mkj.oauth2demo.listeners.WebServiceListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    OkHttpClient client = new OkHttpClient();
    private String url = "https://reqres.in/api/users/2";
    private String urlAuth = "http://shiksha.cg.nic.in/chirayu/api/v1/Authenticate";
    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private TextView mGetB, mPostB;
    private WebServiceFactory mWebServiceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        mGetB = findViewById(R.id.b_get);
        mPostB = findViewById(R.id.b_post);

        mGetB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    run();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        mPostB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*try {
                    JsonObject root = new JsonObject();
                    root.addProperty("ApplicationID", 141);
                    root.addProperty("ClientSecret", "rtY7895fghy");
                    //runAuth(urlAuth, root.toString());
                    mWebServiceFactory.authenticateService(urlAuth, root.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                JsonObject root = new JsonObject();
                root.addProperty("ApplicationID", 141);
                root.addProperty("ClientSecret", "rtY7895fghy");
                mWebServiceFactory.authenticateService(urlAuth, root.toString());
            }
        });

        mWebServiceFactory = new WebServiceFactory(this);
        mWebServiceFactory.setWebServiceListener(mWebServiceListener);


    }

    private WebServiceListener mWebServiceListener = new WebServiceListener() {
        @Override
        public void onSuccess(String arg) {
            Log.d(TAG, "Resp: "+arg);
        }

        @Override
        public void onError(String error) {
            Log.d(TAG, "Error: "+error);
        }
    };

    String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String get(String url) throws IOException {
        Request request = new Request.Builder().url(url).get().build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    void runAuth(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                Log.d(TAG, "" + myResponse);
                /*MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, ""+myResponse);
                    }
                });*/

            }
        });
    }

    void run() throws IOException {

        OkHttpClient client = new OkHttpClient();

        final Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    final String myResponse = response.body().string();
                    Log.d(TAG, "" + myResponse);
                }
                /*MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, ""+myResponse);
                    }
                });*/

            }
        });
    }


}
