package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.user;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class ForgetPasswordActivity extends AppCompatActivity {

    public static String email, telephone_number;
    private EditText forget_Email, forget_telephone_number;
    private Button forget_summit, forget_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        setlisten();
    }


    private void findid() {
        //按钮
        forget_summit = findViewById(R.id.forget_summit);
        forget_back = findViewById(R.id.forget_back);
        //输入框
        forget_Email = findViewById(R.id.forget_Email);
        forget_telephone_number = findViewById(R.id.forget_telephone_number);


    }

    private void setlisten() {
        Onclick onclick = new Onclick();
        forget_summit.setOnClickListener(onclick);
        forget_back.setOnClickListener(onclick);
    }

    public class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            final Intent[] intent = {null};
            switch (v.getId()) {
                case R.id.forget_summit:
                    email = String.valueOf(forget_Email.getText());
                    telephone_number = String.valueOf(forget_telephone_number.getText());
                    BmobQuery<user> eq1 = new BmobQuery<user>();
                    BmobQuery<user> eq2 = new BmobQuery<user>();
                    eq1.addWhereEqualTo("account", email);
                    eq2.addWhereEqualTo("Telephone_number", telephone_number);
                    List<BmobQuery<user>> andQuerys = new ArrayList<BmobQuery<user>>();
                    andQuerys.add(eq1);
                    andQuerys.add(eq2);
                    BmobQuery<user> query = new BmobQuery<user>();
                    query.and(andQuerys);
                    query.findObjects(new FindListener<user>() {
                        @Override
                        public void done(List<user> list, BmobException e) {
                            if (e == null && list.size() != 0) {
                                intent[0] = new Intent(ForgetPasswordActivity.this, SettingPasswordActivity.class);
                                startActivity(intent[0]);
                                finish();
                            } else {
                                Toast.makeText(v.getContext(), "邮箱或电话号码不正确", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                    break;
                case R.id.forget_back:
                    finish();
                    break;
            }
        }
    }

}