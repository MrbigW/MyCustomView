package com.example.egg_timer;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {
    private SeekBar timerSeekBar;
    private TextView timer_tv;
    private Boolean countIsActive = false;
    private Button controller_btn;
    private CountDownTimer countDownTimer;

public void test(){
    Runnable r=()-> System.out.println("hello world");
    new Thread(()-> System.out.println("hello lambda")).start();

}
    public void resetTimer() {

        timer_tv.setText("00:30");
        timerSeekBar.setProgress(30);
        countDownTimer.cancel();
        controller_btn.setText("Go!");
        timerSeekBar.setEnabled(true);
        countIsActive = false;
    }

    public void updateTimer(int secondsLeft) {
        int minutes = (int) secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        String minuteString = Integer.toString(minutes);
        String secondString = Integer.toString(seconds);
        if (minuteString != "10")
            minuteString = "0" + minuteString;
        if (seconds <= 9)
            secondString = "0" + secondString;
        timer_tv.setText(minuteString + ":" + secondString);

    }


    public void continueTimer(int secondsLeft) {
        int minutes = (int) secondsLeft / 60;
        int seconds = secondsLeft - minutes * 60;
        String minuteString = Integer.toString(minutes);
        String secondString = Integer.toString(seconds);

        if (minuteString != "10")
            minuteString = "0" + minuteString;
        if (seconds <= 9)
            secondString = "0" + secondString;
        timer_tv.setText(minuteString + ":" + secondString);


    }

    public void pauseTimer(View view) {
        countDownTimer.cancel();
        timerSeekBar.getProgress();


    }


    public void controlTimer(View view) {
        //new CountDownTimer(total time毫秒,每次变化时间毫秒)
        if (countIsActive == false) {

            countIsActive = true;
            timerSeekBar.setEnabled(false);
            controller_btn.setText("Stop");

            countDownTimer = new CountDownTimer(timerSeekBar.getProgress() * 1000 + 100, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    updateTimer((int) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    resetTimer();
                    MediaPlayer mPlayer = MediaPlayer.create(getApplicationContext(), R.raw.airborn);
                    mPlayer.start();

                }
            }.start();
        } else {
            countDownTimer.cancel();
            controller_btn.setText("Go!");
            timerSeekBar.setEnabled(true);
            countIsActive = false;
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        controller_btn = (Button) findViewById(R.id.controller_bt);
        timer_tv = (TextView) findViewById(R.id.timer_tv);

        
        timerSeekBar = (SeekBar) findViewById(R.id.timer_seekbar);
        timerSeekBar.setMax(600);
        timerSeekBar.setProgress(30);
        timerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                updateTimer(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }


        });


    }
}
