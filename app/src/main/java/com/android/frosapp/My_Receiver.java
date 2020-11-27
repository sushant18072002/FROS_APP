package com.android.frosapp;

import android.Manifest;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.telecom.TelecomManager;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.Provider;

public class My_Receiver extends BroadcastReceiver  {
    SharedRef sharedRef;
    private ReceiverCallback listener;

    public My_Receiver(ReceiverCallback listener){
        this.listener = listener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        /*String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        try {
        if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if("+917232869787".equals(incomingNumber)) {//917232869787
               sharedRef=new SharedRef(context);
                String username=sharedRef.username_load();
                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage("+917232869787", null, username, null, null);
            }
            Toast.makeText(context,"Ringing State Number is -"+incomingNumber,Toast.LENGTH_SHORT).show();
        }
        if ((state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))){
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if("+917232869787".equals(incomingNumber)) {
                String username=sharedRef.username_load();
                SmsManager mySmsManager = SmsManager.getDefault();
                mySmsManager.sendTextMessage("+917232869787", null, username, null, null);

            }
            Toast.makeText(context, "Ringing State Number is @" + incomingNumber, Toast.LENGTH_SHORT).show();
        }
        if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            Toast.makeText(context,"Idle State",Toast.LENGTH_SHORT).show();
        }
        }
        catch (Exception e){
            e.printStackTrace();
        }*/
        if (intent.getAction().equalsIgnoreCase("com.android.frosapp")){
            //Toast.makeText(context, "Panic location sent", Toast.LENGTH_LONG).show();
            sharedRef=new SharedRef(context);
            sharedRef.SaveData(0,0);
            /*location location= new location(context);
            location.check();*/
            listener.doSomething(null);
        }
        else if (intent.getAction().equalsIgnoreCase("android.intent.action.BOOT_COMPLETED")){
            // restart
            sharedRef=new SharedRef(context);
            sharedRef.LoadData();

        }
        /*
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                Toast.makeText(context,"Ringing State Number is - " + incomingNumber, Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }

        //TelephonyManager tm=(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        /*TelephonyManager tm = (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        if (tm.getCallState() == TelephonyManager.CALL_STATE_OFFHOOK) {
            String number = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            Toast.makeText(context, "------"+number, Toast.LENGTH_LONG).show();
        }*/

        /*
        String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        Toast.makeText(context, number, Toast.LENGTH_LONG).show();
        /*TelephonyManager tm=(TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);
        //String phone_number=(String) tm.ACTION_PHONE_STATE_CHANGED;
        if(intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals("+919411420294")){
            Toast.makeText(context, "call", Toast.LENGTH_LONG).show();
        }*/
        //if(state==){}
    }
   /* public void particular_sms(){
           String particular="";
            // permission already granted run sms send
            //Toast.makeText(getBaseContext(),message2, Toast.LENGTH_LONG).show();
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage("+91"+"7232869787", null, particular, null, null);
            //mySmsManager.sendTextMessage("+91"+sms_contact2, null, message2, null, null);
        }*/

}
