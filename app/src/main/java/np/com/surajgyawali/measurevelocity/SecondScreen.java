package np.com.surajgyawali.measurevelocity;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
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

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
//import com.google.android.gms.games.Games;
//import com.google.example.games.basegameutils.GameHelper;


public class SecondScreen extends AppCompatActivity{

    ImageButton playButton;
    private InterstitialAd mInterstitialAd;
    ImageButton shareButton;
    ImageButton leaderBoardButton;
    ImageButton rateButton;
    static int COUNT = 0;
    int score;
    int high_score;
    SharedPreferences game_data;
    SharedPreferences.Editor editor;
    MediaPlayer ButtonSound=new MediaPlayer();

    //leader board
    //private GameHelper gameHelper;
    private final static int requestCode = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_screen_layout);


//leader board
//        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
//        gameHelper.enableDebugLog(false);
//        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
//        gameHelper.enableDebugLog(false);
//
//        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
//        {
//            @Override
//            public void onSignInFailed(){
//                Toast.makeText(SecondScreen.this, "Sign in failed!!", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onSignInSucceeded(){
//                Toast.makeText(SecondScreen.this, "You are signed in.", Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        gameHelper.setup(gameHelperListener);
        TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        playButton = (ImageButton) findViewById(R.id.playButton);
        ButtonSound = MediaPlayer.create(this,R.raw.button);
        TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        rateButton = (ImageButton) findViewById(R.id.rate);
        shareButton = (ImageButton) findViewById(R.id.share);
        leaderBoardButton = (ImageButton) findViewById(R.id.leaderboard);


        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/digital-7.ttf");
        ScoreView.setTypeface(typeface);
        HighScoreView.setTypeface(typeface);

       final Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market:://details?id=" + SecondScreen.this.getPackageName()));

        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

       final Intent goToMarketWeb = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + SecondScreen.this.getPackageName()));

        goToMarketWeb.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        final Intent FinalScreen = new Intent(SecondScreen.this, FinalScreen.class);
        final Animation myAnim = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.button_anim);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        game_data = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        editor= game_data.edit();


        COUNT = game_data.getInt("counter", 0);
        COUNT++;
        editor.putInt("counter", COUNT);
        editor.commit();


        high_score = game_data.getInt("HIGH_SCORE", 0);
        score = game_data.getInt("CURRENT_SCORE", 0);


        ScoreView.setText(Integer.toString(score));

        HighScoreView.setText(Integer.toString(high_score));

        // submitScore(high_score);



        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                startActivity(FinalScreen);
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ButtonSound.start();
                ButtonSound.setLooping(false);
                playButton.startAnimation(myAnim);


                if(COUNT>=5) {
                    if(!mInterstitialAd.isLoaded()){
                        editor.putInt("counter", 5);
                        editor.apply();
                        startActivity(FinalScreen);}
                    else{
                        mInterstitialAd.show();
                        editor.remove("counter");
                        editor.apply();}
                }else
                    {
                startActivity(FinalScreen);}

            }

        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ButtonSound.start();
                ButtonSound.setLooping(false);
                shareButton.startAnimation(myAnim);
                shareGame();
            }

        });
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSound.start();
                ButtonSound.setLooping(false);
                leaderBoardButton.startAnimation(myAnim);

                //startActivity(new Intent(SecondScreen.this, MainActivity.class));

                //showScore();

            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSound.start();
                ButtonSound.setLooping(false);
                rateButton.startAnimation(myAnim);


                try {
                    startActivity(goToMarket);

                } catch (android.content.ActivityNotFoundException c) {
                    startActivity(goToMarketWeb);
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

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);

    }

    //leader board
//    protected void signIn()
//    {
//        gameHelper.onStart(this);
//    }
//
//    @Override
//    protected void onStop()
//    {
//        super.onStop();
//        gameHelper.onStop();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data)
//    {
//        super.onActivityResult(requestCode, resultCode, data);
//        gameHelper.onActivityResult(requestCode, resultCode, data);
//    }
//
//    public void submitScore(int highScore)
//    {
//        if (gameHelper.isSignedIn() == true)
//        {
//            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
//                    getString(R.string.leaderboard_highest), highScore);
//        }
//    }
//    public void showScore()
//    {
//        if (gameHelper.isSignedIn())
//        {
//            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
//                    getString(R.string.leaderboard_highest)), requestCode);
//        }
//        else
//        {
//            try
//            {
//                runOnUiThread(new Runnable()
//                {
//                    @Override
//                    public void run()
//                    {
//                        signIn();
//                    }
//                });
//            }
//            catch (Exception e)
//            {
//                Toast.makeText(SecondScreen.this, "Couldn't login.", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }

}