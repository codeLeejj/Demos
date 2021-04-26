package com.annis.mydemos.braodcastReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetworkReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager conn = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conn.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            Toast.makeText(context, "wifi连接", Toast.LENGTH_SHORT).show();
        } else if (networkInfo != null) {
            Toast.makeText(context, "网络连接", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "网络断开", Toast.LENGTH_SHORT).show();
        }
    }
}