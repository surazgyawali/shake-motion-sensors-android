package np.com.surajgyawali.measurevelocity;
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
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;


public class FinalScreen extends AppCompatActivity implements SensorEventListener {
    Handler TimeSoundHandler= new Handler();
    private Timer timer;
    private int i=0;

    final private int threshold_score =150;

    Runnable endSound = null;
    MediaPlayer TimeSound=new MediaPlayer();
    MediaPlayer StopSound=new MediaPlayer();
    MediaPlayer CountSound=new MediaPlayer();
    MediaPlayer Crowd1= new MediaPlayer();
    MediaPlayer Crowd2= new MediaPlayer();
    MediaPlayer Crowd3= new MediaPlayer();
    MediaPlayer Crowd4= new MediaPlayer();





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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.final_screen_layout);

        Sensor mAccelerometer;
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");

        final Intent SecondScreen= new Intent(FinalScreen.this, SecondScreen.class);
        SecondScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        final TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        ImageView imageView =(ImageView) findViewById(R.id.count_animation);

        StopSound = MediaPlayer.create(this,R.raw.stop);
        TimeSound = MediaPlayer.create(this,R.raw.time);
        CountSound = MediaPlayer.create(this,R.raw.count);
        Crowd1 = MediaPlayer.create(this,R.raw.crowd1);
        Crowd2 = MediaPlayer.create(this,R.raw.crowd2);
        Crowd3 = MediaPlayer.create(this,R.raw.crowd3);
        Crowd4 = MediaPlayer.create(this,R.raw.crowd4);

        imageView.setBackgroundResource(R.drawable.count_movi);
        anim = (AnimationDrawable) imageView.getBackground();
        ScoreView.setTypeface(typeface);
        HighScoreView.setTypeface(typeface);


        game_data = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        //final SharedPreferences.Editor editor= game_data.edit();

        high_score = game_data.getInt("HIGH_SCORE",0);
        score= game_data.getInt("CURRENT_SCORE",0);

        ScoreView.setText(Integer.toString(score));

        HighScoreView.setText(Integer.toString(high_score));

        anim.start();
        anim.setOneShot(true);
        //code for audio while counting 1,2,3


        TimeSound.start();
        TimeSound.setLooping(false);

        TimeSoundHandler.postDelayed(endSound = new Runnable() {
                @Override
                public void run() {
                    mSensorManager.unregisterListener(listener);
                    if(score< threshold_score) {
                        if (TimeSound.isPlaying()) {
                            TimeSound.stop();
                        }
                        TimeSound.reset();

                        if (anim.isRunning()) {
                            anim.stop();
                        }
                        if (CountSound.isPlaying()) {
                            CountSound.stop();
                        }
                        startActivity(SecondScreen);
                        CountSound.reset();
                        Crowd1.start();
                        stopReaction(Crowd1);
                        TimeSoundHandler.removeCallbacks(endSound);

                    }

                }

            }, 3000);

        stopReaction(Crowd2);
        stopReaction(Crowd3);
        stopReaction(Crowd4);




        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
//change NOISE level here.
        final float NOISE = (float) 25.0;

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
            //
            final SharedPreferences.Editor editor= game_data.edit();
            final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
            final Intent SecondScreen= new Intent(FinalScreen.this, SecondScreen.class);
            SecondScreen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //
            if(score> threshold_score){
                mSensorManager.unregisterListener(listener);
                if (anim.isRunning()) {
                    anim.selectDrawable(6);
                    anim.stop();
                }
                if (TimeSound.isPlaying()) {
                    TimeSound.stop();
                    TimeSound.reset();
                }
                if (score> high_score){

                    editor.putInt("HIGH_SCORE",score);
                }
                editor.putInt("CURRENT_SCORE",score);

                CountSound.start();
                CountSound.setLooping(false);

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
                            StopSound.start();
                            StopSound.setLooping(false);
                            editor.apply();
                            timer.cancel();


                            if(CountSound.isPlaying()){
                                CountSound.stop();
                                CountSound.reset();}
                                startActivity(SecondScreen);

                            if(score<=200)
                            {
                                Crowd1.start();

                            }
                            else if(score<=400)
                            {
                                Crowd2.start();
                            }
                            else if(score<=700)
                            {
                                Crowd3.start();
                            }
                            else
                            {
                                Crowd4.start();
                            }


                        }
                    }
                }, 0, period);
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(TimeSound.isPlaying())
        {
            TimeSound.stop();

        }
        TimeSound.reset();
        if(anim.isRunning()){
            anim.stop();
        }
        if(CountSound.isPlaying())
        {
            CountSound.stop();

        }
        CountSound.reset();
        if(StopSound.isPlaying())
        {
            StopSound.stop();
        }
        StopSound.reset();
        TimeSoundHandler.removeCallbacks(endSound);
        mSensorManager.unregisterListener(listener);
        finish();
    }

        //If needed to remove max and determine score using average acceleration throughout the period

//    private double calculateAverage(List<Double> samples) {
//        Double sum = 0.0;
//        if(!samples.isEmpty()) {
//            for (Double mark : samples) {
//                sum += mark;
//            }
//            return sum.doubleValue() / samples.size();
//        }
//        return sum;
//    }
    private int calculateScore(double real_value){
       score=(int)Math.round((real_value/100)*1000);
        return score;
    }

    public  void stopReaction(final MediaPlayer soundobj){
        int mills;
        if(soundobj==Crowd1){mills=3000;}

        else if (soundobj==Crowd2){mills=4000;}
        else if (soundobj==Crowd3){mills=5000;}
        else {mills =6000;}

        new Handler().postDelayed(new Runnable() {

            public void run() {

                if(soundobj.isPlaying()){
                    soundobj.stop();
                }
                soundobj.reset();
            }
        }, mills);
    }
}
