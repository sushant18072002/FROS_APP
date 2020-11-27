package com.android.frosapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class fragement_emergency_contact extends Fragment {
    EditText person_name1,contact1,location1,person_name2,contact2,location2;
    Button button;
    String getArgument;
    TextView show;

    public fragement_emergency_contact(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.emergency_contact,container,false);
        getArgument= getArguments().getString("username");
        person_name1=(EditText)(view.findViewById(R.id.person_name1));
        contact1=(EditText)(view.findViewById(R.id.editTextPhone1));
        location1=(EditText)(view.findViewById(R.id.address1));
        person_name2=(EditText)(view.findViewById(R.id.person_name2));
        contact2=(EditText)(view.findViewById(R.id.editTextPhone2));
        location2=(EditText)(view.findViewById(R.id.address2));
        button=(Button)(view.findViewById(R.id.button_emergency));
        show=(TextView)(view.findViewById(R.id.successfully_update));
        setdata();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkentries()){
                    //String phoneNumbers = maskedString.replaceAll("[^\\d]", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User");
                    myRef.child(getArgument).child("Emergency contact").child("First person").child("Person name:").setValue(person_name1.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("First person").child("Person contact:").setValue(contact1.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("First person").child("Person location:").setValue(location1.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("Second person").child("Person name:").setValue(person_name2.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("Second person").child("Person contact:").setValue(contact2.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("Second person").child("Person location:").setValue(location2.getText().toString());
                    myRef.child(getArgument).child("Emergency contact").child("flag").setValue("1");
                    show.setVisibility(View.VISIBLE);
                }
            }
        });
        return view;
    }
    public boolean checkentries() {
        boolean check = true;
        if (person_name1.getText().toString().length() == 0) {
            person_name1.setError("Person name is required!");
            check = false;
        }
        if (location1.getText().toString().length() == 0) {
            location1.setError("Location is required!");
            check = false;
        }
        if (person_name2.getText().toString().length() == 0) {
            person_name2.setError("Person name is required!");
            check = false;
        }
        if (location2.getText().toString().length() == 0) {
            location2.setError("Location is required!");
            check = false;
        }
        if (contact1.getText().toString().length() == 0) {
            contact1.setError("Contact number is required!");
            check = false;
        }
        else if (contact1.getText().toString().length()!=10) {
            contact1.setError("Contact number is invalid!");
            check = false;
        }
        if (contact2.getText().toString().length() == 0) {
            contact2.setError("Contact number is required!");
            check = false;
        }
        else
        if (contact2.getText().toString().length() != 10) {
            contact2.setError("Contact number is invalid!");
            check = false;
        }

        return check;
    }
        public void setdata(){
            DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("/User");
            Query checkUser = reference1.orderByChild("Username").equalTo(getArgument);
            checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        String flag=dataSnapshot.child(getArgument).child("Emergency contact").child("flag").getValue(String.class);
                        if(flag.equals("1")) {
                            person_name1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person name:").getValue(String.class));
                            contact1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person contact:").getValue(String.class));
                            location1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person location:").getValue(String.class));
                            person_name2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person name:").getValue(String.class));
                            contact2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person contact:").getValue(String.class));
                            location2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person location:").getValue(String.class));
                        }

                    } else {
                        show.setVisibility(View.VISIBLE);
                    }
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    /*              person_name1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person name:").getValue(String.class));
                    contact1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person contact:").getValue(String.class));
                    location1.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("First person").child("Person location:").getValue(String.class));
                    person_name2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person name:").getValue(String.class));
                    contact2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person contact:").getValue(String.class));
                    location2.setText(dataSnapshot.child(getArgument).child("Emergency contact").child("Second person").child("Person location:").getValue(String.class));*/
}
