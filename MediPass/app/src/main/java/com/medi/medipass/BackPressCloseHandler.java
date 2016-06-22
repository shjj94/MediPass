package com.medi.medipass;

import android.app.Activity;
import android.widget.Toast;

/**
 * Created by Sohyeon on 2016-04-03.
 */
public class BackPressCloseHandler {
    private long backkey_pressedTime = 0;
    private Toast toast;

    private Activity activity;

    public BackPressCloseHandler(Activity context) {
        this.activity = context;
    }

    //처음 뒤로가기 버튼이 눌린 시간을 기억하고, 다음 눌린 시간이 2초이내면 종료한다.
    public void onBackPressred() {
        if (System.currentTimeMillis() > backkey_pressedTime + 2000) {
            backkey_pressedTime = System.currentTimeMillis();
            showGuide();
            return;
        }
        if (System.currentTimeMillis() <= backkey_pressedTime + 2000) {
            activity.finish();
        }
    }

    public void showGuide() {
        Toast.makeText(activity, "'뒤로'버튼 한번 더 누르시면 종료됩니다", Toast.LENGTH_SHORT).show();
    }
}
