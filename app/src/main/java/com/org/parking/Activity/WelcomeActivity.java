package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.org.parking.R;

import cn.bmob.v3.Bmob;

public class WelcomeActivity extends AppCompatActivity {
    public SharedPreferences userdata, controllerdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Bmob.initialize(this, "f3f1aacf8a94e08ee236c7e4bb7533c1");
        if (userdata == null) {
            userdata = getSharedPreferences("user", MODE_PRIVATE);
        }
        if (controllerdata == null) {
            controllerdata = getSharedPreferences("controller", Context.MODE_PRIVATE);
        }
        Boolean user_islogin = userdata.getBoolean("user_isused", false);
        Boolean controller_islogin = controllerdata.getBoolean("user_isused", false);
        Intent intent = new Intent();
        if (user_islogin) {
            intent = new Intent(WelcomeActivity.this, UserActivity.class);
            startActivity(intent);
            finish();
        } else if (controller_islogin) {
            intent = new Intent(WelcomeActivity.this, IntendantActivity.class);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(WelcomeActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        }
    }
}