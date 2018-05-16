package com.example.moamen.lab3;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

/**
 * Created by moamen on 2/13/18.
 */

public class ResponseReceiver extends BroadcastReceiver {
    public static final String ACTION_RESP =
            "com.mamlambo.intent.action.MESSAGE_PROCESSED";

    @Override
    public void onReceive(Context context, Intent intent) {
        String text = intent.getStringExtra(RSSPullService.PARAM_OUT_MSG);
    }
}
