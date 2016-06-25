package com.samuelprashker.samuelprashker;

import android.content.res.Resources;
import android.text.TextUtils;

import com.samuelprashker.altcoinwidget.BTCProvider;
import com.samuelprashker.altcoinwidget.BuildConfig;
import com.samuelprashker.altcoinwidget.Currency;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class NewCurrenciesTest extends TestCase {

    @Test
    public void testProviders() {
        Resources resources = RuntimeEnvironment.application.getResources();
        BTCProvider[] values = BTCProvider.values();
        List<String> currencies = new ArrayList<>();
        for (Currency c : Currency.values()) {
            currencies.add(c.name());
        }
        for (BTCProvider btc : values) {
            List<String> added = new ArrayList<>();
            List<String> removed = new ArrayList<>();
            if (btc == BTCProvider.MTGOX || btc == BTCProvider.BTCXCHANGE || btc == BTCProvider.BUTTERCOIN) continue;
            Set<String> existingCurrencies = new HashSet<>(Arrays.asList(resources.getStringArray(btc.getCurrencies())));
            for (String currency : currencies) {
                try {
                    String value = btc.getValue(currency);
                    Double.valueOf(value);
                    if (!existingCurrencies.contains(currency)) {
                        added.add(currency);
                    }
                } catch (Exception e) {
                    if (existingCurrencies.contains(currency)) {
                        removed.add(currency);
                    }
                }
            }
            if (added.size() + existingCurrencies.size() == currencies.size()) continue;;
            if (!added.isEmpty()) {
                Collections.sort(added);
                System.out.println(btc.name() + " has new currencies: " + TextUtils.join(", ", added));
            }
            if (!removed.isEmpty()) {
                Collections.sort(removed);
                System.err.println(btc.name() + " removed currencies: " + TextUtils.join(", ", removed));
            }
        }
    }
}
