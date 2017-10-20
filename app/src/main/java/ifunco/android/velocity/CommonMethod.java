package ifunco.android.velocity;
import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import static com.google.android.gms.ads.AdSize.SMART_BANNER;


public class
CommonMethod extends Application {


    AdView adView;
    static MediaPlayer Sound= new MediaPlayer();

    @Override
    public void onCreate() {
        super.onCreate();

        adView = new AdView(getApplicationContext());
        adView.setAdSize(SMART_BANNER);
        adView.setAdUnitId(getString(R.string.adUnitId1));
        // Request for Ads
        AdRequest adRequest = new AdRequest.Builder().build();

        // Load ads into Banner Ads
        adView.loadAd(adRequest);

    }

    public void loadAd(LinearLayout layAd) {

        // Locate the Banner Ad in activity xml
        if (adView.getParent() != null) {
            ViewGroup tempVg = (ViewGroup) adView.getParent();
            tempVg.removeView(adView);
        }

        layAd.addView(adView);

    }

    static void soundPlayer(Context ctx, int raw_id){

            //Sound.release();
        if(Sound!=null)
        {
            if(Sound.isPlaying()){
                Sound.stop();
            }
            Sound.reset();
            Sound.release();
        }
            Sound = MediaPlayer.create(ctx, raw_id);
            Sound.start();
            Sound.setLooping(false);
        }


}