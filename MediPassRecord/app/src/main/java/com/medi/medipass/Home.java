package com.medi.medipass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Home extends AppCompatActivity {

    public static Activity home_activity;

    /* 하단바를 이용해 home으로 올 때 직전activity를 끄기 위한 선언. activity들을 받아온다. */
    Login login_activity = (Login)Login.login_activity;

    private BackPressCloseHandler backPressCloseHandler;// 뒤로가기 버튼 등록(두번 터치시 종료에 사용)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        home_activity = Home.this;// 현재 activity를 변수에 넣는다.
        backPressCloseHandler = new BackPressCloseHandler(this);// 뒤로가기 버튼 객체 생성

        /* intent시 직전 activity종료 */
        login_activity.finish();

        /* home에 있는 버튼들을 등록 */
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

    @Override
    /* 뒤로가기 버튼 동작 시, 두번 눌러야 꺼지게 */
   public void onBackPressed(){
        backPressCloseHandler.onBackPressred();
    }
}
