package com.perusudroid.synkpro.common;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CodeSnippet {


    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static String getCreatedOn() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //SimpleDateFormat formatter = new SimpleDateFormat("Y-m-d H:i:s", Locale.getDefault());
        Date date = new Date();
        return formatter.format(date);
    }

}
