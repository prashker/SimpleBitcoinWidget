package com.samuelprashker.altcoinwidget;

public interface AltcoinInterface {
    public abstract String getValue(String currencyCode) throws Exception;

    public int getCurrencies();

    public String getLabel();
}
