package com.android.frosapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class fragment_profile extends Fragment {
    public fragment_profile(){

    }
    EditText fname, lname, password, con_password, email, state, city;
    String getArgument;
    Button button;
    TextView showText;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_profile,container,false);
        getArgument= getArguments().getString("data");
        fname = (EditText) (view.findViewById(R.id.editfname));
        lname = (EditText) (view.findViewById(R.id.editlname));
        password = (EditText) (view.findViewById(R.id.editPassword));
        con_password = (EditText) (view.findViewById(R.id.editconfirmPassword));
        email = (EditText) (view.findViewById(R.id.editEmailAddress));
        state = (EditText) (view.findViewById(R.id.editstate));
        city = (EditText) (view.findViewById(R.id.editcity));
        showText=(TextView)(view.findViewById(R.id.textView10));
        /*/fun=(TextView)(view.findViewById(R.id.fun));
        fun.setText(getArgument);*/
        setdata();
        button=(Button)(view.findViewById(R.id.button_emergency));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkentries()) {
                    //String phoneNumbers = maskedString.replaceAll("[^\\d]", "");
                    panic_activity mi= (panic_activity) getActivity();
                    mi.nav_header_user_name(fname.getText().toString());
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User");
                    myRef.child(getArgument).child("Firstname").setValue(fname.getText().toString());
                    myRef.child(getArgument).child("Lastname").setValue(lname.getText().toString());
                    myRef.child(getArgument).child("email").setValue(email.getText().toString());
                    myRef.child(getArgument).child("password").setValue(password.getText().toString());
                    myRef.child(getArgument).child("state").setValue(state.getText().toString());
                    myRef.child(getArgument).child("city").setValue(city.getText().toString());
                    showText.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    public void setdata(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/User");
        Query checkUser = reference.orderByChild("Username").equalTo(getArgument);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fname.setText(dataSnapshot.child(getArgument).child("Firstname").getValue(String.class));
                    lname.setText(dataSnapshot.child(getArgument).child("Lastname").getValue(String.class));
                    email.setText(dataSnapshot.child(getArgument).child("email").getValue(String.class));
                    password.setText(dataSnapshot.child(getArgument).child("password").getValue(String.class));
                    con_password.setText(dataSnapshot.child(getArgument).child("password").getValue(String.class));
                    city.setText(dataSnapshot.child(getArgument).child("city").getValue(String.class));
                    state.setText(dataSnapshot.child(getArgument).child("state").getValue(String.class));

                } else {


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public boolean checkentries() {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        boolean check = true;
        if (fname.getText().toString().length() == 0) {
            fname.setError("First name is required!");
            check = false;
        }
        if (lname.getText().toString().length() == 0) {
            lname.setError("Last name is required!");
            check = false;
        }
        if (email.getText().toString().length() == 0) {
            email.setError("Email is required!");
            check = false;
        } else if (!(email.getText().toString().trim().matches(emailPattern))) {
            email.setError("Invalid email");
            check = false;
        }
        if (!password.getText().toString().equals(con_password.getText().toString())) {
            password.setError("Password must match!");
            con_password.setError("Password must match!");
            check = false;
        }
        if (city.getText().toString().length() == 0) {
            city.setError("City name is required!");
            check = false;
        }
        if (state.getText().toString().length() == 0) {
            state.setError("State name is required!");
            check = false;
        }
        return check;
    }

}
