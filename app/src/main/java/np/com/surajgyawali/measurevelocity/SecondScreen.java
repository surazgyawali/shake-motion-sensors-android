package np.com.surajgyawali.measurevelocity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class SecondScreen extends AppCompatActivity {
    ImageButton playButton;
    ImageButton shareButton;
    ImageButton leaderBoardButton;
    ImageButton rateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // for full screen immersive mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //now show layout
        setContentView(R.layout.second_screen_layout);
        //to view digital Scores
        Typeface CurrentScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        ScoreView.setTypeface(CurrentScore);

        Typeface HighScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        HighScoreView.setTypeface(HighScore);
        //code for play button

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
                //write Code for share button


            }

        });
        leaderBoardButton = (ImageButton)findViewById(R.id.leaderboard);
                leaderBoardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                        leaderBoardButton.startAnimation(myAnim);
                        //Write Code for leader board
                    }
                });

        rateButton = (ImageButton)findViewById(R.id.rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this,R.anim.button_anim);
                rateButton.startAnimation(myAnim);
                //Write Code for rate button

            }
        });

//        public void enlarge(View view) {
//            ImageButton button = (ImageButton)findViewById(R.id.button);
//            final Animation myAnim = AnimationUtils.loadAnimation(this, R.anim.button_anim);
//            button.startAnimation(myAnim);
//        }

//code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }




    }