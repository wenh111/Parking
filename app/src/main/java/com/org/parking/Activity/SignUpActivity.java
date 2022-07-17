package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.method.TransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.user;
import com.org.parking.Excention.Nullchange_cb;
import com.org.parking.Excention.Nullexcetion;
import com.org.parking.Excention.error_miyao;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class SignUpActivity extends AppCompatActivity {
    private EditText sign_up_account,sign_up_password,sign_up_telephone_number,sign_up_name,sign_up_ind_password;
    private ImageView sign_up_eye_photo;
    private Button summit,sign_up_back;
    private CheckBox sign_up_user,sign_up_intendant;
    private boolean isHideFirst = true;
    String user_password,user_auccount,user_telephone_number,user_name,input_miyao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        sign_up_ind_password.setEnabled(false);
        sign_up_user.setChecked(false);
        sign_up_intendant.setChecked(false);
        sign_up_eye_photo.setImageResource(R.drawable.close_eye);
        setlisten();

    }

    //获取控件
    private void findid() {
        //按钮
        summit = findViewById(R.id.sign_up_summit);
        sign_up_back = findViewById(R.id.sign_up_back);

        //复选框
        sign_up_user = findViewById(R.id.sign_up_user);
        sign_up_intendant = findViewById(R.id.sign_up_intendant);

        //小眼睛
        sign_up_eye_photo = findViewById(R.id.sign_up_eye_photo);

        //输入框
        sign_up_ind_password = findViewById(R.id.sign_up_ind_password);
        sign_up_name = findViewById(R.id.sign_up_name);
        sign_up_account = findViewById(R.id.sign_up_account);
        sign_up_password = findViewById(R.id.sign_up_password);
        sign_up_telephone_number = findViewById(R.id.sign_up_telephone_number);
    }

    //设置监听事件
    private void setlisten() {
        Onclick onclick = new Onclick();
        checkBoxChangeListener changeListener = new checkBoxChangeListener();

        //复选框监听
        sign_up_user.setOnCheckedChangeListener(changeListener);
        sign_up_intendant.setOnCheckedChangeListener(changeListener);

        //小眼睛
        sign_up_eye_photo.setOnClickListener(onclick);

        //按钮
        summit.setOnClickListener(onclick);
        sign_up_back.setOnClickListener(onclick);
    }

    //复选框设置为单选并且同时设置秘钥输入框是否能输入
    public class checkBoxChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.sign_up_user:
                    if(sign_up_user.isChecked()){
                        sign_up_user.setChecked(true);
                        sign_up_intendant.setChecked(false);
                        sign_up_ind_password.setEnabled(false);
                    }else{
                        sign_up_ind_password.setEnabled(false);
                        sign_up_user.setChecked(false);
                    }
                    break;
                case R.id.sign_up_intendant:
                    if(sign_up_intendant.isChecked()){
                        sign_up_user.setChecked(false);
                        sign_up_intendant.setChecked(true);
                        sign_up_ind_password.setEnabled(true);
                    }else{
                        sign_up_ind_password.setEnabled(false);
                        sign_up_intendant.setChecked(false);
                    }
                    break;

            }
        }
    }

    //各个点击事件的实现
    public class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()){
                case R.id.sign_up_eye_photo:
                    if(isHideFirst == true){
                        sign_up_eye_photo.setImageResource(R.drawable.open_eye);
                        HideReturnsTransformationMethod method1 = HideReturnsTransformationMethod.getInstance();
                        sign_up_password.setTransformationMethod(method1);
                        isHideFirst=false;
                    }else{
                        sign_up_eye_photo.setImageResource(R.drawable.close_eye);
                        TransformationMethod method = PasswordTransformationMethod.getInstance();
                        sign_up_password.setTransformationMethod(method);
                        isHideFirst=true;
                    }
                    int index = sign_up_password.getText().toString().length();
                    sign_up_password.setSelection(index);
                    break;
                case R.id.sign_up_summit:
                    try {
                        if(sign_up_user.isChecked()){
                            long id = 0;
                            user_name = String.valueOf(sign_up_name.getText());
                            user_auccount = String.valueOf(sign_up_account.getText());
                            user_password = String.valueOf(sign_up_password.getText());
                            user_telephone_number = String.valueOf(sign_up_telephone_number.getText());
                            if(user_name.equals("")||user_auccount.equals("")||user_password.equals("")||user_telephone_number.equals("")){
                                if(user_name.equals("")){
                                    sign_up_name.requestFocus();
                                    throw new Nullexcetion();
                                }
                                if(user_auccount.equals("")){
                                    sign_up_account.requestFocus();
                                    throw new Nullexcetion();
                                }
                                if(user_password.equals("")){
                                    sign_up_password.requestFocus();
                                    throw new Nullexcetion();
                                }
                                if(user_telephone_number.equals("")){
                                    sign_up_telephone_number.requestFocus();
                                    throw new Nullexcetion();
                                }
                            }else {
                                user u1 = new user();
                                u1.setName(user_name);
                                u1.setAccount(user_auccount);
                                u1.setPassword(user_password);
                                u1.setTelephone_number(user_telephone_number);
                                u1.setIdentity("员工");
                                u1.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Toast.makeText(v.getContext(), "注册成功 ", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(v.getContext(), "注册失败 " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                            }else if(sign_up_intendant.isChecked()){
                                //long id = 0;
                                int miyao = 123456;
                                input_miyao = String.valueOf(sign_up_ind_password.getText());
                                user_name = String.valueOf(sign_up_name.getText());
                                user_auccount = String.valueOf(sign_up_account.getText());
                                user_password = String.valueOf(sign_up_password.getText());
                                user_telephone_number = String.valueOf(sign_up_telephone_number.getText());
                                if(user_name.equals("")||user_auccount.equals("")||user_password.equals("")||user_telephone_number.equals("")||input_miyao.equals("")){
                                    if(user_name.equals("")){
                                        sign_up_name.requestFocus();
                                        throw new Nullexcetion();
                                    }
                                    if(user_auccount.equals("")){
                                        sign_up_account.requestFocus();
                                        throw new Nullexcetion();
                                    }
                                    if(user_password.equals("")){
                                        sign_up_password.requestFocus();
                                        throw new Nullexcetion();
                                    }
                                    if(user_telephone_number.equals("")){
                                        sign_up_telephone_number.requestFocus();
                                        throw new Nullexcetion();
                                    }
                                    if(input_miyao.equals("")){
                                        sign_up_ind_password.requestFocus();
                                        throw new Nullexcetion();
                                    }
                                }else if(Integer.parseInt(input_miyao)!=miyao){
                                    sign_up_ind_password.setText("");
                                    throw new error_miyao();
                                }else{
                                    user u1 = new user();
                                    u1.setName(user_name);
                                    u1.setAccount(user_auccount);
                                    u1.setPassword(user_password);
                                    u1.setTelephone_number(user_telephone_number);
                                    u1.setIdentity("管理者");
                                    u1.save(new SaveListener<String>() {
                                        @Override
                                        public void done(String s, BmobException e) {
                                            if(e==null){
                                                Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
                                                startActivity(intent);
                                                Toast.makeText(v.getContext(),"注册成功 ", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }else{
                                                Toast.makeText(v.getContext(),"注册失败:信息重复", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }else{
                                throw new Nullchange_cb();
                            }
                    }catch (Nullexcetion e){
                        Toast.makeText(v.getContext(),"请完善资料", Toast.LENGTH_SHORT).show();
                    } catch (Nullchange_cb nullchange_cb) {
                        Toast.makeText(v.getContext(),"请选择身份", Toast.LENGTH_SHORT).show();
                    } catch (com.org.parking.Excention.error_miyao error_miyao) {
                        Toast.makeText(v.getContext(),"请输入正确的秘钥", Toast.LENGTH_SHORT).show();
                    }

                    break;
                case R.id.sign_up_back:
                    finish();
                    break;
            }

        }
    }
}