package com.pa1.loginui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //navigation drawer website for detail info   https://stablekernel.com/using-fragments-to-simply-android-nagigation-drawers/
//login using sqlite: http://computerjunkies.com.android-login-registration-screen-with-sqlite-database-example/
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.btnLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getApplication(),Main2Activity.class);
                startActivity(intent);
            }
        });
    }
}
