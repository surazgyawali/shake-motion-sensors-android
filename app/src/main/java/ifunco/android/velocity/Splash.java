package ifunco.android.velocity;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity{
    private Timer timer;
    private ProgressBar progressBar;
    private int i=0;
    TextView textView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        //now show layout
//        setContentView(R.layout.activity_splash);
//
//
//        //code for progressbar
//        progressBar=(ProgressBar)findViewById(R.id.progressBar);
//        progressBar.setProgress(0);
//        Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/digital-7.ttf");
//
//        textView=(TextView)findViewById(R.id.textView);
//        textView.setTypeface(typeface);
//        textView.setText("");
//
//        final long period =15;
//        timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                //this repeats for every value of period variable in ms i.e changes the speed of progress
//                if (i<100){
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            textView.setText(String.valueOf(i)+"%");
//                        }
//                    });
//                    progressBar.setProgress(i);
//                    i++;
//                }else{
//                    //closing the timer
//                    timer.cancel();
//                    Intent secondScreen =new Intent(Splash.this,SecondScreen.class);
//                    secondScreen.putExtra("SignIn","signIn");
//                    startActivity(secondScreen);
//                    // close this activity
//                    finish();
//
//                }
//            }
//        }, 0, period);
        startActivity(new Intent(Splash.this, SecondScreen.class));

        // close splash activity

        finish();
    }
}
