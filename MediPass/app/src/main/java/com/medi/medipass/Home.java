package com.medi.medipass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    public static Activity home_activity;
    //하단바를 이용해 home으로 올 때 직전activity를 끄기 위한 선언
    Login login_activity = (Login)Login.login_activity;
    Record record_activity = (Record)Record.record_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        home_activity = Home.this;

        //
        if(login_activity != null) { login_activity.finish(); }
        if(record_activity != null) { record_activity.finish(); }

        Button bt_record = (Button)findViewById(R.id.home_record);
        Button bt_myPage = (Button) findViewById(R.id.home_mypage);

        bt_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_record = new Intent(getApplicationContext(), Record.class);
                startActivity(intent_record);
            }
        });

        bt_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mypage = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent_mypage);
            }
        });
    }
}
