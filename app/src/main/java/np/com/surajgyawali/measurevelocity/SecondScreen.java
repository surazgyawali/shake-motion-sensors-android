package np.com.surajgyawali.measurevelocity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
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
            startActivity(new Intent(SecondScreen.this,FinalScreen.class));
            }

        });
        shareButton = (ImageButton)findViewById(R.id.share);

        shareButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public  void onClick(View arg0){
                //write Code for share button

            }

        });
        leaderBoardButton = (ImageButton)findViewById(R.id.leaderboard);
                leaderBoardButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Write Code for leader board
                    }
                });

        rateButton = (ImageButton)findViewById(R.id.rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Write Code for rate button

            }
        });
//code for score background

//code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }




    }