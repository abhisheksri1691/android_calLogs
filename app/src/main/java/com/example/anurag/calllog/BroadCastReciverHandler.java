package com.example.anurag.calllog;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.provider.CallLog;
import android.provider.Settings;


import java.util.Date;
import java.util.logging.Logger;


public class BroadCastReciverHandler extends BroadcastReceiver {

    Context context;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Logger.getGlobal().info("alarm trigger::");
         new RestServiceManager(context).send();
    }
}
