package np.com.surajgyawali.measurevelocity;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FinalScreen extends AppCompatActivity {
    @Override
    public void onBackPressed() { }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.final_screen_layout);
        //code for digital fonts
        Typeface CurrentScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView ScoreView = (TextView) findViewById(R.id.Current_Score);
        ScoreView.setTypeface(CurrentScore);

        Typeface HighScore = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
        TextView HighScoreView = (TextView) findViewById(R.id.High_Score);
        HighScoreView.setTypeface(HighScore);

        //code for frame animation
        ImageView imageView =(ImageView) findViewById(R.id.count_animation);
        imageView.setBackgroundResource(R.drawable.count_movi);
        final AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
        anim.start();
        anim.setOneShot(true);

        //code for audio while counting 1,2,3
        final MediaPlayer CountSound=MediaPlayer.create(this,R.raw.time);
        //to play sound for 3 sec since sound is of 4s ;4s-1s=3s
        CountSound.seekTo(1000);
        CountSound.start();
        CountSound.setLooping(false);
        CountSound.setVolume(100,100);

        CountSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {

                CountSound.reset();
                CountSound.release();
                //to go back to previous activity in 3 secs
               finish();
            }
        });


            //
        //code to return back to second screen in 3 secs

//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                finish();
//            }
//        }, 3000);

        //code for banner ad
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }


    }