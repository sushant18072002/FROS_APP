package com.android.frosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class fragment_main extends Fragment {
    ImageView panic_button,panic_button_click;
    TextView textView1,textView2,textView3,textView4;
    EditText panic_sms;
    Button submit;
    private onFragmentBtnSelected listner;
    public fragment_main(){

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_main,container,false);
        panic_button=(ImageView)(view.findViewById(R.id.panic_button));
        panic_button_click=(ImageView)(view.findViewById(R.id.panic_clicked_image));
        textView1=(TextView)(view.findViewById(R.id.textView5));
        textView2=(TextView)(view.findViewById(R.id.textView7));
        textView3=(TextView)(view.findViewById(R.id.textView9));
        textView4=(TextView)(view.findViewById(R.id.textView11));
        panic_sms=(EditText) (view.findViewById(R.id.panic_sms));
        submit=(Button)(view.findViewById(R.id.panic_sms_submit));
        panic_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //panic_button.setImageResource(R.drawable.panic_clicked);
                textView1.setVisibility(View.VISIBLE);
                textView2.setVisibility(View.VISIBLE);
                textView3.setVisibility(View.VISIBLE);
                textView4.setVisibility(View.VISIBLE);
                panic_sms.setVisibility(View.VISIBLE);
                submit.setVisibility(View.VISIBLE);
                panic_button.setVisibility(View.GONE);
                panic_button_click.setVisibility(View.VISIBLE);
                rotate();
                listner.onButtonSelected();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                panic_activity sms_send= (panic_activity) getActivity();
                sms_send.particular_sms(panic_sms.getText().toString());
            }
        });
        return view;
    }
    public void rotate() {
        RotateAnimation rotate = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setRepeatCount(3);
        rotate.setDuration(5000);
        rotate.setInterpolator(new LinearInterpolator());
        panic_button_click.startAnimation(rotate);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  onFragmentBtnSelected){
            listner= (onFragmentBtnSelected) context;
        }
        else{
            throw new ClassCastException(context.toString()+"must implement lisner");
        }

    }

    public interface onFragmentBtnSelected{
        public void onButtonSelected();
    }

}
