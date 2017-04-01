package com.samuelprashker.altcoinwidget;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Locale;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

public enum ETHProvider implements AltcoinInterface {

    KRAKEN(R.array.currencies_krkneth, "krkneth") {
        @Override
        public String getValue(String currencyCode) throws Exception {
            String url = "https://api.kraken.com/0/public/Ticker?pair=ETH%s";
            String key = String.format("XETHZ%s", currencyCode);
            JSONObject obj = RemoteHelper.getJSONObject(String.format(url, currencyCode));
            JSONObject obj2 = obj.getJSONObject("result");

            // May be problematic but I don't get the XETHZUSD format
            // SO just assume there's only 1 key
            String innerkey = obj2.keys().next();

            return (String)obj2.getJSONObject(innerkey).getJSONArray("c").get(0);
        }
    },
    POLONIEX(R.array.currencies_poloeth, "poloeth") {
        @Override
        public String getValue(String currencyCode) throws Exception {
            String url = "https://poloniex.com/public?command=returnTicker";
            String key = String.format("%s_ETH", currencyCode);

            JSONObject obj = RemoteHelper.getJSONObject(url);
            String result = obj.getJSONObject(key).getString("last");
            return result;
        }
    },
    QUADRIGA(R.array.currencies_quadriga, "qdrga") {
        @Override
        public String getValue(String currencyCode) throws Exception {
            String url = "https://api.quadrigacx.com/v2/ticker?book=ETH_%s";
            return RemoteHelper.getJSONObject(String.format(url, currencyCode)).getString("last");
        }
    };

    private final int currencyArrayID;
    private String label;

    ETHProvider(int currencyArrayID, String label) {
        this.currencyArrayID = currencyArrayID;
        this.label = label;
    }

    public abstract String getValue(String currencyCode) throws Exception;

    public int getCurrencies() {
        return currencyArrayID;
    }

    public String getLabel() {
        return label;
    }
}
