package com.mkj.oauth2demo;

import android.content.Context;
import android.util.Log;

import com.mkj.oauth2demo.listeners.WebServiceListener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class WebServiceFactory {
    private Context mContext;
    private WebServiceListener mWebServiceListener;
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    OkHttpClient client = new OkHttpClient();

    public WebServiceFactory(Context mContext) {
        this.mContext = mContext;
    }

    public void setWebServiceListener(WebServiceListener mWebServiceListener) {
        this.mWebServiceListener = mWebServiceListener;
    }

    public void authenticateService(String url, String json) {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                call.cancel();
                mWebServiceListener.onError("Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                final String myResponse = response.body().string();
                mWebServiceListener.onSuccess(myResponse);
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
