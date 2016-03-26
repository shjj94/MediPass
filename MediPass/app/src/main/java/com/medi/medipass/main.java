package com.medi.medipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        Button bt_myPage = (Button) findViewById(R.id.main_myPage);

        bt_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_mypage = new Intent(getApplicationContext(), myPage.class);
                startActivity(intent_mypage);
            }
        });
    }
}
