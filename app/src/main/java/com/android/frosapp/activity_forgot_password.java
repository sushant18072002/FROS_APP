package com.android.frosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

public class activity_forgot_password extends AppCompatActivity {
    Button btnGenerateOTP, btnSignIn;

    EditText etPhoneNumber, etOTP;

    String phoneNumber,otp,phone_no;

    FirebaseAuth auth;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private String verificationCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        findViews();
        StartFirebaseLogin();

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

        btnGenerateOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etPhoneNumber.getText().toString().length() == 0) {
                    etPhoneNumber.setError("Invalid number");
                }
                else{
                    boolean connected=false;
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
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/User");
                        Query checkUser = reference.orderByChild("Username").equalTo(etPhoneNumber.getText().toString());
                        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    phone_no = etPhoneNumber.getText().toString();
                                    phoneNumber = "+91" + (etPhoneNumber.getText().toString());
                                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                                            phoneNumber,                     // Phone number to verify
                                            60,                           // Timeout duration
                                            TimeUnit.SECONDS,                // Unit of timeout
                                            activity_forgot_password.this,        // Activity (for callback binding)
                                            mCallback);
                                } else
                                    etPhoneNumber.setError("User does not exist!");
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                    }                  // OnVerificationStateChangedCallbacks
                }}
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otp=etOTP.getText().toString();

                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode, otp);

                SigninWithPhone(credential);
            }
        });
    }
    @Override
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }

    private void SigninWithPhone(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(activity_forgot_password.this,activity_register.class);
                            intent.putExtra("phone_no",phone_no);
                            intent.putExtra("parent_activity","forgot_password_activity");
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(activity_forgot_password.this,"Incorrect OTP",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void findViews() {
        etPhoneNumber=(EditText)(findViewById(R.id.phone_no));
        etOTP=(EditText)(findViewById(R.id.edit_otp));
        etPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        btnGenerateOTP=(Button)(findViewById(R.id.button_otp));
        btnSignIn=(Button)(findViewById(R.id.button_next));
    }

    private void StartFirebaseLogin() {

        auth = FirebaseAuth.getInstance();
        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Toast.makeText(activity_forgot_password.this,"verification completed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(activity_forgot_password.this,"verification failed",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationCode = s;
                Toast.makeText(activity_forgot_password.this,"Code sent",Toast.LENGTH_SHORT).show();
            }
        };
    }

}