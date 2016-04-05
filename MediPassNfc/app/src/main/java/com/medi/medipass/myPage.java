package com.medi.medipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MyPage extends AppCompatActivity {

    Record record_activity = (Record)Record.record_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page);

        if(record_activity != null) {
            record_activity.finish();
        }

        Button myPage_logInfo = (Button)findViewById(R.id.mypage_info);
        myPage_logInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent_mypage_loginfo = new Intent(getApplicationContext(), MyPageLogInfo.class);
                startActivity(intent_mypage_loginfo);
            }
        });
    }
}
