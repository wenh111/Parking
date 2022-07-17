package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.user;
import com.org.parking.Excention.error;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class DeleteUserActivity extends AppCompatActivity {

    private EditText user_name, user_account, user_telephone_number;
    private Button delete_summit, sign_up_back;
    private String name, account, telephone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        findid();
        setlisten();
    }

    private void setlisten() {
        Onclick onclick = new Onclick();
        delete_summit.setOnClickListener(onclick);
        sign_up_back.setOnClickListener(onclick);
    }


    class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.delete_summit:
                    delete();
                    break;
                case R.id.sign_up_back:
                    finish();
                    break;
            }
        }
    }

    private void findid() {
        delete_summit = findViewById(R.id.delete_summit);
        user_name = findViewById(R.id.user_name);
        user_account = findViewById(R.id.user_account);
        user_telephone_number = findViewById(R.id.user_telephone_number);
        sign_up_back = findViewById(R.id.sign_up_back);
    }

    private void delete() {
        name = String.valueOf(user_name.getText());
        account = String.valueOf(user_account.getText());
        telephone_number = String.valueOf(user_telephone_number.getText());
        BmobQuery<user> eq1 = new BmobQuery<user>();
        BmobQuery<user> eq2 = new BmobQuery<user>();
        BmobQuery<user> eq3 = new BmobQuery<user>();
        eq1.addWhereEqualTo("name", name);
        eq2.addWhereEqualTo("account", account);
        eq3.addWhereEqualTo("Telephone_number", telephone_number);
        List<BmobQuery<user>> andQuerys = new ArrayList<BmobQuery<user>>();
        andQuerys.add(eq1);
        andQuerys.add(eq2);
        andQuerys.add(eq3);
        BmobQuery<user> query = new BmobQuery<user>();
        query.and(andQuerys);
        query.findObjects(new FindListener<user>() {
            @Override
            public void done(List<user> list, BmobException e) {
                String obid = null;
                for (user oid : list) {
                    obid = oid.getObjectId();
                }
                if (e == null && list.size() == 1 && obid != null) {
                    user u1 = new user();
                    u1.delete(obid, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Toast.makeText(DeleteUserActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(DeleteUserActivity.this, "删除失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    try {
                        throw new error("信息不正确");
                    } catch (com.org.parking.Excention.error error) {
                        Toast.makeText(DeleteUserActivity.this, "信息不正确", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}