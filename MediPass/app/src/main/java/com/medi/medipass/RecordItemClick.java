package com.medi.medipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by shjjthjj on 2016-05-09.
 */
public class RecordItemClick extends AppCompatActivity {
    TextView tv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_item_click);

        Intent intent = getIntent();
        String name = intent.getExtras().getString("name");

        tv = (TextView)findViewById(R.id.textView);
        tv.setText(name);
    }
}
