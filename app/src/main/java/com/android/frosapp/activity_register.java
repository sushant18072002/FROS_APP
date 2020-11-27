package com.android.frosapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import android.os.Bundle;

public class activity_register extends AppCompatActivity {
    TextView text;
    String phone_no;
    String parent_activity;
    EditText fname, lname, password, con_password, email, city,state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        fname = (EditText) (findViewById(R.id.editfname));
        /*EditText firstName = (EditText)findViewById(R.id.first_name);
        if( firstName.getText().toString().length() == 0 )
            firstName.setError( "First name is required!" );*/
        text=(TextView)(findViewById(R.id.textView3));
        lname = (EditText) (findViewById(R.id.editlname));
        password = (EditText) (findViewById(R.id.editPassword));
        con_password = (EditText) (findViewById(R.id.editconfirmPassword));
        email = (EditText) (findViewById(R.id.editEmailAddress));
        state = (EditText) (findViewById(R.id.editstate));
       /* AutoCompleteTextView state = (AutoCompleteTextView) findViewById(R.id.editstate);
// Get the string array
        String[] states = getResources().getStringArray(R.array.india_states);
// Create the adapter and set it to the AutoCompleteTextView
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,states);
        state.setAdapter(adapter);*/
        /*ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this,android.R.layout.select_dialog_item,getResources().getStringArray(R.array.india_states));
        //Getting the instance of AutoCompleteTextView
        state  =  (AutoCompleteTextView)findViewById(R.id.editstate);
        state.setThreshold(1);//will start working from first character
        state.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
*/
        city = (EditText) (findViewById(R.id.editcity));
        Bundle b = getIntent().getExtras();
        assert b != null;
        phone_no = b.getString("phone_no", "000");
        parent_activity=b.getString("parent_activity", "000");
        if(parent_activity.equals("forgot_password_activity")){
            text.setText("Edit Profile");
            setdata();
        }
        //Toast.makeText(this, phone_no, Toast.LENGTH_SHORT).show();
        //phone_no="9690286930";
    }
    public void onBackPressed()
    {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
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

    public void button_register(View view) {
        if (checkentries()) {
            //String phoneNumbers = maskedString.replaceAll("[^\\d]", "");
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference("User");
            myRef.child(phone_no).child("Username").setValue(phone_no);
            myRef.child(phone_no).child("Firstname").setValue(fname.getText().toString());
            myRef.child(phone_no).child("Lastname").setValue(lname.getText().toString());
            myRef.child(phone_no).child("email").setValue(email.getText().toString());
            myRef.child(phone_no).child("password").setValue(password.getText().toString());
            myRef.child(phone_no).child("state").setValue(state.getText().toString());
            myRef.child(phone_no).child("city").setValue(city.getText().toString());
            myRef.child(phone_no).child("Emergency contact").child("flag").setValue("0");
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("phone_no", String.valueOf(phone_no));
            startActivity(intent);
            finish();
        }
    }
    public void setdata(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("/User");
        Query checkUser = reference.orderByChild("Username").equalTo(phone_no);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    fname.setText(dataSnapshot.child(phone_no).child("Firstname").getValue(String.class));
                    lname.setText(dataSnapshot.child(phone_no).child("Lastname").getValue(String.class));
                    email.setText(dataSnapshot.child(phone_no).child("email").getValue(String.class));
                    password.setText(dataSnapshot.child(phone_no).child("password").getValue(String.class));
                    con_password.setText(dataSnapshot.child(phone_no).child("password").getValue(String.class));
                    city.setText(dataSnapshot.child(phone_no).child("city").getValue(String.class));
                    state.setText(dataSnapshot.child(phone_no).child("state").getValue(String.class));


                } else {


                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}