package com.android.frosapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class location implements LocationListener,ReceiverCallback {
    SharedRef sharedRef;
    Context context;
    String latitude,longitude;


    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 99;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    public location(Context context){
        this.context=context;
    }

    public void check(){
        sharedRef=new SharedRef(context);
        String username=sharedRef.username_load();
        Toast.makeText(context, "bolo ji", Toast.LENGTH_LONG).show();
        /*if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Kindly allow the  permission required for the proper function of the app");
                builder .setCancelable(false)
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //MyActivity.this.finish();
                            }
                        })
                        .setNegativeButton("", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //dialog.cancel();
                            }
                        });
                AlertDialog alertdialog = builder.create();
                alertdialog.show();
                alertdialog.setCancelable(false);
                alertdialog.setCanceledOnTouchOutside(false);

            }
        } else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 2, this);
            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (location != null) {
                    latitude = (String.valueOf(location.getLatitude()));
                    longitude = (String.valueOf(location.getLongitude()));
                    //getAddressFromLocation(location, getApplicationContext(), new panic_activity.GeoCoderHandler());
                    //Toast.makeText(getBaseContext(),username+" "+latitude, Toast.LENGTH_LONG).show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User");
                    myRef.child(username).child("Location").child("latitude").setValue(latitude);
                    myRef.child(username).child("Location").child("longitude").setValue(longitude);
                   // myRef.child(username).child("Location").child("address").setValue(address);
                /*Toast.makeText(getBaseContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
                getAddressFromLocation(location, getApplicationContext(), new GeoCoderHandler());*/
        /*        }
           } else {
               // showGPSDisabledAlertToUser();
            }
            Toast.makeText(context, "Panic location sent", Toast.LENGTH_LONG).show();

            // check permission is given
        }*/
    }

    @Override
    public void onLocationChanged(Location location) {
        sharedRef=new SharedRef(context);
        String username=sharedRef.username_load();
        latitude=(String.valueOf(location.getLatitude()));
        longitude=(String.valueOf(location.getLongitude()));
        Toast.makeText(context,username+" "+"latitude----"+latitude, Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myRef.child(username).child("Location").child("latitude").setValue(latitude);
        myRef.child(username).child("Location").child("longitude").setValue(longitude);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void doSomething(Object object) {
        check();
    }
}
