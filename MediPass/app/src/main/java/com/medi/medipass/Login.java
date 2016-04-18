package com.medi.medipass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    public static Activity login_activity;// 해당 activity를 담을 변수
    private BackPressCloseHandler backPressCloseHandler;// 뒤로가기 버튼 등록(두번 터치시 종료에 사용)

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_activity = Login.this;// 현재 activity를 변수에 넣는다
        backPressCloseHandler = new BackPressCloseHandler(this);// 뒤로가기 버튼 객체 생성성

        /* 로그인하기, 회원가입하기 버튼 등록 */
        Button bt_login = (Button)findViewById(R.id.bt_login);
        Button bt_join = (Button)findViewById(R.id.bt_join);
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(getApplicationContext(), Home.class);
                startActivity(intent_login);
            }
        });
        bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_join = new Intent(getApplicationContext(), Join.class);
                startActivity(intent_join);
            }
        });
    }

    /* 뒤로가기 버튼 동작 시, 두번 눌러야 꺼지게 */
    @Override
    public void onBackPressed(){
        backPressCloseHandler.onBackPressred();
    }
}
