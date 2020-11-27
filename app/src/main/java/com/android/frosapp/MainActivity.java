package com.android.frosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.CallLog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText user,password_edit;
    TextView result;
    Button login_button;
    ProgressBar progressBar;
    String username,password,name;
    boolean connected;
    SharedRef sharedRef;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedRef=new SharedRef(this);
        String load_username=sharedRef.username_load();
        String load_name=sharedRef.name_load();
        //call();

       /* if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    230);
        }*/
       /* if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            /*ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.PROCESS_OUTGOING_CALLS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALL_LOG,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS},
                    1);
        }*/
        if(!load_username.equals("No_name_username")){
            Intent intent = new Intent(getApplicationContext(), panic_activity.class);
            intent.putExtra("name",load_name);
            intent.putExtra("username",load_username);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);
        //this.setTheme(R.style.CustomDialogTheme);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Make sure you have internet connection!");
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
        user=(EditText)(findViewById(R.id.username));
        password_edit=(EditText)(findViewById(R.id.password));
        login_button=(Button)(findViewById(R.id.Login_button));
        result=(TextView)(findViewById(R.id.text));
        progressBar=(ProgressBar)(findViewById(R.id.progressBar));
        connected = false;

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;
                if(connected==false){
                    //Toast.makeText(getBaseContext(), "Make sure you have internet connection!", Toast.LENGTH_LONG).show();
                    AlertDialog alertdialog = builder.create();
                    alertdialog.show();
                    alertdialog.setCancelable(false);
                    alertdialog.setCanceledOnTouchOutside(false);
                }
                else {
                    /*Intent intent = new Intent(getApplicationContext(), Panic_activity.class);
                    startActivity(intent);
                    finish();*/
                    isUser();
                }
            }
        });
    }

    public void forgot_password(View view) {
        //Toast.makeText(getBaseContext(), "~ Abhi sewa m nahi h ~", Toast.LENGTH_LONG).show();
        //Toast.makeText(getBaseContext(), "Keep touch with developer for future updates!", Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this,activity_forgot_password.class);
        startActivity(intent);
        finish();
    }

    public void signup(View view) {
        Intent intent = new Intent(this,activity_sigup.class);
        startActivity(intent);
        finish();
    }

    private void isUser() {
        progressBar.setVisibility(View.VISIBLE);
        login_button.setEnabled(false);
        login_button.setFocusableInTouchMode(true);
        final String userEnteredUsername = user.getText().toString().trim();
        //final String userEnteredPassword = password_edit.getEditText().getText().toString().trim();
        final String userEnteredPassword = password_edit.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/User");
        Query checkUser = reference.orderByChild("Username").equalTo(userEnteredUsername);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    user.setError(null);
                    String passwordFromDB = dataSnapshot.child(userEnteredUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userEnteredPassword)) {
                        progressBar.setVisibility(View.GONE);
                        user.setError(null);
                       /* Toast.makeText(getBaseContext(), "Successsfully Login", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(),panic_activity.class);
                        startActivity(intent);
                        finish();*/
                        String nameFromDB = dataSnapshot.child(userEnteredUsername).child("Firstname").getValue(String.class);
                        username=userEnteredUsername;
                        name=nameFromDB;
                        next_activity();
                        //String usernameFromDB = dataSnapshot.child(userEnteredUsername).child("username").getValue(String.class);
                        //String phoneNoFromDB = dataSnapshot.child(userEnteredUsername).child("phoneNo").getValue(String.class);
                       // String emailFromDB = dataSnapshot.child(userEnteredUsername).child("email").getValue(String.class);

                        /* Intent intent = new Intent(getApplicationContext(), panic_activity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("username",userEnteredUsername);
                        //intent.putExtra("password", passwordFromDB);
                        startActivity(intent);*/
                    } else {
                        progressBar.setVisibility(View.GONE);
                        login_button.setFocusableInTouchMode(false);
                        login_button.setEnabled(true);
                        login_button.setFocusableInTouchMode(false);
                        password_edit.setError("Wrong Password ");
                        password_edit.requestFocus();
                    }
                } else {
                    login_button.setEnabled(true);
                    login_button.setFocusableInTouchMode(false);
                    user.setError("No such User exist");
                    user.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void next_activity(){
        sharedRef.saveData(username,name);
        Intent intent = new Intent(getApplicationContext(), panic_activity.class);
        intent.putExtra("name", name);
        intent.putExtra("username",username);
        startActivity(intent);
    }

    /*public void call() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    230);
        } else {
            String[] projection = new String[]{
                    CallLog.Calls.CACHED_NAME,
                    CallLog.Calls.NUMBER,
                    CallLog.Calls.TYPE,
                    CallLog.Calls.DATE
            };
// String sortOrder = ContactsContract.Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC";


            Cursor cursor = mContext.getContentResolver().query(CallLog.Calls.CONTENT_URI, projection, null, null, null);
            while (cursor.moveToNext()) {
                String name = cursor.getString(0);
                String number = cursor.getString(1);
                Toast.makeText(getBaseContext(), name + "  " + number, Toast.LENGTH_LONG).show();
                String type = cursor.getString(2); // https://developer.android.com/reference/android/provider/CallLog.Calls.html#TYPE
                String time = cursor.getString(3); // epoch time - https://developer.android.com/reference/java/text/DateFormat.html#parse(java.lang.String
            }
            cursor.close();
        }
    }*/
}