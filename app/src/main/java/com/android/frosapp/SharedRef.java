package com.android.frosapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.content.ContextCompat;

import java.util.Calendar;

public class SharedRef {
    SharedPreferences ShredRef;
    Context context;
    public SharedRef(Context context){
        this.context=context;
        ShredRef=context.getSharedPreferences("myRef",Context.MODE_PRIVATE);
    }
    public void saveData(String Username,String name){
        SharedPreferences.Editor editor=ShredRef.edit();
        editor.putString("Username",Username);
        editor.putString("name",name);
        editor.commit();

    }
    public String username_load(){
        String FileContent=ShredRef.getString("Username","No_name_username");
        return FileContent;
    }
    public String name_load(){
        String FileContent=ShredRef.getString("name","No_name");
        return FileContent;
    }
    public  void SaveData(int hour,int minute){
        SharedPreferences.Editor editor=ShredRef.edit();
        editor.putInt("hour",hour);
        editor.putInt("minute",minute);
        editor.commit();
    }

    public void LoadData(){
        int Minute=ShredRef.getInt("minute",0);
        int Hour=ShredRef.getInt("hour",0);
        Alarmset(Hour,Minute);
    }
    public int loadmin(){
        int Minute1=ShredRef.getInt("minute",0);
        return Minute1;
    }
    public int loadhour(){
        int Hour1=ShredRef.getInt("hour",0);
        return Hour1;
    }
    void Alarmset(int Hour,int Minute){
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Hour);
        calendar.set(Calendar.MINUTE, Minute);
        calendar.set(Calendar.SECOND, 0);
        AlarmManager am = (AlarmManager)  context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, My_Receiver.class);
        intent.setAction("com.android.frosapp");
        intent.putExtra("MyMessage","hello from alarm");
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY , pi);
    }
}
