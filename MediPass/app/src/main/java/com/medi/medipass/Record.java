package com.medi.medipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Record extends AppCompatActivity implements View.OnClickListener {
    View frag_record; // fragment View

    public final int FRAGMENT_RECORD_LIST = 1;
    public final int FRAGMENT_RECORD_CAL = 2;

    int current_fragment_index = FRAGMENT_RECORD_LIST;// 기본화면은 list

    Button bt_list, bt_cal;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record);

        frag_record = (View) findViewById(R.id.ll_fragment_record);

        /* xml의 버튼을 찾아와서 임시객체에 등록 */
        bt_list = (Button) findViewById(R.id.up_menu_list);
        bt_cal = (Button) findViewById(R.id.up_menu_cal);
        Button bt_record = (Button) findViewById(R.id.bottom_record_record);
        Button bt_home = (Button) findViewById(R.id.bottom_record_home);
        Button bt_mypage = (Button) findViewById(R.id.bottom_record_mypage);

        /* onClick은 밑에 따로 메소드로 구현 */
        bt_record.setOnClickListener(this);
        bt_home.setOnClickListener(this);
        bt_mypage.setOnClickListener(this);
        bt_list.setOnClickListener(this);
        bt_cal.setOnClickListener(this);

        bt_list.setSelected(true);
        bt_record.setSelected(true);

        fragmentReplace(current_fragment_index);
    }

    /* 넘겨받은 fragment index를 가지고 getFragment메소드를 가서 실질적인 fragment의 객체 생성을 하고 그 객체를 리턴 */
    public void fragmentReplace(int new_fragment_index) {
        Fragment new_fragment = null;
        new_fragment = getFragment(new_fragment_index);

        /* replace fragment */
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_fragment_record, new_fragment);

        /* commit the transaction(변경사항을 적용) */
        transaction.commit();
    }

    private Fragment getFragment(int idx) {
        Fragment newFragment = null;

        switch (idx) {
            case FRAGMENT_RECORD_LIST:
                newFragment = new RecordList();
                break;
            case FRAGMENT_RECORD_CAL:
                newFragment = new RecordCal();
                break;
            default:
                break;
        }

        return newFragment;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bottom_record_record:
                Intent intent_record = new Intent(getApplicationContext(), Record.class);
                intent_record.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_record);
                break;
            case R.id.bottom_record_home:
                Intent intent_home = new Intent(getApplicationContext(), Home.class);
                intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_home);
                break;
            case R.id.bottom_record_mypage:
                Intent intent_mypage = new Intent(getApplicationContext(), MyPage.class);
                intent_mypage.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent_mypage);
                break;
            case R.id.up_menu_list:
                bt_list.setSelected(true);
                bt_cal.setSelected(false);
                current_fragment_index = FRAGMENT_RECORD_LIST;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.up_menu_cal:
                bt_list.setSelected(false);
                bt_cal.setSelected(true);
                current_fragment_index = FRAGMENT_RECORD_CAL;
                fragmentReplace(current_fragment_index);
                break;
        }
    }

    /* 뒤로가기 버튼 동작 시, Home으로 가기 */
    //http://diyall.tistory.com/781
    //http://comxp.tistory.com/109
    @Override
    public void onBackPressed() {
        Intent intent_home = new Intent(getApplicationContext(), Home.class);
        intent_home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent_home);
    }
}