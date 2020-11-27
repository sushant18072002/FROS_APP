package com.android.frosapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class fragment_timer extends Fragment {
    SharedRef sharedRef;
    TimePicker tp;
    TextView time_count;
    Button timer,counter;
    Context context;

    public long mStartTimeInMillis;

    public fragment_timer(Context context) {
            this.context=context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timer, container, false);
        tp=(TimePicker)view.findViewById(R.id.tp1);
        timer=(Button)(view.findViewById(R.id.timer));
        counter=(Button)(view.findViewById(R.id.Time_stop));
        time_count=(TextView)(view.findViewById(R.id.timer_count));
        //show=(TextView)(view.findViewById(R.id.successfully_update));
        sharedRef=new SharedRef(context);
        if(sharedRef.loadhour()!=0 && sharedRef.loadmin()!=0){
            time_count.setVisibility(View.VISIBLE);
            counter.setVisibility(View.VISIBLE);
            tp.setVisibility(View.GONE);
            timer.setVisibility(View.GONE);
            timersount();
        }
        timer.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                //this.dismiss();
                //sharedRef.Alarmset(tp.getHour(),tp.getMinute());
                //sharedRef.SaveData(tp.getHour(),tp.getMinute());
                time_count.setVisibility(View.VISIBLE);
                counter.setVisibility(View.VISIBLE);
                tp.setVisibility(View.GONE);
                timer.setVisibility(View.GONE);

                panic_activity ma=(panic_activity) getActivity();
                if ((int) Build.VERSION.SDK_INT >= 23)
                    ma.SetTime(tp.getHour(),tp.getMinute());
                else
                    ma.SetTime(tp.getCurrentHour(),tp.getCurrentMinute());


            }

        });
        counter.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                time_count.setVisibility(View.GONE);
                counter.setVisibility(View.GONE);
                tp.setVisibility(View.VISIBLE);
                timer.setVisibility(View.VISIBLE);
                sharedRef.SaveData(0,0);
            }

        });
        return view;
    }

   /* private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }
            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();
        mTimerRunning = true;
        updateWatchInterface();
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }
        time_count.setText(timeLeftFormatted);
    }*/

   public void timersount(){
        sharedRef=new SharedRef(context);
        int hour=sharedRef.loadhour();
        int minute=sharedRef.loadmin();
        final long maxCounter =60000;
        //long diff =1000*(minute*60+hour*3600+60);
      // long mili= 1000*(minute*60+hour*3600+60)-System.currentTimeMillis();
        mStartTimeInMillis=10000*(minute*60+hour*3600);
       Calendar rightNow = Calendar.getInstance();
       int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY);
       int currentmin = rightNow.get(Calendar.MINUTE);
       int seconds = rightNow.get(Calendar.SECOND);
       final long[] counter = {(minute-currentmin) * 60 + (hour-currentHourIn24Format) * 3600-seconds};
       new CountDownTimer(mStartTimeInMillis, 1000){
           public void onTick(long millisUntilFinished){
               int hours = (int) (counter[0]) / 3600;
               int minutes = (int) ((counter[0]) % 3600) / 60;
               int seconds = (int) (counter[0]) % 60;
               time_count.setText(String.format("%d:%02d:%02d",hours,minutes,seconds));
               counter[0]--;
               if(counter[0]<0){
                   onFinish();
               }
           }
           public  void onFinish(){
               time_count.setText("FINISH!!");
           }
       }.start();
   /*     new CountDownTimer(maxCounter , diff ) {

            public void onTick(long millisUntilFinished) {
                long diff = maxCounter - millisUntilFinished;
                int hours = (int) (diff / 1000) / 3600;
                int minutes = (int) ((diff / 1000) % 3600) / 60;
                int seconds = (int) (diff / 1000) % 60;
                time_count.setText(String.format("%d:%02d:%02d",hours,minutes,seconds));
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                time_count.setText("done!");
            }

        }.start();*/
    }


}
