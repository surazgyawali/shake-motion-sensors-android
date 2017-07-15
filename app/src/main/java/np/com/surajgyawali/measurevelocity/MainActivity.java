package np.com.surajgyawali.measurevelocity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.widget.TextView;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private final float NOISE = (float) 2.0;
    float[] values=null;
    float[] last_values = null;
    float[] velocity = null;
    float tVelocity =0;
    long last_timestamp = 0;
    static final float NS2S = 1.0f / 1000000000.0f;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer , SensorManager.SENSOR_DELAY_GAME);

        Timer updateTimer = new Timer("velocityUpdate");
        updateTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {

            }
        }, 0, 1000);
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // can be safely ignored for this demo
    }
    @Override
    public void onSensorChanged(SensorEvent event) {

        TextView tvX= (TextView)findViewById(R.id.x_axis);
        TextView tvY= (TextView)findViewById(R.id.y_axis);
        TextView tvZ= (TextView)findViewById(R.id.z_axis);
        TextView tvV= (TextView)findViewById(R.id.velocity);
        TextView tvS= (TextView)findViewById(R.id.speed);
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];
        if (!mInitialized) {
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText("0.0");
            tvY.setText("0.0");
            tvZ.setText("0.0");
            mInitialized = true;
        } else {
            float deltaX = Math.abs(mLastX - x);
            float deltaY = Math.abs(mLastY - y);
            float deltaZ = Math.abs(mLastZ - z);
            if (deltaX < NOISE) deltaX = (float)0.0;
            if (deltaY < NOISE) deltaY = (float)0.0;
            if (deltaZ < NOISE) deltaZ = (float)0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;
            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));
            values[0]=deltaX;
            values[1]=deltaY;
            values[3]=deltaZ;
        }
       // code to find real time velocity
        if(last_values != null){
            float dt = (event.timestamp - last_timestamp) * NS2S;

            for(int index = 0; index < 3;++index){
                velocity[index] += (event.values[index] + last_values[index])/2 * dt;
            }
        }
        else{
            last_values = new float[3];
            velocity = new float[3];
            velocity[0] = velocity[1] = velocity[2] = 0f;
        }
        System.arraycopy(event.values, 0, last_values, 0, 3);
        last_timestamp = event.timestamp;
        tvV.setText(Float.toString(tVelocity));


    }


}
