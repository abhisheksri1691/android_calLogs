//package com.example.anurag.calllog;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.database.Cursor;
//import android.provider.CallLog;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.content.ContextCompat;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.Date;
//import java.util.List;
//
//import static com.example.anurag.calllog.LoginManager.Mobile;
//import static com.example.anurag.calllog.LoginManager.Name;
//
//public class MainActivity extends AppCompatActivity {
//
//    TextView call;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_CALL_LOG)!= PackageManager.PERMISSION_GRANTED)
//        {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,Manifest.permission.READ_CALL_LOG))
//            {
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
//            }
//            else
//            {
//                ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_CALL_LOG},1);
//            }
//        }
//        else
//        {
//             TextView textView = findViewById(R.id.call);
//             textView.setText(new RestServiceManager(this).get(0, new VollyCallBack() {
//                 @Override
//                 public void onSuccess(List<String> result) {
//                     result;
//                 }
//             }));
//
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions,  int[] grantResults) {
//        switch (requestCode)
//        {
//            case  1 : {
//                if (grantResults.length>0 && grantResults[0] ==PackageManager.PERMISSION_GRANTED)
//                {
//                    if (ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_CALL_LOG)
//                            == PackageManager.PERMISSION_GRANTED){
//                        Toast.makeText(this,"Permistion Granted!",Toast.LENGTH_SHORT).show();
//                        TextView textView = findViewById(R.id.call);
//                        textView.setText(new RestServiceManager(this).get(0).toString());
//                    }
//                    else
//                    {
//                        Toast.makeText(this,"No Permistion Granted!",Toast.LENGTH_SHORT).show();
//                    }
//                    return;
//                }
//        }
//        }
//    }
//}
