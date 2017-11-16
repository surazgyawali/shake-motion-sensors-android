package ifunco.android.velocity;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.GameHelper;

import static ifunco.android.velocity.CommonMethod.Sound;
import static ifunco.android.velocity.CommonMethod.mute;

public class SecondScreen extends AppCompatActivity{

    private static final int TIME_DELAY = 2000;
    private static long back_pressed;

    private InterstitialAd mInterstitialAd;
    static int COUNT = 0;
    int score;
    int high_score;
    boolean mute;
    SharedPreferences game_data;
    private MediaPlayer ButtonSound=new MediaPlayer();
    //leader board
    private GameHelper gameHelper;
    private final static int requestCode = 1;
    Intent FinalScreen=new Intent();

    LinearLayout layAd;
    CommonMethod commonMethod;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.second_screen_layout);
        layAd = (LinearLayout) findViewById(R.id.layad);
        commonMethod = (CommonMethod) getApplication();


//leader board
        gameHelper = new GameHelper(this, GameHelper.CLIENT_GAMES);
        gameHelper.enableDebugLog(false);

        GameHelper.GameHelperListener gameHelperListener = new GameHelper.GameHelperListener()
        {
            @Override
            public void onSignInFailed(){

                         }

            @Override
            public void onSignInSucceeded(){

            }
        };

        gameHelper.setup(gameHelperListener);




        final TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        final Button playButton = (Button) findViewById(R.id.playButton);
        final TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
         final Button rateButton = (Button) findViewById(R.id.rate);
        final Button shareButton = (Button) findViewById(R.id.share);
       final Button leaderBoardButton = (Button) findViewById(R.id.leaderboard);
        final CheckBox muteCheckBox = (CheckBox) findViewById(R.id.Mute);
        game_data = getSharedPreferences("GAME_DATA", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor= game_data.edit();




        if(ButtonSound!=null)
        {
            if(ButtonSound.isPlaying())
            {
                ButtonSound.stop();
            }
            ButtonSound.reset();
            ButtonSound.release();
        }
        ButtonSound = MediaPlayer.create(this,R.raw.button);
        ButtonSound.setLooping(false);



        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/alphamencondital.ttf");
        ScoreView.setTypeface(typeface);
        HighScoreView.setTypeface(typeface);

       final Intent goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market:://details?id=" + SecondScreen.this.getPackageName()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        final Intent goToMarketWeb = new Intent(Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + SecondScreen.this.getPackageName()));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            goToMarketWeb.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        }

        FinalScreen = new Intent(SecondScreen.this, FinalScreen.class);
        Animation scoreAnimation = AnimationUtils.loadAnimation(SecondScreen.this, R.anim.shake);

        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));

        mInterstitialAd.loadAd(new AdRequest.Builder().build());



        high_score = game_data.getInt("HIGH_SCORE", 0);
        score = game_data.getInt("CURRENT_SCORE", 0);
        mute=game_data.getBoolean("MUTE",false);
        muteCheckBox.setChecked(mute);


        ScoreView.setText(Integer.toString(score));
        HighScoreView.setText(Integer.toString(high_score));


        muteCheckBox.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mute = muteCheckBox.isChecked();
                editor.putBoolean("MUTE",mute);
                Toast.makeText(SecondScreen.this, "MUTE-STATE:"+mute, Toast.LENGTH_SHORT).show();
                editor.apply();
                editor.commit();
                mute(mute,ButtonSound);
            }
        });




        if(savedInstanceState == null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                String method = extras.getString("AnimateScore");

                if (method != null && method.equals("myAnim")) {
                    ScoreView.setText(Integer.toString(score));
                    HighScoreView.setText(Integer.toString(high_score));
                    ScoreView.startAnimation(scoreAnimation);

                }
            }

        }

        submitScore(high_score);


        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                COUNT=0;
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                startActivity(FinalScreen);
                finish();
            }
        });


        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if(Sound.isPlaying()&& Sound!=null){
                    Sound.stop();
                }
                    ButtonSound.start();

                if(COUNT>=5) {
                    if(!mInterstitialAd.isLoaded()){
                        COUNT=5;
                        startActivity(FinalScreen);
                    finish();}
                    else{
                        mInterstitialAd.show();
                        }
                }   else{

                startActivity(FinalScreen);
                finish();}

            }

        });

        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                ButtonSound.start();

                shareGame();
            }

        });
        leaderBoardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSound.start();
                showScore();

            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ButtonSound.start();


                try {
                    startActivity(goToMarket);

                } catch (ActivityNotFoundException c) {
                    startActivity(goToMarketWeb);
                }
            }
        });


        commonMethod.loadAd(layAd);
        mute(mute,ButtonSound);


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
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
            if(Sound!=null)
            {
                if(Sound.isPlaying()){
                    Sound.stop();
                }
                Sound.reset();
                Sound.release();
            }
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
        //moveTaskToBack(true);

    }

    @Override
    protected void onResume() {
        commonMethod.loadAd(layAd);
        super.onResume();
        COUNT++;
    }


    @Override
    protected void onStart() {
        super.onStart();
        gameHelper.onStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //gameHelper.onStop();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mInterstitialAd.setAdListener(null);
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
        if (gameHelper.isSignedIn())
        {
            Games.Leaderboards.submitScore(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highest),highScore);
        }
    }

    public void showScore()
    {
        if (!gameHelper.isSignedIn()) {
            Toast.makeText(SecondScreen.this, "Not logged in to Google Play.", Toast.LENGTH_SHORT).show();
            gameHelper.beginUserInitiatedSignIn();


        } else {

            Games.Leaderboards.submitScore(gameHelper.getApiClient(), getString(R.string.leaderboard_highest), high_score);

            startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(),
                    getString(R.string.leaderboard_highest)), requestCode);
        }
    }

}