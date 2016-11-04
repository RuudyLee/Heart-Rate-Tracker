package ruudylee.com.heartratetracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Pair<Integer, Integer>> heartRateData;

    LineGraphSeries<DataPoint> series;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        heartRateData = new ArrayList<>();

        // Register local broadcast receiver
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver, messageFilter);

    }

    public void buildGraph(View view) {
        if (heartRateData.size() > 0) {
            // Initialize graph
            double x, y;
            x = 0;

            GraphView graph = (GraphView) findViewById(R.id.graph);
            series = new LineGraphSeries<>();

            for (Pair<Integer, Integer> data : heartRateData) {
                x = data.second;
                y = data.first;

                series.appendData(new DataPoint(x, y), true, heartRateData.size());
            }
            graph.addSeries(series);
        }
    }

    public class MessageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String message = intent.getStringExtra("message");
            double heartRate = Double.parseDouble(message.substring(0, message.indexOf("#") - 1));
            int heartRateAsInt = (int) heartRate;
            int timeStamp = Integer.parseInt(message.substring(message.indexOf("#") + 1, message.length()));

            heartRateData.add(new Pair<>(heartRateAsInt, timeStamp));
        }
    }
}
