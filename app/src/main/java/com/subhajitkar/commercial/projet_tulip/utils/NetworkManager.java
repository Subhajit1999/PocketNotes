package com.subhajitkar.commercial.projet_tulip.utils;

import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private Context context;

    public NetworkManager(Context context){
        this.context = context;
    }

    public boolean isNetworkConnected() {  //checks only internet connectivity
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }
}
