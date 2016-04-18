package com.medi.medipass;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Elizabeth on 2016-04-07.
 */

public class WaitList extends AppCompatActivity {
    /* 로그인 안하고 대기인원 확인하기 */
    Record record_activity = (Record)Record.record_activity;
    public static Activity wait_activity;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wait_list);

        if(record_activity != null) {
            record_activity.finish();
        }

        wait_activity=WaitList.this;

    }
}
