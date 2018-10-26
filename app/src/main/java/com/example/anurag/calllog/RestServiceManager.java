package com.example.anurag.calllog;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.CallLog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.anurag.calllog.LoginManager.Name;

public class RestServiceManager extends AppCompatActivity {

    public  SharedPreferences sharedpreferences;
    public static final String mypreference = "userlogin";
    public static final String Name = "nameKey";
    public static final String Mobile = "mobileKey";
    String callDuration,name ="";
    SimpleDateFormat DATE_FORMAT;
    String callBy ;
    String PHONE_NO_1 = "+919911222259";
    String PHONE_NO_2 = "9911222259";
    Date callDayTime = null;
    String date,month ="";
    String date_month;
    String url = "http://abhidev.tk:2127/call/sync";
    String getUniqueUrl = "http://abhidev.tk:2127/call/current?month";
    String getByUserUrl = "http://abhidev.tk:2127/call/user?name";
    RequestQueue queue ;
    Context context;


   public RestServiceManager(Context context)
    {
        this.context=context;
        sharedpreferences = context.getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(context);
    }
    public RestServiceManager(){

    }

//    public static RestServiceManager getInstance() {
//        return mInstance;
//    }
    public  void  send() {
        JSONObject json = null;
         callBy= sharedpreferences.getString(Name, "");
         System.out.print("call By::"+callBy);
        try {
         json = new JSONObject();
         json.put("data",new JSONArray(callDetails().toString()));

             System.out.println(json.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

                final JsonObjectRequest postRequest = new JsonObjectRequest (Request.Method.POST,url, json,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // response
                        Log.d("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        )
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
            Map<String, String>  params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");

            return params;
        }
        };
        queue.add(postRequest);
    }



    public void get(int month, final VollyCallBack callBack)
    {
        final ArrayList<String> data =new ArrayList<>();

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getUniqueUrl+"="+month, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        // display response
                        try {

                            if (response.getInt("status") == 200) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                             DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            Date date = new Date(Long.parseLong(jsonArray.getJSONObject(i).getString("calltime")));
                                            String dateFormat = DATE_FORMAT.format(date);
                                            data.add(jsonArray.getJSONObject(i).getString("callBy")
                                                    +" "+dateFormat);
                                        }
                                        catch (Exception e)
                                        {
                                            data.add(jsonArray.getJSONObject(i).getString("callBy"));
                                            e.printStackTrace();
                                        }

                                    }
                                    callBack.onSuccess(data);

                                }
                                callBack.onSuccess(data);
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);

    }


    public List<String> callDetails()
    {
        List<String> list = new ArrayList<>();

        Cursor managedCursor = context.getContentResolver().query(CallLog.Calls.CONTENT_URI, null, null, null, null);
        int number = managedCursor.getColumnIndex(CallLog.Calls.NUMBER);
        int type = managedCursor.getColumnIndex(CallLog.Calls.TYPE);
        int date = managedCursor.getColumnIndex(CallLog.Calls.DATE);
        int duration = managedCursor.getColumnIndex(CallLog.Calls.DURATION);
        JSONObject jsonObject;
        while (managedCursor.moveToNext()) {
                String phNumber = managedCursor.getString(number);
                String callDate = managedCursor.getString(date);
                callDayTime = new Date(Long.valueOf(callDate));

                if (phNumber.equals(PHONE_NO_1)|| phNumber.equals(PHONE_NO_2))
                {
                    month = callDayTime.getMonth()+1+"";
                    this.date = callDayTime.getDate()+"";
                    date_month = this.date+""+month;
                    callDuration = managedCursor.getString(duration);
                    try {
                     jsonObject = new JSONObject();
                    jsonObject.put("name", "Pani Wala");
                    jsonObject.put("date", this.date);
                    jsonObject.put("month", month);
                    jsonObject.put("callBy", callBy);
                    jsonObject.put("callDuration", callDuration);
                    jsonObject.put("date_month", date_month);
                    jsonObject.put("calltime", callDayTime.getTime());
                    list.add(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

    }
        managedCursor.close();
        return list;
    }

    public void getByUser(String txt, final VollyCallBack callBack) {
        final ArrayList<String> data =new ArrayList<>();

// prepare the Request
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, getByUserUrl+"="+txt, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        // display response
                        try {

                            if (response.getInt("status") == 200) {
                                JSONArray jsonArray = response.getJSONArray("data");
                                if (jsonArray.length() != 0) {
                                    for (int i = 0; i < jsonArray.length(); i++) {
                                        try {
                                            DATE_FORMAT = new SimpleDateFormat("dd MMM yyyy HH:mm");
                                            Date date = new Date(Long.parseLong(jsonArray.getJSONObject(i).getString("calltime")));
                                            String dateFormat = DATE_FORMAT.format(date);
                                            data.add(jsonArray.getJSONObject(i).getString("callBy")
                                                    +" "+dateFormat);
                                        }
                                        catch (Exception e)
                                        {
                                            data.add(jsonArray.getJSONObject(i).getString("callBy"));
                                            e.printStackTrace();
                                        }

                                    }
                                    callBack.onSuccess(data);

                                }
                                callBack.onSuccess(data);
                            }
                        } catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                    }
                }
        );

// add it to the RequestQueue
        queue.add(getRequest);

    }
}

