package com.medi.medipass;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MyPageLogInfo extends AppCompatActivity implements View.OnClickListener {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_page_loginfo);

        /* xml의 버튼을 찾아와서 임시객체에 등록 */
        Button bt_record = (Button) findViewById(R.id.bottom_mypage_loginfo_record);
        Button bt_home = (Button) findViewById(R.id.bottom_mypage_loginfo_home);
        Button bt_mypage = (Button) findViewById(R.id.bottom_mypage_loginfo_mypage);

        /* onClick은 밑에 따로 메소드로 구현 */
        bt_record.setOnClickListener(this);
        bt_home.setOnClickListener(this);
        bt_mypage.setOnClickListener(this);

        bt_mypage.setSelected(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_mypage_loginfo_record:
                Intent intent_record = new Intent(getApplicationContext(), Record.class);
                intent_record.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_record);
                break;
            case R.id.bottom_mypage_loginfo_home:
                Intent intent_home = new Intent(getApplicationContext(), Home.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
                break;
            case R.id.bottom_mypage_loginfo_mypage:
                Intent intent_mypage = new Intent(getApplicationContext(), MyPage.class);
                intent_mypage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_mypage);
                break;
        }
    }
}