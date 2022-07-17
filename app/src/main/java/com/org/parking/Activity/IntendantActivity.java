package com.org.parking.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.parking.R;

public class IntendantActivity extends AppCompatActivity {
    public static String dc = "6";
    //private RelativeLayout user_data;
    private Button day_price,select_user,history,car_information,add_user,del_user,upd_user,log_out,car_book;
    public static SharedPreferences mdc;
    public static SharedPreferences.Editor meditor;
    public String USER_EMAIL = "user_email";
    public String USER_PASSWORD = "user_password";
    public String USER_ISUSED = "user_isused";
    private String TAG = "IntendantActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intendant);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        setlisetn();
        mdc = getSharedPreferences("data",MODE_PRIVATE);
        meditor = mdc.edit();
        meditor.putString("dc","6");
        meditor.commit();
    }

    private void setlisetn() {
        Onclick onclick = new Onclick();
        select_user.setOnClickListener(onclick);
        day_price.setOnClickListener(onclick);
        history.setOnClickListener(onclick);
        car_information.setOnClickListener(onclick);
        add_user.setOnClickListener(onclick);
        del_user.setOnClickListener(onclick);
        upd_user.setOnClickListener(onclick);
        log_out.setOnClickListener(onclick);
        car_book.setOnClickListener(onclick);
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()){
                case R.id.add_user:
                    intent = new Intent(IntendantActivity.this, SignUpActivity.class);
                    startActivity(intent);
                    break;
                case R.id.del_user:
                    intent = new Intent(IntendantActivity.this, DeleteUserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.upd_user:
                    intent = new Intent(IntendantActivity.this, UpdateUserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.select_user:
                    intent = new Intent(IntendantActivity.this, SelectUserActivity.class);
                    startActivity(intent);
                    break;
                case R.id.day_price:
                    setDay_price();
                    break;
                case R.id.history:
                    intent = new Intent(IntendantActivity.this, HistoryActivity.class);
                    startActivity(intent);
                    break;
                case R.id.car_information:
                    intent = new Intent(IntendantActivity.this, ParkingInformationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.car_book:
                    intent = new Intent(IntendantActivity.this, SelectBookCarActivity.class);
                    startActivity(intent);
                    break;
                case R.id.intendant_log_out:
                    logout();
                    break;
            }
        }
    }
    private void setDay_price(){
        AlertDialog.Builder builder = new AlertDialog.Builder(IntendantActivity.this);
        View view = LayoutInflater.from(IntendantActivity.this).inflate(R.layout.daycost_dialog,null);
        EditText et = view.findViewById(R.id.input);
        builder.setTitle("今日票价设置").setView(view);
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                meditor.putString("dc",et.getText().toString());
                meditor.commit();
                dc = et.getText().toString();
                Toast.makeText(IntendantActivity.this,"设置成功", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setView(view).create().show();
    }
    private void findid() {
        select_user = findViewById(R.id.select_user);
        day_price = findViewById(R.id.day_price);
        day_price = findViewById(R.id.day_price);
        history = findViewById(R.id.history);
        car_information = findViewById(R.id.car_information);
        add_user = findViewById(R.id.add_user);
        del_user = findViewById(R.id.del_user);
        upd_user = findViewById(R.id.upd_user);
        log_out = findViewById(R.id.intendant_log_out);
        car_book = findViewById(R.id.car_book);
    }
    private void logout(){
        SharedPreferences controllerdata = null;
        if(controllerdata==null){
            controllerdata = getSharedPreferences("controller", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor controller_editor = controllerdata.edit();
        controller_editor.putString(USER_EMAIL,"");
        controller_editor.putString(USER_PASSWORD,"");
        controller_editor.putBoolean(USER_ISUSED,false);
        controller_editor.commit();
        Log.d(TAG,"管理者状态为：" + controllerdata.getBoolean("user_isused", false));
        Log.d(TAG,"管理者账号为：" + controllerdata.getString("user_email", ""));
        Log.d(TAG,"管理者密码为：" + controllerdata.getString("user_password", ""));
        Intent intent = new Intent();
        intent = new Intent(IntendantActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }
}