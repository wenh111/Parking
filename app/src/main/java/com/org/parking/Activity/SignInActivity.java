package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.util.Log;
import android.view.View;

import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.user;
import com.org.parking.Excention.Nullchange_cb;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class SignInActivity extends AppCompatActivity {

    private ImageView iv;
    private boolean isHideFirst = true;
    public Button sign_up, summit;
    public EditText editText_email, editText_password;
    public TextView fp_tv;
    public CheckBox sign_in_user, sign_in_intendant;
    public ProgressBar pb;
    public String USER_EMAIL = "user_email";
    public String USER_PASSWORD = "user_password";
    public String USER_ISUSED = "user_isused";
    String email, password, identity;
    private String TAG = "Sign_in_Activity";

    /*public SharedPreferences userdata,controllerdata;*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        iv.setImageResource(R.drawable.close_eye);
        sign_in_user.setChecked(false);
        sign_in_intendant.setChecked(false);
        setlisten();

    }

    //获取ID
    private void findid() {
        pb = findViewById(R.id.pb);
        sign_up = findViewById(R.id.sign_up);
        summit = findViewById(R.id.sign_in);
        editText_email = (EditText) findViewById(R.id.sign_in_account);
        editText_password = (EditText) findViewById(R.id.sign_in_passward);
        fp_tv = (TextView) findViewById(R.id.fp_tv);
        iv = (ImageView) findViewById(R.id.sign_in_eye_photo);
        sign_in_user = (CheckBox) findViewById(R.id.sign_in_user);
        sign_in_intendant = (CheckBox) findViewById(R.id.sign_in_intendant);
    }

    //添加监听事件
    private void setlisten() {
        Onclick onclick = new Onclick();
        checkBoxChangeListener changeListener = new checkBoxChangeListener();

        //复选框监听
        sign_in_user.setOnCheckedChangeListener(changeListener);
        sign_in_intendant.setOnCheckedChangeListener(changeListener);

        //按钮监听
        sign_up.setOnClickListener(onclick);
        iv.setOnClickListener(onclick);
        summit.setOnClickListener(onclick);
        fp_tv.setOnClickListener(onclick);


    }

    //复选框响应代码
    public class checkBoxChangeListener implements CompoundButton.OnCheckedChangeListener {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            switch (buttonView.getId()) {
                case R.id.sign_in_user:
                    if (sign_in_user.isChecked()) {
                        sign_in_user.setChecked(true);
                        sign_in_intendant.setChecked(false);
                    } else {
                        sign_in_user.setChecked(false);
                    }
                    break;

                case R.id.sign_in_intendant:
                    if (sign_in_intendant.isChecked()) {
                        sign_in_user.setChecked(false);
                        sign_in_intendant.setChecked(true);
                    } else {
                        sign_in_intendant.setChecked(false);
                    }
                    break;
            }
        }
    }

    //点击事件响应代码
    class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            final Intent[] intent = {null};
            switch (v.getId()) {
                case R.id.sign_in_eye_photo:
                    if (isHideFirst == true) {
                        iv.setImageResource(R.drawable.open_eye);
                        HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                        editText_password.setTransformationMethod(method1);
                        isHideFirst = false;
                    } else {
                        iv.setImageResource(R.drawable.close_eye);
                        TransformationMethod method = PasswordTransformationMethod.getInstance();
                        editText_password.setTransformationMethod(method);
                        isHideFirst = true;
                    }
                    int index = editText_password.getText().toString().length();
                    editText_password.setSelection(index);
                    break;


                case R.id.sign_in:
                    try {
                        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
                        if (sign_in_user.isChecked()) {
                            email = String.valueOf(editText_email.getText());
                            password = String.valueOf(editText_password.getText());
                            BmobQuery<user> eq1 = new BmobQuery<user>();
                            BmobQuery<user> eq2 = new BmobQuery<user>();
                            eq1.addWhereEqualTo("account", email);
                            eq2.addWhereEqualTo("password", password);
                            List<BmobQuery<user>> andQuerys = new ArrayList<BmobQuery<user>>();
                            andQuerys.add(eq1);
                            andQuerys.add(eq2);
                            BmobQuery<user> query = new BmobQuery<user>();
                            query.and(andQuerys);
                            query.findObjects(new FindListener<user>() {
                                @Override
                                public void done(List<user> list, BmobException e) {
                                    if (e == null && list.size() != 0) {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        SharedPreferences userdata = null;
                                        if (userdata == null) {
                                            userdata = getSharedPreferences("user", Context.MODE_PRIVATE);
                                        }
                                        SharedPreferences.Editor user_editor = userdata.edit();
                                        user_editor.putString(USER_EMAIL, editText_email.getText().toString());
                                        user_editor.putString(USER_PASSWORD, editText_password.getText().toString());
                                        user_editor.putBoolean(USER_ISUSED, true);
                                        user_editor.commit();
                                        Log.d(TAG, "用户状态为：" + userdata.getBoolean("user_isused", false));
                                        Log.d(TAG, "用户账号为：" + userdata.getString("user_email", ""));
                                        Log.d(TAG, "用户密码为：" + userdata.getString("user_password", ""));
                                        Toast.makeText(v.getContext(), "登录成功 ", Toast.LENGTH_SHORT).show();
                                        intent[0] = new Intent(SignInActivity.this, UserActivity.class);
                                        startActivity(intent[0]);
                                        finish();
                                    } else if (e == null && list.size() == 0) {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(v.getContext(), "请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                                    } else {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(v.getContext(), "请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        } else if (sign_in_intendant.isChecked()) {
                            email = String.valueOf(editText_email.getText());
                            password = String.valueOf(editText_password.getText());
                            BmobQuery<user> eq1 = new BmobQuery<user>();
                            BmobQuery<user> eq2 = new BmobQuery<user>();
                            eq1.addWhereEqualTo("account", email);
                            eq2.addWhereEqualTo("password", password);
                            List<BmobQuery<user>> andQuerys = new ArrayList<BmobQuery<user>>();
                            andQuerys.add(eq1);
                            andQuerys.add(eq2);
                            BmobQuery<user> query = new BmobQuery<user>();
                            query.and(andQuerys);
                            query.findObjects(new FindListener<user>() {
                                @Override
                                public void done(List<user> list, BmobException e) {
                                    String IT = null;
                                    for (user identity : list) {
                                        IT = identity.getIdentity();
                                    }
                                    if (e == null && list.size() != 0 && IT.equals("管理者")) {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        SharedPreferences controllerdata = null;
                                        if (controllerdata == null) {
                                            controllerdata = getSharedPreferences("controller", Context.MODE_PRIVATE);
                                        }
                                        SharedPreferences.Editor controller_editor = controllerdata.edit();
                                        controller_editor.putString(USER_EMAIL, editText_email.getText().toString());
                                        controller_editor.putString(USER_PASSWORD, editText_password.getText().toString());
                                        controller_editor.putBoolean(USER_ISUSED, true);
                                        controller_editor.commit();
                                        Log.d(TAG, "状态为：" + controllerdata.getBoolean("user_isused", false));
                                        Log.d(TAG, "账号为：" + controllerdata.getString("user_email", ""));
                                        Log.d(TAG, "密码为：" + controllerdata.getString("user_password", ""));
                                        Toast.makeText(v.getContext(), "登录成功 ", Toast.LENGTH_SHORT).show();
                                        intent[0] = new Intent(SignInActivity.this, IntendantActivity.class);
                                        startActivity(intent[0]);
                                        finish();
                                    } else if (e == null && list.size() == 0) {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(v.getContext(), "请输入正确的账号密码", Toast.LENGTH_SHORT).show();
                                    } else {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(v.getContext(), "登录失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            throw new Nullchange_cb();
                        }
                    } catch (Nullchange_cb e) {
                        Toast.makeText(v.getContext(), "请选择身份", Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.sign_up:
                    intent[0] = new Intent(SignInActivity.this, SignUpActivity.class);
                    startActivity(intent[0]);
                    break;


                case R.id.fp_tv:
                    intent[0] = new Intent(SignInActivity.this, ForgetPasswordActivity.class);
                    startActivity(intent[0]);
                    break;
            }
        }
    }

}