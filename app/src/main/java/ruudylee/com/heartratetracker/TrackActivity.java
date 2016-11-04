package ruudylee.com.heartratetracker;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.TextView;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class TrackActivity extends WearableActivity implements
        SensorEventListener {

    private static final String TAG = TrackActivity.class.getName();
    private String filename;

    SensorManager mSensorManager;
    Sensor mHeartRateSensor;
    TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);
        setAmbientEnabled();

        // Set up sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mHeartRateSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);

        filename = this.getResources().getString(R.string.filename);
        mTextView = (TextView) findViewById(R.id.tv_tracking);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mHeartRateSensor != null) {
            mSensorManager.registerListener(this, mHeartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mHeartRateSensor != null) {
            mSensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onEnterAmbient(Bundle ambientDetails) {
        super.onEnterAmbient(ambientDetails);

    }

    @Override
    public void onExitAmbient() {
        super.onExitAmbient();

    }

    @Override
    public void onUpdateAmbient() {
        super.onUpdateAmbient();
        // Update the content
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            String value = String.valueOf(event.values[0]);

            try {
                Context context = getApplicationContext();
                OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(filename, Context.MODE_APPEND));
                PrintWriter writer = new PrintWriter(outputStreamWriter);
                writer.println(value);
                writer.close();
                outputStreamWriter.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mTextView.setText(value);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void stopTracking(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
