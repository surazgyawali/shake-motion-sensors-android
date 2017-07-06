package np.com.surajgyawali.measurevelocity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

public class SecondScreen extends AppCompatActivity {
    ImageButton playButton;
    private InterstitialAd mInterstitialAd;
    ImageButton shareButton;
    ImageButton leaderBoardButton;
    ImageButton rateButton;
    static int COUNT=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // for full screen immersive mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //now show layout
        setContentView(R.layout.second_screen_layout);


        //to view digital Scores
        Typeface CurrentScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        ScoreView.setTypeface(CurrentScore);

        Typeface HighScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        HighScoreView.setTypeface(HighScore);
        //Todo:code for play button

        playButton = (ImageButton) findViewById(R.id.playButton);

        playButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                //code to increase button on click by 10%
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                playButton.startAnimation(myAnim);
                //launch new activity
            startActivity(new Intent(SecondScreen.this,FinalScreen.class));

            }

        });
        shareButton = (ImageButton)findViewById(R.id.share);

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View arg0){
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                shareButton.startAnimation(myAnim);
                //todo:write Code for share button


            }

        });
        leaderBoardButton = (ImageButton)findViewById(R.id.leaderboard);
                leaderBoardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                        leaderBoardButton.startAnimation(myAnim);
                        //todo:Write Code for leader board
                    }
                });

        rateButton = (ImageButton)findViewById(R.id.rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                rateButton.startAnimation(myAnim);
                //code to redirect to app page
//                Uri appUri1=Uri.parse("market:://details?id="+SecondScreen.this.getPackageName());
//                //test uri
//                Uri appUri2=Uri.parse("https://play.google.com/store/apps/details?id=com.epicactiononline.ffxv.ane&hl=en");
////
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("market:://details?id="+SecondScreen.this.getPackageName())));
                    } catch (android.content.ActivityNotFoundException c) {
                        startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse("https://play.google.com/store/apps/details?id="+SecondScreen.this.getPackageName())));
                    }
            }
        });

//code for inter
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
//code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    @Override
    public void onResume(){
        super.onResume();
        COUNT++;
       float rem=COUNT%5;
        if(rem==0)
            if (mInterstitialAd.isLoaded()) {
                mInterstitialAd.show();
            }
        else
            Toast.makeText(this, "Count="+COUNT, Toast.LENGTH_SHORT).show();

    }
    }



//code to count activity load time

//    //SharedPreferences sharedPreferences = getPreferences(MODE_PRIVATE);
//    int count = sharedPreferences.getInt("count", 0);
//if (count == N) {
//        ...
//        } else {
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("count", count + 1);
//        editor.commit();
//        }