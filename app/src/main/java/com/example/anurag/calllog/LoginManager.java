package com.example.anurag.calllog;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.logging.Logger;

public class LoginManager extends AppCompatActivity {

    EditText name,mobileNo;
    Button button;
    public static SharedPreferences sharedpreferences;
    public static final String mypreference = "userlogin";
    public static final String Name = "nameKey";
    public static final String Mobile = "mobileKey";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        button = findViewById(R.id.submit);
        name =  findViewById(R.id.eName);
        mobileNo =  findViewById(R.id.eMobileNo);

        if (checkCredential())
        {
            Intent intent = new Intent(getApplicationContext(),RecyclerViewManager.class);
            startActivity(intent);
        }
        if (button!=null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String n = name.getText().toString();
                    String e = mobileNo.getText().toString();
                    SharedPreferences.Editor editor = sharedpreferences.edit();
                    editor.putString(Name, n);
                    editor.putString(Mobile, e);
                    editor.commit();
                    Intent intent = new Intent(getApplicationContext(), RecyclerViewManager.class);
                    startActivity(intent);
                }
            });
        }
    }

    private boolean checkCredential() {
        Logger.getGlobal().warning("Inside check creadential method::");
        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Name) && sharedpreferences.contains(Mobile)) {
            Logger.getGlobal().warning(sharedpreferences.getString(Name, "name get from shared::::"));
            Logger.getGlobal().warning(sharedpreferences.getString(Mobile, "mobile get from shared::::"));
            return true;
        }
        return false;
    }

}
