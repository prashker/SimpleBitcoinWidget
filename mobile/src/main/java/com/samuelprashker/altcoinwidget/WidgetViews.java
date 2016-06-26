package com.samuelprashker.altcoinwidget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;

public class WidgetViews {

	@SuppressLint("NewApi")
    public static void setText(Context context, RemoteViews views, int coin, Currency currency, String text, boolean color, String label, int widgetId) {
        TextSizer.Group group = null;
        int width = Prefs.getWidth(context, widgetId);
        if(width <= 0) width = 78;
		if(text!=null) {
            Double amount = Double.valueOf(text);
            Prefs.setLastAmount(context, widgetId, amount);
            group = TextSizer.getPriceID(context, currency, amount, width);
            if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || group.split) {
                views.setTextViewText(group.id, group.text);
            } else {
                views.setTextViewText(R.id.priceJB, group.text);
                views.setTextViewTextSize(R.id.priceJB, TypedValue.COMPLEX_UNIT_DIP, group.size);
            }
		}
        int providerID = TextSizer.getProviderID(context, label);
        for(int i=0; i< TextSizer.providerMap.size(); i++) {
            hide(views, TextSizer.providerMap.valueAt(i));
        }

        boolean showLabel = Prefs.getLabel(context, widgetId);
        if(showLabel) {
            show(views, providerID);
            hide(views, R.id.space);
            views.setTextViewText(providerID, label);
        } else {
            show(views, R.id.space);
            hide(views, providerID);
        }

        if(group == null) {
            Double amount = Prefs.getLastAmount(context, widgetId);
            group = TextSizer.getPriceID(context, currency, amount, width);
        }
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || group.split) {
            show(views, group.id, R.id.imageLayout);
        } else {
            show(views, R.id.priceJB, R.id.imageLayout);
        }
        boolean hideIcon = Prefs.getIcon(context, widgetId);

        String key = CoinFactory.getKey(coin);

        // To make this "dynamic" images need to follow a specific format
        // icon_<coin>(_bw) + suffix
        // <coin> is defined in the CoinFactory key field

        String image_resource = "icon_" + key;

        show(views, R.id.coinImage);

        // They don't want the icon
        if (hideIcon) {
            hide(views, R.id.coinImage);
        }
        else if (color) {
            // They want the icon, and was recent activity
            // Nothing to do?
        }
        else {
            // They want icon, no recent activity
            image_resource += "_bw";
        }

        // Dark is special, and requires _dark variants
        // Else, there is no special suffix
        String suffix = (Prefs.getThemeLayout(context, widgetId) == R.layout.widget_layout_dark ? "_dark" : "");

        // Add in the special dark suffix
        image_resource += suffix;

        int image_id_dynamic = CoinFactory.getResourceFromDrawable(image_resource);

        // Fallback if we didn't implement the specific style images
        if (image_id_dynamic == -1)
            image_id_dynamic = CoinFactory.getResourceFromDrawable("icon_" + key);

        // Regardless, change image ID
        changeImage(views, R.id.coinImage, image_id_dynamic);

        // No layout for image or label
        if(hideIcon && !showLabel) {
            hide(views, R.id.imageLayout);
        }
        hide(views, R.id.loading);
	}

	public static void setLoading(RemoteViews views) {
        show(views, R.id.loading);
        hide(views, R.id.imageLayout, R.id.priceJB);
        for(int i=0; i< TextSizer.priceMap.size(); i++) {
            hide(views, TextSizer.priceMap.valueAt(i));
        }
        for(int i=0; i< TextSizer.priceSplitMap.size(); i++) {
            hide(views, TextSizer.priceSplitMap.valueAt(i));
        }
	}

    static void show(RemoteViews views, int... ids) {
        for (int id : ids) {
            views.setViewVisibility(id, View.VISIBLE);
        }
    }

    static void hide(RemoteViews views, int... ids) {
        for (int id : ids) {
            views.setViewVisibility(id, View.GONE);
        }
    }

    static void changeImage(RemoteViews views, int id, int newresource) {
        views.setImageViewResource(id, newresource);
    }

}
