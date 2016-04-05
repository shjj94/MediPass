package com.medi.medipass;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class Login extends AppCompatActivity {

    public static Activity login_activity;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login_activity = Login.this;

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
}
