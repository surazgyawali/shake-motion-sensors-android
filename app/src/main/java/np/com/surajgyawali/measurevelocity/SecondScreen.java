package np.com.surajgyawali.measurevelocity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;


public class SecondScreen extends AppCompatActivity{

    ImageButton playButton;
    private InterstitialAd mInterstitialAd;
    ImageButton shareButton;
    ImageButton leaderBoardButton;
    ImageButton rateButton;
    static int COUNT = 0;
    int score;
    int highscore;
    SharedPreferences gamedata;
    SharedPreferences.Editor editor;
    //leader board
    private GameHelper gameHelper;
    private final static int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // for full screen immersive mode
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_screen_layout);
//leader board
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){
                Toast.makeText(SecondScreen.this, "Sign in failed!!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSignInSucceeded(){
                Toast.makeText(SecondScreen.this, "You are signed in.", Toast.LENGTH_SHORT).show();
            }
        };

        gameHelper.setup(gameHelperListener);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        gamedata = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        editor= gamedata.edit();


        COUNT = gamedata.getInt("counter", 0);
        COUNT++;
        editor.putInt("counter", COUNT);
        editor.commit();


        highscore = gamedata.getInt("HIGH_SCORE", 0);
        score = gamedata.getInt("CURRENT_SCORE", 0);

        Typeface CurrentScore = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        ScoreView.setTypeface(CurrentScore);
        ScoreView.setText(Integer.toString(score));

        Typeface HighScore = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/digital-7.ttf");
        TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        HighScoreView.setTypeface(HighScore);
        HighScoreView.setText(Integer.toString(highscore));

        submitScore(highscore);



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                startActivity(new Intent(SecondScreen.this, FinalScreen.class));
            }
        });
        playButton = (ImageButton) findViewById(R.id.playButton);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                //code to increase button on click by 10%
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.button_anim);
                playButton.startAnimation(myAnim);

                Toast.makeText(SecondScreen.this, "COUNT:"+COUNT, Toast.LENGTH_SHORT).show();
                if(COUNT>=5) {
                    if(!mInterstitialAd.isLoaded()){
                        editor.putInt("counter", 5);
                        editor.apply();
                        startActivity(new Intent(SecondScreen.this, FinalScreen.class));}
                    else{
                        mInterstitialAd.show();
                        editor.remove("counter");
                        editor.apply();}
                }else
                    {
                startActivity(new Intent(SecondScreen.this, FinalScreen.class));}

            }

        });
        shareButton = (ImageButton) findViewById(R.id.share);

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.button_anim);
                shareButton.startAnimation(myAnim);
                shareGame();
            }

        });
        leaderBoardButton = (ImageButton) findViewById(R.id.leaderboard);
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.button_anim);
                leaderBoardButton.startAnimation(myAnim);
                showScore();

            }
        });

        rateButton = (ImageButton) findViewById(R.id.rate);
        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.button_anim);
                rateButton.startAnimation(myAnim);

                Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market:://details?id=" + SecondScreen.this.getPackageName()));
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);

                } catch (android.content.ActivityNotFoundException c) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + SecondScreen.this.getPackageName())));
                }
            }
        });


//code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }


    private void shareGame() {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "I'm playing this awesome game. You can check out this game by clicking here: " + Uri.parse("https://play.google.com/store/apps/details?id=" + SecondScreen.this.getPackageName());
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Try this awesome game!!");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }
//leader board
    protected void signIn()
    {
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        gameHelper.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        gameHelper.onActivityResult(requestCode, resultCode, data);
    }

    public void submitScore(int highScore)
    {
        if (gameHelper.isSignedIn() == true)
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highest), highScore);
        }
    }
    public void showScore()
    {
        if (gameHelper.isSignedIn())
        {
            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highest)), requestCode);
        }
        else
        {
            try
            {
                runOnUiThread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        signIn();
                    }
                });
            }
            catch (Exception e)
            {
                Toast.makeText(SecondScreen.this, "Couldn't login.", Toast.LENGTH_SHORT).show();
            }
        }
    }

}