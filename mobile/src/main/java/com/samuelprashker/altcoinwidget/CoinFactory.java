package com.samuelprashker.altcoinwidget;

import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.HashMap;
import java.util.Map;

public class CoinFactory {
    public static final Map<Integer, Entry<Class, String>> providers = new HashMap<Integer, Entry<Class, String>>();

    static {
        providers.put(0, build(BTCProvider.class, "btc"));
        providers.put(1, build(ETHProvider.class, "eth"));
    }

    private static Entry build(Class c, String key) {
        return new SimpleEntry<Class, String>(c, key);
    }

    public static AltcoinInterface[] getEnum(int input) {
        return (AltcoinInterface[]) providers.get(input).getKey().getEnumConstants();
    }


    public static String getKey(int input) {
        String i = null;
        i = providers.get(input).getValue();
        return i;
    }

    public static int getResourceFromArray(String key) {
        try {
            return R.array.class.getField(key).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static int getResourceFromDrawable(String key) {
        try {
            return R.drawable.class.getField(key).getInt(null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return -1;
    }
}