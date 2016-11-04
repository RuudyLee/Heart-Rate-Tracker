package ruudylee.com.heartratetracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.support.wearable.view.WearableListView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class DataActivity extends Activity {

    private static final String TAG = DataActivity.class.getName();

    private String filename;

    private WearableListView mListView;
    private ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mListView = (WearableListView) stub.findViewById(R.id.listView1);

                mListView.setAdapter(new MyAdapter(DataActivity.this, listItems));
            }
        });

        filename = this.getResources().getString(R.string.filename);

        getListItems();
    }

    private void getListItems() {
        listItems = new ArrayList<>();

        try {
            Context context = getApplicationContext();
            InputStream inputStream = context.openFileInput(filename);

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString;

                while ((receiveString = bufferedReader.readLine()) != null) {
                    listItems.add(receiveString);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void returnToMenu(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    private class MyAdapter extends WearableListView.Adapter {
        private final LayoutInflater mInflater;
        private ArrayList<String> data;

        private MyAdapter(Context context, ArrayList<String> listItems) {
            mInflater = LayoutInflater.from(context);
            data = listItems;
        }

        @Override
        public WearableListView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new WearableListView.ViewHolder(
                    mInflater.inflate(R.layout.row_wear_list, null));
        }

        @Override
        public void onBindViewHolder(WearableListView.ViewHolder holder, int position) {
            TextView view = (TextView) holder.itemView.findViewById(R.id.row_tv_name);
            view.setText(data.get(position));
            holder.itemView.setTag(position);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }
    }
}
