package com.org.parking.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.org.parking.R;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import cn.bmob.v3.Bmob;

public class MainActivity extends AppCompatActivity {
    public Button main_sign_up,main_sign_in;
    public SharedPreferences userdata,controllerdata;
    CarouselView carouselView;
    int [] img = {R.drawable.main_photo4,R.drawable.main_photo2,R.drawable.main_photo3};
    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        Bmob.initialize(this, "f3f1aacf8a94e08ee236c7e4bb7533c1");
        main_sign_up=findViewById(R.id.main_sign_up);
        main_sign_in=findViewById(R.id.main_sign_in);
        carouselView=findViewById(R.id.carouselView);
        carouselView.setPageCount(img.length);
        carouselView.setImageListener(imageListener);
        setlisten();
    }


    private void setlisten() {
        View.OnClickListener onClick = new Onclick();
        main_sign_up.setOnClickListener(onClick);
        main_sign_in.setOnClickListener(onClick);
    }


    //图片轮播
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(img[position]);
        }
    };



    private class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.main_sign_up:
                    intent=new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.main_sign_in:
                    if(userdata==null){
                        userdata = getSharedPreferences("user",MODE_PRIVATE);
                    }
                    if(controllerdata==null){
                        controllerdata = getSharedPreferences("controller", Context.MODE_PRIVATE);
                    }
                    Boolean user_islogin = userdata.getBoolean("user_isused", false);
                    Boolean controller_islogin = controllerdata.getBoolean("user_isused",false);
                    Log.d(TAG,"状态为：" + userdata.getBoolean("user_isused", false));
                    Log.d(TAG,"管理者状态为：" + controllerdata.getBoolean("user_isused", false));
                    if(user_islogin){
                        intent=new Intent(MainActivity.this,UserActivity.class);
                        startActivity(intent);
                    }else if(controller_islogin){
                        intent=new Intent(MainActivity.this,IntendantActivity.class);
                        startActivity(intent);
                    }else{
                        intent=new Intent(MainActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                    break;
            }

        }
    }

}