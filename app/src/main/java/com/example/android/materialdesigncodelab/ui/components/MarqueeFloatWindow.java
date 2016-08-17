package com.example.android.materialdesigncodelab.ui.components;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.android.materialdesigncodelab.R;
import com.example.android.materialdesigncodelab.widget.FloatWindowHostService;

/**
 * Created by lixindong on 8/9/16.
 */
public class MarqueeFloatWindow extends FloatWindowHostService {
    public static final String TEXT = "text";
    String text = "";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        text = intent.getStringExtra(TEXT);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected View getView() {
        View v = LayoutInflater.from(getApplicationContext()).inflate(R.layout.view_marquee, null);
        TextView tv = (TextView) v.findViewById(R.id.text);
        tv.setText(text);
        return v;
    }
}
