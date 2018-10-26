package com.example.anurag.calllog;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.logging.Logger;

public class AlarmManagerTest extends AppCompatActivity {
    TimePicker timePicker;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_activity);
        timePicker =  findViewById(R.id.timePicker);
        if (ContextCompat.checkSelfPermission(AlarmManagerTest.this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(AlarmManagerTest.this,Manifest.permission.READ_CALL_LOG))
            {
                ActivityCompat.requestPermissions(AlarmManagerTest.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
            }
            else
            {
                ActivityCompat.requestPermissions(AlarmManagerTest.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
            }
        }

        //attaching clicklistener on button
        findViewById(R.id.buttonAlarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //We need a calendar object to get the specified time in millis
                //as the alarm manager method takes time in millis to setup the alarm
                Calendar calendar = Calendar.getInstance();
                if (android.os.Build.VERSION.SDK_INT >= 23) {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getHour(), timePicker.getMinute(), 0);
                } else {
                    calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH),
                            timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
                }

                setAlarm(calendar.getTimeInMillis());
            }
        });
    }

    private void setAlarm(long time) {
        Logger.getGlobal().info(":::Time :::"+time);
        //getting the alarm manager
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(getApplicationContext(), BroadCastReciverHandler.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day
        am.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_DAY, pi);
        Toast.makeText(this, "Alarm is set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode)
        {
            case  1 : {
                if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(AlarmManagerTest.this,Manifest.permission.READ_CALL_LOG)
                            == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permistion Granted!",Toast.LENGTH_SHORT).show();
//                        TextView textView = findViewById(R.id.call);
//                        textView.setText(new RestServiceManager().get());
                    }
                    else
                    {
                        Toast.makeText(this,"No Permistion Granted!",Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
            }
        }
    }
}
