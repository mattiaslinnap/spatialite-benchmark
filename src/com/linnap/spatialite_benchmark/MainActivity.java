package com.linnap.spatialite_benchmark;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import jsqlite.*;
import org.w3c.dom.Text;

import java.util.Random;

public class MainActivity extends Activity {

    public static final String TAG = "Spatialite";
    Database db = null;
    Random random = new Random();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        attachUiEvents();
        refreshCounters();
    }

    public void onResume() {
        super.onResume();
        db = SqlHelper.open(this);
    }

    public void onPause() {
        super.onPause();
        SqlHelper.close(db);
        db = null;
    }

    public void attachUiEvents() {
        ((Button)findViewById(R.id.reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SqlHelper.truncate(db);
                refreshCounters();
            }
        });
        ((Button)findViewById(R.id.addrow)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addrow();
                refreshCounters();
            }
        });
    }

    public void refreshCounters() {
        ((TextView)findViewById(R.id.rowcount)).setText(String.format("Row count: %d", SqlHelper.count(db)));
        ((TextView)findViewById(R.id.filesize)).setText(String.format("File bytes: %d", SqlHelper.size(this)));
    }

    public void addrow() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < 100; ++i) {
            sb.append("long text ");
        }

        SqlHelper.append(db,
                random.nextLong(),
                random.nextLong(),
                random.nextLong(),
                random.nextLong(),
                random.nextLong(),
                "tag",
                sb.toString());
    }
}
