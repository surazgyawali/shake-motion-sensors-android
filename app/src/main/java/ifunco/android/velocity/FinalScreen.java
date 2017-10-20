package ifunco.android.velocity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

import static ifunco.android.velocity.CommonMethod.soundPlayer;
import static ifunco.android.velocity.CommonMethod.Sound;


public class FinalScreen extends AppCompatActivity implements SensorEventListener {
    static final Handler TimeSoundHandler= new Handler();
    private Timer timer;
    private int i=0;

    final private int threshold_score =150;

    Runnable endSound = null;
    private MediaPlayer StopSound=new MediaPlayer();

    AnimationDrawable anim=new AnimationDrawable();
    SharedPreferences game_data;

    int score=0;
    int high_score =0;

    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;

    final SensorEventListener listener = this;
    double instant_acceleration=0;
    double resultantAcceleration=0;
    ArrayList<Double> accelerationList = new ArrayList<>();
    private Intent SecondScreen= new Intent();

    CommonMethod commonMethod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.final_screen_layout);

        LinearLayout layAd = (LinearLayout) findViewById(R.id.layad);

        commonMethod = (CommonMethod) getApplication();
        commonMethod.loadAd(layAd);

        Sensor mAccelerometer;
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);



        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");

        SecondScreen= new Intent(FinalScreen.this, SecondScreen.class);

        final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        final TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        ImageView imageView =(ImageView) findViewById(R.id.count_animation);




        if(StopSound!=null)
        {
            if(StopSound.isPlaying())
            {
                StopSound.stop();
            }
            StopSound.reset();
           StopSound.release();
        }
        StopSound = MediaPlayer.create(this,R.raw.stop);


        imageView.setBackgroundResource(R.drawable.count_movi);
        anim = (AnimationDrawable) imageView.getBackground();
        ScoreView.setTypeface(typeface);
        HighScoreView.setTypeface(typeface);

        game_data = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);

        high_score = game_data.getInt("HIGH_SCORE",0);
        score= game_data.getInt("CURRENT_SCORE",0);

        ScoreView.setText(Integer.toString(score));

        HighScoreView.setText(Integer.toString(high_score));

        anim.start();
        anim.setOneShot(true);

        soundPlayer(this,R.raw.time);


        TimeSoundHandler.postDelayed(endSound = new Runnable() {
                @Override
                public void run() {
                    mSensorManager.unregisterListener(listener);
                    if(score< threshold_score) {
                        if (anim.isRunning()) {
                            anim.stop();
                        }
                        Sound.stop();
                        soundPlayer(FinalScreen.this,R.raw.crowd1);
                        TimeSoundHandler.removeCallbacks(endSound);
                        TimeSoundHandler.removeCallbacksAndMessages(null);
                        startActivity(SecondScreen);
                        finish();
                    }

                }

            }, 3000);


    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
//change NOISE level here.
        final float NOISE = (float) 15.0;

        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            instant_acceleration =Math.sqrt((deltaX*deltaX)+(deltaY*deltaY)+(deltaZ*deltaZ));

            accelerationList.add(instant_acceleration);

           resultantAcceleration = Collections.max(accelerationList);
            score=calculateScore(resultantAcceleration);

            final SharedPreferences.Editor editor= game_data.edit();
            final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);

            //
            if(score> threshold_score){
                mSensorManager.unregisterListener(listener);

                if (anim.isRunning()) {
                    anim.selectDrawable(6);
                    anim.stop();
                }
                Sound.stop();

                if (score> high_score){

                    editor.putInt("HIGH_SCORE",score);
                }
                editor.putInt("CURRENT_SCORE",score);
                soundPlayer(this,R.raw.count);
                Sound.setLooping(true);

                final long period =2;
                timer=new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        //this repeats for every value of period variable in ms i.e changes the speed of progress
                        if (i<score){
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ScoreView.setText(Integer.toString(i));
                                }

                            });
                            i++;


                        }else{
                            //closing the timer
                            if(Sound.isPlaying()){
                            Sound.stop();}

                            StopSound.start();
                            StopSound.setLooping(false);


                            editor.apply();
                            timer.cancel();
                            if(score<=200)
                            {
                                soundPlayer(FinalScreen.this,R.raw.crowd1);

                            }
                            else if(score<=400)
                            {
                                soundPlayer(FinalScreen.this,R.raw.crowd2);
                            }
                            else if(score<=700)
                            {
                                soundPlayer(FinalScreen.this,R.raw.crowd3);
                            }
                            else
                            {
                                soundPlayer(FinalScreen.this,R.raw.crowd4);
                            }
                            TimeSoundHandler.removeCallbacks(endSound);
                            TimeSoundHandler.removeCallbacksAndMessages(null);

                            SecondScreen.putExtra("AnimateScore","myAnim");
                            startActivity(SecondScreen);
                            finish();
                        }
                    }
                }, 0, period);
            }

        }
    }

    @Override
    public void onBackPressed() {
        if(anim.isRunning()){
            anim.stop();
        }
        Sound.stop();
        TimeSoundHandler.removeCallbacks(endSound);
        TimeSoundHandler.removeCallbacksAndMessages(null);
        mSensorManager.unregisterListener(listener);
        startActivity(SecondScreen);
        finish();
    }

    private int calculateScore(double real_value){
       score=(int)Math.round((real_value/100)*1000);
        return score;
    }

}
