package com.samuelprashker.altcoinwidget;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public class RemoteHelper {

    public static JSONObject getJSONObject(String url) throws Exception {
        return new JSONObject(getString(url));
    }

    public static JSONArray getJSONArray(String url) throws Exception {
        return new JSONArray(getString(url));
    }

    @SuppressWarnings("deprecation")
    public static String getString(String url) throws Exception {
        OkHttpClient client = new OkHttpClient();
        client.setHostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });
        client.setFollowRedirects(true);
        Request request = new Request.Builder()
                .addHeader("User-Agent", "curl/7.43.0")
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

}
