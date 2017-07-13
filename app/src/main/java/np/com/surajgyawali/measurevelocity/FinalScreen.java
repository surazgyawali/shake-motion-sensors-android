package np.com.surajgyawali.measurevelocity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
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

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class FinalScreen extends AppCompatActivity {
    Handler TimeSoundHandler= new Handler();
    private Timer timer;
    private int i=0;
    final private int thersholdscore=0;
    Runnable endSound = null;
    MediaPlayer TimeSound=new MediaPlayer();
    MediaPlayer StopSound=new MediaPlayer();
    MediaPlayer CountSound=new MediaPlayer();
    AnimationDrawable anim=new AnimationDrawable();
    SharedPreferences gamedata;

    int score=0;
    int highscore=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.final_screen_layout);

        gamedata = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= gamedata.edit();

        highscore = gamedata.getInt("HIGH_SCORE",0);
        score=gamedata.getInt("CURRENT_SCORE",0);

        Typeface CurrentScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        ScoreView.setTypeface(CurrentScore);
        ScoreView.setText(Integer.toString(score));


        Typeface HighScore = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        final TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        HighScoreView.setTypeface(HighScore);
        HighScoreView.setText(Integer.toString(highscore));

        Random r = new Random();
        score = r.nextInt(999) + 1;
        if(score>thersholdscore){
        editor.putInt("CURRENT_SCORE",score);
        }


        if (score>highscore){

            editor.putInt("HIGH_SCORE",score);
        }
        else{


        }

        //code for frame animation
        ImageView imageView =(ImageView) findViewById(R.id.count_animation);
        imageView.setBackgroundResource(R.drawable.count_movi);
        anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
        anim.setOneShot(true);
        //code for audio while counting 1,2,3

        //TimeSound=MediaPlayer.create(this, getResources().getIdentifier("time","raw",getPackageName()))
        StopSound = MediaPlayer.create(this,R.raw.stop);
        TimeSound = MediaPlayer.create(this,R.raw.time);
        CountSound = MediaPlayer.create(this,R.raw.count);
        TimeSound.start();
        TimeSound.setLooping(false);

        //code to update or count score



        //code to end all activities in 3 secs
            TimeSoundHandler.postDelayed(endSound = new Runnable() {
                @Override
                public void run() {
                    CountSound.start();
                    CountSound.setLooping(false);
                    if(score<thersholdscore) {
                        if (TimeSound.isPlaying()) {
                            TimeSound.stop();
                            TimeSound.reset();
                        }
                        if (anim.isRunning()) {
                            anim.stop();
                        }
                        if (CountSound.isPlaying()) {
                            CountSound.stop();
                            CountSound.reset();
                        }
                        TimeSoundHandler.removeCallbacks(endSound);
                        finish();
                    }

                    else {
                        //when the velocity is noticeable
                        if (TimeSound.isPlaying()) {
                            TimeSound.stop();
                            TimeSound.reset();
                        }
                        if (anim.isRunning()) {
                            anim.selectDrawable(4);
                            anim.stop();
                        }


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
                                    editor.apply();
                                    timer.cancel();
                                    StopSound.start();
                                    StopSound.setLooping(false);
                                    if(CountSound.isPlaying()){
                                    CountSound.stop();
                                    CountSound.reset();}
                                    startActivity(new Intent(FinalScreen.this, SecondScreen.class));
                                    finish();

                                }
                            }
                        }, 0, period);


                    }
                }

            }, 3000);
            //code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(TimeSound.isPlaying()){
            TimeSound.stop();
            TimeSound.reset();
            }
        if(anim.isRunning()){
            anim.stop();
        }
        if(CountSound.isPlaying()){
            CountSound.stop();
            CountSound.reset();
        }
        if(StopSound.isPlaying()){
            StopSound.stop();
            StopSound.reset();
        }
        TimeSoundHandler.removeCallbacks(endSound);
        startActivity(new Intent(FinalScreen.this, SecondScreen.class));
        finish();
    }


    }