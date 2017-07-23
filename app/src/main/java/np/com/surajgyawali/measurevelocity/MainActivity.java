package np.com.surajgyawali.measurevelocity;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private float mLastX, mLastY, mLastZ;
    private boolean mInitialized;
    private SensorManager mSensorManager;


    double resultant_accleration;
    ArrayList<Double> accln = new ArrayList<>();
    double avgaccln;
    int score;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Sensor mAccelerometer;
        mInitialized = false;
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        final SensorEventListener listener = this;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                mSensorManager.unregisterListener(listener);
                //avgaccln=Collections.max(accln);
                avgaccln=calculateAverage(accln);
                System.out.println("Average accleration:"+ avgaccln);
                score=(int)Math.round((avgaccln/100)*1000);
                TextView tvS = (TextView)findViewById(R.id.score);
                tvS.setText(Double.toString(score));
            }
        }, 2000);







    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
//    }
    @Override
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
        final float NOISE = (float) 15.0;
        TextView tvX = (TextView) findViewById(R.id.x_axis);
        TextView tvY = (TextView) findViewById(R.id.y_axis);
        TextView tvZ = (TextView) findViewById(R.id.z_axis);
       TextView tvA = (TextView) findViewById(R.id.accln);


        //enter your code here
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
            if (deltaX < NOISE) deltaX = (float) 0.0;
            if (deltaY < NOISE) deltaY = (float) 0.0;
            if (deltaZ < NOISE) deltaZ = (float) 0.0;
            mLastX = x;
            mLastY = y;
            mLastZ = z;

            resultant_accleration =Math.sqrt((deltaX*deltaX)+(deltaY*deltaY)+(deltaZ*deltaZ));

            accln.add(resultant_accleration);

            tvX.setText(Float.toString(deltaX));
            tvY.setText(Float.toString(deltaY));
            tvZ.setText(Float.toString(deltaZ));



        }
        }

    private double calculateAverage(List<Double> nums) {
        Double sum = 0.0;
        if(!nums.isEmpty()) {
            for (Double mark : nums) {
                sum += mark;
            }
            return sum.doubleValue() / nums.size();
        }
        return sum;
    }
    }