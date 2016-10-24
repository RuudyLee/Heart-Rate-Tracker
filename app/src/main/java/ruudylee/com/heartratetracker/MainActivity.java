package ruudylee.com.heartratetracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.view.WatchViewStub;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private static final String TAG = MainActivity.class.getName();
    private static final int MY_PERMISSIONS_REQUEST_BODY_SENSORS = 1;

    private boolean havePermission = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check for BODY_SENSORS permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BODY_SENSORS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.BODY_SENSORS },
                    MY_PERMISSIONS_REQUEST_BODY_SENSORS);
        }
        else {
            // Already have permissions
            havePermission = true;
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_BODY_SENSORS: {
                // If request is cancelled, the result arrays are empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted
                    havePermission = true;
                }
                return;
            }
        }
    }

    public void startTracking(View view) {
        if (havePermission) {
            Intent intent = new Intent(this, TrackActivity.class);
            startActivity(intent);
        }
        else {
            // no permission, display a toast
            Context context = getApplicationContext();
            CharSequence text = "Requires body sensor permissions.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }

    public void viewData(View view) {
        Intent intent = new Intent(this, DataActivity.class);
        startActivity(intent);
    }
}
