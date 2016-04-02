package com.medi.medipass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class Record extends AppCompatActivity implements View.OnClickListener{

    //View frag_base; //진료기록 이외에서 쓸 때 이 fragment를 사용
    View frag_record; //진료기록 메뉴에서는 리스트/캘린더버튼이 들어가면, 그 버튼만큼 fragment 크기가 줄어들기 때문에 진료기록에서만 이 fragment사용

    public final int FRAGMENT_RECORD = 0;
    public final int FRAGMENT_RECORD_LIST = 1;
    public final int FRAGMENT_RECORD_CAL = 2;

    int current_fragment_index = FRAGMENT_RECORD_LIST;

    LinearLayout record;

    //static activity선언. 화면전환시 사용
    //http://muzesong.tistory.com/entry/%EC%95%88%EB%93%9C%EB%A1%9C%EC%9D%B4%EB%93%9C-%ED%98%84%EC%9E%AC-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EB%8B%A4%EB%A5%B8-%EC%95%A1%ED%8B%B0%EB%B9%84%ED%8B%B0-%EC%A2%85%EB%A3%8C%ED%95%98%EA%B8%B0
    public static Activity record_activity;
    Home home_activity = (Home)Home.home_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_menu);

        if(home_activity != null) {
            home_activity.finish();
        }

        //frag_base = (View)findViewById(R.id.ll_fragment_base);
        frag_record = (View)findViewById(R.id.ll_fragment_record);

        /* xml의 버튼을 찾아와서 임시객체에 등록 */
        Button bt_list = (Button)findViewById(R.id.up_menu_list);
        Button bt_cal = (Button)findViewById(R.id.up_menu_cal);
        Button bt_record = (Button)findViewById(R.id.button_record);
        Button bt_home = (Button)findViewById(R.id.button_home);
        Button bt_mypage = (Button)findViewById(R.id.button_mypage);

        /* onClick은 밑에 따로 메소드로 구현 */
        bt_record.setOnClickListener(this);
        bt_home.setOnClickListener(this);
        bt_mypage.setOnClickListener(this);
        bt_list.setOnClickListener(this);
        bt_cal.setOnClickListener(this);

        //activity객체에 현재 클래스를 담아준다
        record_activity = Record.this;

        /* 안보이게 설정했던 '리스트', '캘린더'버튼이 들어있는 화면을 보여준다 */
        record = (LinearLayout)findViewById(R.id.frag_record);
        record.setVisibility(View.VISIBLE);

        fragmentReplace(current_fragment_index);
    }

    /* 넘겨받은 fragment index를 가지고 getFragment메소드를 가서 실질적인 fragment의 객체 생성을 하고 그 객체를 리턴 */
    public void fragmentReplace(int new_fragment_index){
        Fragment new_fragment = null;
        new_fragment = getFragment(new_fragment_index);

        /* replace fragment */
        final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.ll_fragment_record, new_fragment);

        /* commit the transaction(변경사항을 적용) */
        transaction.commit();
    }

    private Fragment getFragment(int idx){
        Fragment newFragment = null;

        switch(idx){
            case FRAGMENT_RECORD:
                newFragment = new RecordList();
                break;
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
    public void onClick(View v){
        switch(v.getId()){
            case R.id.button_record:
                current_fragment_index = FRAGMENT_RECORD;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.button_home:
                Intent intent_home = new Intent(getApplicationContext(), Home.class);
                startActivity(intent_home);
                record.setVisibility(View.INVISIBLE);
                break;
            case R.id.button_mypage:
                Intent intent_mypage = new Intent(getApplicationContext(), MyPage.class);
                startActivity(intent_mypage);
                record.setVisibility(View.INVISIBLE);
                break;
            case R.id.up_menu_list:
                current_fragment_index = FRAGMENT_RECORD_LIST;
                fragmentReplace(current_fragment_index);
                break;
            case R.id.up_menu_cal:
                current_fragment_index = FRAGMENT_RECORD_CAL;
                fragmentReplace(current_fragment_index);
                break;
        }
    }
}