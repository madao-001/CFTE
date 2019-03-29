package com.example.administrator.cfte;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.administrator.cfte.GetLocation.MyLocationManager;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

import java.util.ArrayList;
import java.util.List;

public class MyApplicaton extends Application {
    public static Context context;

    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        SpeechUtility.createUtility(this, SpeechConstant.APPID+"=5c1704b9");
    }
    public static Context getContext(){
        return context;
    }
}
