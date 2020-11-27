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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class fragment_feedback  extends Fragment {
    EditText feedback;
    Button button;
    String getArgument;
    public fragment_feedback(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragement_feedback,container,false);
        getArgument= getArguments().getString("username");
        feedback=(EditText)(view.findViewById(R.id.feedback));
        button=(Button)(view.findViewById(R.id.feedback_button));
        //show=(TextView)(view.findViewById(R.id.successfully_update));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkentries()) {
                    //String phoneNumbers = maskedString.replaceAll("[^\\d]", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference myRef = database.getReference("User");
                    myRef.child(getArgument).child("Feedback").setValue(feedback.getText().toString());
                    feedback.setText("Thank you for your feedback");
                }
            }
        });
        return view;
    }
    public boolean checkentries() {
        boolean check = true;
        if (feedback.getText().toString().length() == 0) {
            feedback.setError("Feedback is required!");
            check = false;
        }
        return check;
    }
}
