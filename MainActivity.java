package com.example.StudyCafe;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static android.support.v4.content.ContextCompat.getSystemService;
import static android.support.v4.content.ContextCompat.startActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.R;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    //Timer Variables


    private TextView mTextViewCountDown;
    private Button startPauseButton;
    private Button resetButton;

    private Button setButton;
    private EditText mEditTextInput;
    private Button rewardButton;
    private CountDownTimer cdTimer;
    private boolean mTimerRunning;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewCountDown = findViewById(R.id.text_view_countdown);

        startPauseButton = findViewById(R.id.button_start_pause);
        resetButton = findViewById(R.id.button_reset);

        rewardButton = findViewById(R.id.button_reward); // Initializing Reward Button on 1st activity page
        setButton = findViewById(R.id.button_set); //Initialzing Set Button on 1st activity page

        //Uses method to open rewards page
        rewardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openRewardsPage();
            }
            //method to open rewards page


        });


        //Button Set
        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = mEditTextInput.getText().toString();
                if (input.length() == 0) {
                    Toast.makeText(MainActivity.this, "Field can't be empty", Toast.LENGTH_SHORT).show();
                    return;

                }else {
                    setTime(1800000);
                }

                long millisInput = Long.parseLong(input) * 60000;
                if (millisInput == 0) {
                    Toast.makeText(MainActivity.this, "Please enter a positive number", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    setTime(1800000);
                }
                setTime(millisInput);
                mEditTextInput.setText("");
            }
        });



        startPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mTimerRunning) {
                            pauseTimer();
                        } else {
                            startTimer();
                        }
                        /*rewardButton.setText("Reward");
                        rewardButton.setVisibility(View.VISIBLE);*/
                    }
                });

                resetButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resetTimer();
                    }
                });

                updateCountDownText();


            }
            public void openRewardsPage() {
                Intent intent = new Intent(MainActivity.this, com.example.StudyCafe.RewardsPage.class);
                startActivity(intent);
            }


            private void startTimer() {
                cdTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        mTimeLeftInMillis = millisUntilFinished;
                        updateCountDownText();
                        rewardButton.setText("Reward");
                        rewardButton.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onFinish() {
                        mTimerRunning = false;
                        updateButtons();
                        //Need to call method to collect points


                    }
                }.start();
                mTimerRunning = true;
                updateButtons();


            }

            private void pauseTimer() {
                cdTimer.cancel();
                mTimerRunning = false;
                updateButtons();
            }

            private void resetTimer() {

                mTimeLeftInMillis = mStartTimeInMillis;
                updateCountDownText();
                updateButtons();

            }
       private void setTime(long milliseconds) {
            mStartTimeInMillis = milliseconds;
           resetTimer();
                closeKeyboard();
        }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }




    private void updateCountDownText() {
            if(mTimeLeftInMillis==1800000) {
                int minuets = (int) mTimeLeftInMillis / 1000 / 60;
                int seconds = (int) mTimeLeftInMillis / 1000 % 60;

                String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minuets, seconds);

                mTextViewCountDown.setText(timeLeftFormatted);
            }
            else{
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
                    mTextViewCountDown.setText(timeLeftFormatted);
                }
            }

            //Makes sure that when rotating device, timer does not change
            private void updateButtons() {
                if (mTimerRunning) {
                    resetButton.setVisibility(View.INVISIBLE);
                    startPauseButton.setText("Pause");
                    rewardButton.setVisibility(View.INVISIBLE);
                } else {
                    startPauseButton.setText("Start");
                    if (mTimeLeftInMillis < 1000) {
                        startPauseButton.setVisibility(View.INVISIBLE);
                    } else {
                        startPauseButton.setVisibility(View.VISIBLE);
                    }
                    if (mTimeLeftInMillis < mTimeLeftInMillis) {
                        resetButton.setVisibility(View.VISIBLE);
                        rewardButton.setVisibility(View.VISIBLE);
                    } else {
                        resetButton.setVisibility(View.INVISIBLE);
                        rewardButton.setVisibility(View.INVISIBLE);
                    }
                }
            }
    protected void onStop() {
        super.onStop();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();
        if (cdTimer != null) {
            cdTimer.cancel();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        updateCountDownText();
        updateButtons();
        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();
            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateButtons();
            } else {
                startTimer();
            }
        }








}

    public View getCurrentFocus() {
        return currentFocus;
    }

    public void setCurrentFocus(View currentFocus) {
        this.currentFocus = currentFocus;
    }
}


/* https://www.w3adda.com/android-tutorial/android-intents   - will help with the data access */