package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.user;
import com.org.parking.Excention.error;
import com.org.parking.Excention.null_check;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateUserActivity extends AppCompatActivity {
    private CheckBox name_user,telephone_number_user;
    private EditText user_name,user_account,user_telephone_number;
    private Button update_summit,sign_up_back;
    private String account,name,telephone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        user_name.setEnabled(false);
        user_telephone_number.setEnabled(false);
        name_user.setChecked(false);
        telephone_number_user.setChecked(false);
        setlisten();
    }

    private void setlisten() {
        Onclick onclick = new Onclick();
        checkBoxChangeListener changeListener = new checkBoxChangeListener();

        name_user.setOnCheckedChangeListener(changeListener);
        telephone_number_user.setOnCheckedChangeListener(changeListener);
        sign_up_back.setOnClickListener(onclick);
        update_summit.setOnClickListener(onclick);
    }

    public class checkBoxChangeListener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.name_user:
                    if(name_user.isChecked()){
                        user_name.setEnabled(true);
                    }else{
                        user_name.setEnabled(false);
                    }
                    break;
                case R.id.telephone_number_user:
                    if(telephone_number_user.isChecked()){
                        user_telephone_number.setEnabled(true);
                    }else{
                        user_telephone_number.setEnabled(false);
                    }
                    break;
            }
        }
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.update_summit:
                    update();
                    break;
                case R.id.sign_up_back:
                    finish();
                    break;
            }
        }
    }

    private void update(){
        try {
            if(name_user.isChecked()&&telephone_number_user.isChecked()==false){
                //选择更新名字
                user_telephone_number.setText("");
                account = String.valueOf(user_account.getText());
                name = String.valueOf(user_name.getText());
                if(account.equals("")||name.equals("")){
                    throw new error("完善信息");
                }else {
                    final String[] obid = {null};
                    BmobQuery<user> findid = new BmobQuery<>();
                    findid.addWhereEqualTo("account",account);
                    findid.findObjects(new FindListener<user>() {
                        @Override
                        public void done(List<user> list, BmobException e) {
                            for(user oid : list){
                                obid[0] = oid.getObjectId();
                            }
                            user u1 = new user();
                            u1.setName(name);
                            u1.update(obid[0], new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UpdateUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateUserActivity.this, "修改失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });

                }
            }else if(name_user.isChecked()==false&&telephone_number_user.isChecked()){
                //选择更新电话号码
                user_name.setText("");
                account = String.valueOf(user_account.getText());
                telephone_number = String.valueOf(user_telephone_number.getText());
                if(account.equals("")||telephone_number.equals("")){
                    throw new error("完善信息");
                }else {
                    final String[] obid = {null};
                    BmobQuery<user> findid = new BmobQuery<>();
                    findid.addWhereEqualTo("account",account);
                    findid.findObjects(new FindListener<user>() {
                        @Override
                        public void done(List<user> list, BmobException e) {
                            for(user oid : list){
                                obid[0] = oid.getObjectId();
                            }
                            user u1 = new user();
                            u1.setTelephone_number(telephone_number);
                            u1.update(obid[0], new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UpdateUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateUserActivity.this, "修改失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }else if(name_user.isChecked()&&telephone_number_user.isChecked()){
                //两个一起更新
                name = String.valueOf(user_name.getText());
                account = String.valueOf(user_account.getText());
                telephone_number = String.valueOf(user_telephone_number.getText());
                if(account.equals("")||telephone_number.equals("")||name.equals("")){
                    throw new error("完善信息");
                }else {
                    final String[] obid = {null};
                    BmobQuery<user> findid = new BmobQuery<>();
                    findid.addWhereEqualTo("account",account);
                    findid.findObjects(new FindListener<user>() {
                        @Override
                        public void done(List<user> list, BmobException e) {
                            for(user oid : list){
                                obid[0] = oid.getObjectId();
                            }
                            user u1 = new user();
                            u1.setTelephone_number(telephone_number);
                            u1.setName(name);
                            u1.update(obid[0], new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e == null) {
                                        Toast.makeText(UpdateUserActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(UpdateUserActivity.this, "修改失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                }
            }else{
                throw new null_check();
            }
        } catch (com.org.parking.Excention.null_check null_check) {
            Toast.makeText(UpdateUserActivity.this,"请选择需要修改的用户信息", Toast.LENGTH_SHORT).show();
        } catch (com.org.parking.Excention.error error) {
            Toast.makeText(UpdateUserActivity.this,"请完善信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void findid() {
        sign_up_back = findViewById(R.id.sign_up_back);
        name_user = findViewById(R.id.name_user);
        telephone_number_user = findViewById(R.id.telephone_number_user);

        user_name = findViewById(R.id.user_name);
        user_account = findViewById(R.id.user_account);
        user_telephone_number = findViewById(R.id.user_telephone_number);

        update_summit = findViewById(R.id.update_summit);

    }
}