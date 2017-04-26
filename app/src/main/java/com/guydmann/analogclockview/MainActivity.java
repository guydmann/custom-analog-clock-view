package com.guydmann.analogclockview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.guydmann.customanalogclockview.PolarClock;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        PolarClock polarClock = (PolarClock) findViewById(R.id.analog_clock);
        polarClock.setAutoUpdate(true);
    }
}
