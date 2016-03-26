package com.medi.medipass;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class index extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_login = (Button)findViewById(R.id.bt_login);
        Button bt_join = (Button)findViewById(R.id.bt_join);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_login = new Intent(getApplicationContext(), main.class);
                startActivity(intent_login);
            }
        });

        bt_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_join = new Intent(getApplicationContext(), join.class);
                startActivity(intent_join);
            }
        });
    }
}
