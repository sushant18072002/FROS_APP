package com.android.frosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.GradientDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.SEND_SMS;

public class panic_activity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,fragment_main.onFragmentBtnSelected, LocationListener {
    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 99;
    private LocationManager locationManager;
    private Location location;
    private final int REQUEST_LOCATION = 200;
    private static final int PERMISSION_SEND_SMS =223;

    ActionBarDrawerToggle toggle;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    String  username,name,longitude,latitude,address;
    int count=0,i=0;
    SharedRef sharedRef;
    TextView nav_user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(checkAndRequestPermissions()) {
            if(checkAndRequestPermissions()) {

            }
        }
        setContentView(R.layout.activity_panic_activity);
        sharedRef=new SharedRef(this);
        Bundle b = getIntent().getExtras();
        username = b.getString("username", "000");
        name = b.getString("name", "name");
        toolbar=(findViewById(R.id.toolbar));
        setSupportActionBar(toolbar);
        drawerLayout=findViewById(R.id.drawerLayoutId);
        navigationView = findViewById(R.id.navigationViewId);
        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.setDrawerIndicatorEnabled(true);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


        //Inilize the default fragment
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment,new fragment_main());
        fragmentTransaction.commit();
        View hView =  navigationView.getHeaderView(0);
        nav_user = (TextView)hView.findViewById(R.id.nav_header_username);
        /*-------------*/
        nav_user.setText(sharedRef.name_load());
        locationManager = (LocationManager) getSystemService(Service.LOCATION_SERVICE);

        //Toast.makeText(this, username+"  "+name , Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else{
            if(count==2){
//            super.onBackPressed();
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(1);
        }
            if(count==1){
                toolbar.setTitle("Home");
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new fragment_main());
                fragmentTransaction.commit();
                count=2;
            }

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.right_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            sharedRef.saveData("No_name_username","No_name");
            Toast.makeText(getBaseContext(), "~ Logout ~", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        count=1;
        drawerLayout.closeDrawer(GravityCompat.START);
        if(menuItem.getItemId()==R.id.nav_home){
            //Menu menu = toolbar.getMenu();
            //MenuItem menuItem = menu.findItem(R.id.some_action);
           // menuItem.setTitle("New title");
            toolbar.setTitle("Home");
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new fragment_main());
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.nav_edit_profile){
            Fragment argumentFragment = new fragment_profile();//Get Fragment Instance
            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("data",username);//put string, int, etc in bundle with a key value
             argumentFragment.setArguments(data);
            toolbar.setTitle("Edit Profile");
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,argumentFragment);
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.nav_emergency_contacts){
            toolbar.setTitle("Emergency Contacts");
            Fragment fragment_e= new fragement_emergency_contact();//Get Fragment Instance
            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("username",username);//put string, int, etc in bundle with a key value
            fragment_e.setArguments(data);
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,fragment_e);
            fragmentTransaction.commit();
        }

        if(menuItem.getItemId()==R.id.nav_feedback){
            toolbar.setTitle("Feedback");
            Fragment fragment_e= new fragment_feedback();//Get Fragment Instance
            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("username",username);//put string, int, etc in bundle with a key value
            fragment_e.setArguments(data);
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,fragment_e);
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.nav_about_us){
            toolbar.setTitle("About us");
            Fragment fragment_e= new fragment_about_us();//Get Fragment Instance
            Bundle data = new Bundle();//Use bundle to pass data
            data.putString("username",username);//put string, int, etc in bundle with a key value
            fragment_e.setArguments(data);
            fragmentManager=getSupportFragmentManager();
            fragmentTransaction=fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,fragment_e);
            fragmentTransaction.commit();
        }
        if(menuItem.getItemId()==R.id.timer){
            if(checkAndRequestPermissions()) {
                toolbar.setTitle("Timer");
                Fragment fragment_e = new fragment_timer(this);//Get Fragment Instance
                Bundle data = new Bundle();//Use bundle to pass data
                data.putString("username", username);//put string, int, etc in bundle with a key value
                fragment_e.setArguments(data);
                fragmentManager = getSupportFragmentManager();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment, fragment_e);
                fragmentTransaction.commit();
            }
            else {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
                builder1.setMessage("Kindly allow the permission required for the proper function of the app");
                builder1.setCancelable(false)
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
                AlertDialog alertdialog = builder1.create();
                alertdialog.show();
                alertdialog.setCancelable(false);
                alertdialog.setCanceledOnTouchOutside(false);
            }
        }
        return true;
    }

    @Override
    public void onButtonSelected() {
        //Toast.makeText(getBaseContext(), "Hlw ji", Toast.LENGTH_LONG).show();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(panic_activity.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Kindly allow the permission required for the proper function of the app");
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
                toolbar.setTitle("Home");
                fragmentManager=getSupportFragmentManager();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container_fragment,new fragment_main());
                fragmentTransaction.commit();

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
                    getAddressFromLocation(location, getApplicationContext(), new GeoCoderHandler());
                    //Toast.makeText(getBaseContext(),username+" "+latitude, Toast.LENGTH_LONG).show();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User");
                    myRef.child(username).child("Location").child("latitude").setValue(latitude);
                    myRef.child(username).child("Location").child("longitude").setValue(longitude);
                    message(latitude, longitude );
                    myRef.child(username).child("Location").child("address").setValue(address);
                /*Toast.makeText(getBaseContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
                getAddressFromLocation(location, getApplicationContext(), new GeoCoderHandler());*/
                }
            } else {
                showGPSDisabledAlertToUser();
            }
            Toast.makeText(getBaseContext(), "Panic location sent", Toast.LENGTH_LONG).show();

            // check permission is given
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                // request permission (see result in onRequestPermissionsResult() method)
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        PERMISSION_SEND_SMS);
            } else {
                // permission already granted run sms send
           /* Toast.makeText(getBaseContext(),"click", Toast.LENGTH_LONG).show();
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(number,null, message, null, null);*/
            }

        }
    }


    @Override
    public void onLocationChanged(Location location) {
        //Toast.makeText(getBaseContext(), String.valueOf(location.getLatitude()), Toast.LENGTH_LONG).show();
        latitude=(String.valueOf(location.getLatitude()));
        longitude=(String.valueOf(location.getLongitude()));
       // Toast.makeText(getBaseContext(),username+" "+latitude, Toast.LENGTH_LONG).show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("User");
        myRef.child(username).child("Location").child("latitude").setValue(latitude);
        myRef.child(username).child("Location").child("longitude").setValue(longitude);
      /*  latitudePosition.setText(String.valueOf(location.getLatitude()));
        longitudePosition.setText(String.valueOf(location.getLongitude()));*/
        //getAddressFromLocation(location, getApplicationContext(), new GeoCoderHandler());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
        if (provider.equals(LocationManager.GPS_PROVIDER)) {
            showGPSDisabledAlertToUser();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
    private void showGPSDisabledAlertToUser() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent callGPSSettingIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(callGPSSettingIntent);
                    }
                });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    public void getAddressFromLocation(final Location location, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;
                try {
                    List<Address> list = geocoder.getFromLocation(
                            location.getLatitude(), location.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // sending back first address line and locality
                        result = address.getAddressLine(0) + ", " + address.getLocality() + ", " +  address.getCountryName() ;
                    }
                } catch (IOException e) {
                //    Log.e("TAG", "Impossible to connect to Geocoder", e);
                    Toast.makeText(panic_activity.this,"Impossible to connect to Geocoder"+e, Toast.LENGTH_LONG).show();
                    //Log.e("TAG", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();
    }
    private class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    break;
                default:
                    result= null;
            }
            //currentCity.setText(result);
            FirebaseDatabase database1 = FirebaseDatabase.getInstance();
            DatabaseReference myRef1 = database1.getReference("User");
            address=result;
           /* SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage(number,null, message, null, null);*/

            //Toast.makeText(getBaseContext(),address, Toast.LENGTH_LONG).show();
            myRef1.child(username).child("Location").child("address").setValue(address);

        }
    }
    public void message(final String lan1,final String lon1){
        /*SmsManager mySmsManager = SmsManager.getDefault();
        mySmsManager.sendTextMessage(sms_contact1,null,message1, null, null);
        mySmsManager.sendTextMessage("+919411420294",null,address, null, null);
        Toast.makeText(getBaseContext(),"Send message", Toast.LENGTH_LONG).show(); */
        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("/User");
        Query checkUser = reference1.orderByChild("Username").equalTo(username);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String sms_user_name1,sms_contact1,sms_user_name2,sms_contact2;
                    String flag=dataSnapshot.child(username).child("Emergency contact").child("flag").getValue(String.class);
                    if(flag.equals("1")) {
                        sms_user_name1=dataSnapshot.child(username).child("Emergency contact").child("First person").child("Person name:").getValue(String.class);
                        sms_contact1= dataSnapshot.child(username).child("Emergency contact").child("First person").child("Person contact:").getValue(String.class);
                        //location1.setText(dataSnapshot.child(username).child("Emergency contact").child("First person").child("Person location:").getValue(String.class));
                        sms_user_name2=dataSnapshot.child(username).child("Emergency contact").child("Second person").child("Person name:").getValue(String.class);
                        sms_contact2=dataSnapshot.child(username).child("Emergency contact").child("Second person").child("Person contact:").getValue(String.class);
                        //location2.setText(dataSnapshot.child(username).child("Emergency contact").child("Second person").child("Person location:").getValue(String.class));
                        String message1=sms_user_name1+"\n"+"ALERT!!! "+name+" is in Peril. "+"Location Info:"+"\n"+"https://www.google.com/maps/search/?api=1&query="+lan1+","+lon1;
                        //String message2=sms_user_name2+"\n"+"ALERT!!!"+"\n"+"This is to notify that "+name+" is in Peril "+"\n"+"Please try to contact immediately"+"\n"+"Location Info"+"\n"+address;
                        String message2=sms_user_name2+"\n"+"ALERT!!! "+name+" is in Peril. "+"Location Info:"+"\n"+"https://www.google.com/maps/search/?api=1&query="+lan1+","+lon1;
                        //Toast.makeText(getBaseContext(),message1, Toast.LENGTH_LONG).show();
                        sendsms(sms_contact1,message1,sms_contact2,message2);
                    }
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sendsms(String sms_contact1,String message1,String sms_contact2,String message2){
        if (ContextCompat.checkSelfPermission(panic_activity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(panic_activity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            //Toast.makeText(getBaseContext(),message2, Toast.LENGTH_LONG).show();
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage("+91"+sms_contact1, null, message1, null, null);
            mySmsManager.sendTextMessage("+91"+sms_contact2, null, message2, null, null);
            Toast.makeText(getBaseContext(), "Sms alert sent", Toast.LENGTH_LONG).show();
        }
    }
    public void nav_header_user_name(String userfname){
        nav_user.setText(userfname);
        sharedRef.saveData(username,userfname);
    }
    public void particular_sms(String particular){
        if (ContextCompat.checkSelfPermission(panic_activity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(panic_activity.this,
                    new String[]{Manifest.permission.SEND_SMS},
                    PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send
            //Toast.makeText(getBaseContext(),message2, Toast.LENGTH_LONG).show();
            SmsManager mySmsManager = SmsManager.getDefault();
            mySmsManager.sendTextMessage("+91"+"7232869787", null, particular, null, null);
            //mySmsManager.sendTextMessage("+91"+sms_contact2, null, message2, null, null);
            Toast.makeText(getBaseContext(), "Sms sent", Toast.LENGTH_LONG).show();
        }
    }

    private  boolean checkAndRequestPermissions() {
        int permissionSendMessage = ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS);
        int locationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        else
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getBaseContext(),"Sms permission required", Toast.LENGTH_LONG).show();
        }

        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        else
        if (ContextCompat.checkSelfPermission(panic_activity.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            Toast.makeText(getBaseContext(),"Location permission required", Toast.LENGTH_LONG).show();
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
    void SetTime(int Hour ,int Minute ){
        // /save dat
        //savedata savedata =new savedata(this);
        //sharedRef.saveData(username,name);
        sharedRef.Alarmset(Hour,Minute);
        sharedRef.SaveData(Hour,Minute);
        toolbar.setTitle("Timer");
        Fragment fragment_e = new fragment_timer(this);//Get Fragment Instance
        Bundle data = new Bundle();//Use bundle to pass data
        data.putString("username", username);//put string, int, etc in bundle with a key value
        fragment_e.setArguments(data);
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, fragment_e);
        fragmentTransaction.commit();
    }
}