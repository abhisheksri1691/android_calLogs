package com.example.anurag.calllog;

import android.Manifest;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

import static android.support.v7.widget.RecyclerView.HORIZONTAL;

public class RecyclerViewManager extends AppCompatActivity {
    Button filterByDate,syncData ;
    int year,month,date;
    TextView noContent;
    boolean isMonthEnable= false;
    boolean isUserEnable= false;
    Spinner monthSpinner,userSpinner;
    RecyclerView recyclerView;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    ProgressDialog progressDialog ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        isUserEnable = true;
        isMonthEnable =true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        setAlarm(System.currentTimeMillis());

        if (ContextCompat.checkSelfPermission(RecyclerViewManager.this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(RecyclerViewManager.this,Manifest.permission.READ_CALL_LOG))
            {
                ActivityCompat.requestPermissions(RecyclerViewManager.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
            }
            else
            {
                ActivityCompat.requestPermissions(RecyclerViewManager.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
            }
        }

        final Calendar myCalaender = Calendar.getInstance();
        year = myCalaender.get(Calendar.YEAR);
        month = myCalaender.get(Calendar.MONTH);
        date = myCalaender.get(Calendar.DAY_OF_MONTH);

        monthSpinner = findViewById(R.id.monthSpinner);
        userSpinner = findViewById(R.id.userSpinner);
        syncData = findViewById(R.id.syncData);
        noContent = findViewById(R.id.noContent);
        filterByDate = findViewById(R.id.bFilterByDate);
         recyclerView = findViewById(R.id.callList);
         LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,linearLayoutManager.getOrientation());
//        recyclerView.addItemDecoration(dividerItemDecoration);
        final RestServiceManager restServiceManager= new RestServiceManager(this);
        restServiceManager.get(month + 1, new VollyCallBack() {
            @Override
            public void onSuccess(List<String> result) {
                recyclerView.setAdapter(new AppAdapter(result));
                if (result.size()>0)
                {
                    noContent.setVisibility(View.INVISIBLE);
                    recyclerView.setVisibility(View.VISIBLE);
                    noContent.setHeight(TableRow.LayoutParams.WRAP_CONTENT);
                }
                else
                {
//            progressDialog.dismiss();
                    noContent.setVisibility(View.VISIBLE);
                    noContent.setHeight(TableRow.LayoutParams.MATCH_PARENT);
                    recyclerView.setVisibility(View.INVISIBLE);
                }
            }
        });


        ArrayAdapter<CharSequence> monthadapter = ArrayAdapter.createFromResource(this,R.array.month,R.layout.support_simple_spinner_dropdown_item);
        monthadapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        monthSpinner.setAdapter(monthadapter);
        monthSpinner.setSelection(9);
        monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {

                if (!isMonthEnable) {

                    Logger.getGlobal().warning("call rest api when month spinner is seletced::");

                restServiceManager.get(position + 1, new VollyCallBack() {
                    @Override
                    public void onSuccess(List<String> result) {

                        recyclerView.setAdapter(new AppAdapter(result));
                        if (result.size() > 0) {
                            noContent.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.WRAP_CONTENT);
                        } else {
//            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "No data available", Toast.LENGTH_SHORT).show();
                            noContent.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.MATCH_PARENT);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                isMonthEnable =false;
            }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        ArrayAdapter<CharSequence> useradapter = ArrayAdapter.createFromResource(this,R.array.users,R.layout.support_simple_spinner_dropdown_item);
        useradapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        userSpinner.setAdapter(useradapter);
        userSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String txt =adapterView.getItemAtPosition(position).toString();

                if (!isUserEnable) {
                    Logger.getGlobal().warning("call rest api when user spinner is seletced::");
                restServiceManager.getByUser(txt, new VollyCallBack() {
                    @Override
                    public void onSuccess(List<String> result) {
                        recyclerView.setAdapter(new AppAdapter(result));
                        if (result.size()>0)
                        {
                            noContent.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.WRAP_CONTENT);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"No data available",Toast.LENGTH_SHORT).show();
                            noContent.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.MATCH_PARENT);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });
                }
                isUserEnable =false;
                isMonthEnable =false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        progressDialog.dismiss();


//        syncData.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Calendar calendar = Calendar.getInstance();
//                int houralarm = calendar.get(Calendar.HOUR_OF_DAY);
//                int minute = calendar.get(Calendar.MINUTE);
//
//               timePickerDialog =  new TimePickerDialog(RecyclerViewManager.this, new TimePickerDialog.OnTimeSetListener() {
//                    @Override
//                    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
//                        Calendar calendarinner = Calendar.getInstance();
//                        if (android.os.Build.VERSION.SDK_INT >= 23) {
//                            calendarinner.set(calendarinner.get(Calendar.YEAR), calendarinner.get(Calendar.MONTH), calendarinner.get(Calendar.DAY_OF_MONTH),
//                                    timePicker.getHour(), timePicker.getMinute(), 0);
//                        } else {
//                            calendarinner.set(calendarinner.get(Calendar.YEAR), calendarinner.get(Calendar.MONTH), calendarinner.get(Calendar.DAY_OF_MONTH),
//                                    timePicker.getCurrentHour(), timePicker.getCurrentMinute(), 0);
//                        }
//                        setAlarm(calendarinner.getTimeInMillis());
//
//                    }
//                }
//                ,houralarm,minute,true);
//                timePickerDialog.setTitle("Select Time");
//                timePickerDialog.show();
//
//
//
//
//            }
//        });

        syncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                restServiceManager.get(month + 1, new VollyCallBack() {
                    @Override
                    public void onSuccess(List<String> result) {
                        recyclerView.setAdapter(new AppAdapter(result));
                        if (result.size()>0)
                        {
                            noContent.setVisibility(View.INVISIBLE);
                            recyclerView.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.WRAP_CONTENT);
                        }
                        else
                        {
                            noContent.setVisibility(View.VISIBLE);
                            noContent.setHeight(TableRow.LayoutParams.MATCH_PARENT);
                            recyclerView.setVisibility(View.INVISIBLE);
                        }
                    }
                });

            }
        });
        filterByDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               datePickerDialog = new DatePickerDialog(RecyclerViewManager.this, new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                     System.out.println("year::"+year);
                       System.out.println("month::"+month);
                       System.out.println("date::"+day);
                   }
               },year,month,date);
               datePickerDialog.show();
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

        //setting the repeating alarm that will be fired every 4 hours
        am.setInexactRepeating(AlarmManager.RTC_WAKEUP, time, 4*60*60*1000, pi);
        Toast.makeText(this, "Sync Time  is set", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
        switch (requestCode)
        {
            case  1 : {
                if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
                {
                    if (ContextCompat.checkSelfPermission(RecyclerViewManager.this,Manifest.permission.READ_CALL_LOG)
                            == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this,"Permistion Granted!",Toast.LENGTH_SHORT).show();
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
