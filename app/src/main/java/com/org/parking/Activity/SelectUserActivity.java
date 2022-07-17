package com.org.parking.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.List.UserList;
import com.org.parking.R;
import com.org.parking.BmobTable.user;
import com.org.parking.Excention.error;
import com.org.parking.Excention.error_car_number;
import com.org.parking.RecycleViewAdapter.SelectUserLinAdapter;
import com.org.parking.RecycleViewAdapter.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class SelectUserActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<UserList> userdata = new ArrayList<>();
    private ProgressBar pb;
    private EditText select;
    private Button select_one,select_all;
    private CheckBox cb_name,cb_email,cb_telephone_number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_user);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        finid();
        setlisten();

        cb_email.setChecked(false);
        cb_name.setChecked(false);
        cb_telephone_number.setChecked(false);

        select.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    select.setHint("");
                }else{
                    select.setHint("请输入信息");
                }
            }
        });

        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectUserActivity.this));
        recyclerView.setAdapter(new SelectUserLinAdapter(SelectUserActivity.this,userdata));


    }

    private String void_select_text() {

        String information = String.valueOf(select.getText());
        return information;

    }

    private void setlisten() {

        checkBoxChangeListener changeListener = new checkBoxChangeListener();
        cb_name.setOnCheckedChangeListener(changeListener);
        cb_email.setOnCheckedChangeListener(changeListener);
        cb_telephone_number.setOnCheckedChangeListener(changeListener);

        Onclick onclick = new Onclick();
        select_one.setOnClickListener(onclick);
        select_all.setOnClickListener(onclick);

    }

    private void finid() {

        pb = findViewById(R.id.pb);
        recyclerView = findViewById(R.id.rv_main);

        select_one = findViewById(R.id.select_one);
        select_all = findViewById(R.id.select_all);

        select = findViewById(R.id.select);

        cb_name = findViewById(R.id.cb_name);
        cb_email = findViewById(R.id.cb_email);
        cb_telephone_number = findViewById(R.id.cb_telephone_number);
    }

    public class checkBoxChangeListener implements CompoundButton.OnCheckedChangeListener{
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            switch (buttonView.getId()){
                case R.id.cb_name:
                    if(cb_name.isChecked()){
                        cb_name.setChecked(true);
                        cb_email.setChecked(false);
                        cb_telephone_number.setChecked(false);
                    }else{
                        cb_name.setChecked(false);
                    }
                    break;
                case R.id.cb_email:
                    if(cb_email.isChecked()){
                        cb_email.setChecked(true);
                        cb_name.setChecked(false);
                        cb_telephone_number.setChecked(false);
                    }else{
                        cb_email.setChecked(false);
                    }
                    break;
                case R.id.cb_telephone_number:
                    if(cb_telephone_number.isChecked()){
                        cb_telephone_number.setChecked(true);
                        cb_name.setChecked(false);
                        cb_email.setChecked(false);
                    }else{
                        cb_telephone_number.setChecked(false);
                    }
                    break;
            }
        }
    }

    class Onclick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.select_all:
                    void_select_all();
                    break;
                case R.id.select_one:
                    void_select_one();
                    break;
            }
        }
    }

    private void void_select_one() {
        try {
            String information = void_select_text();
            userdata.clear();
            pb.setVisibility(findViewById(R.id.pb).VISIBLE);
            String sql = null;
            String sql_name = "select * from user where name = ?";
            String sql_email = "select * from user where account = ?";
            String sql_telephone_number = "select * from user where Telephone_number = ?";

            if(cb_name.isChecked()){
                sql = sql_name;
            }else if(cb_email.isChecked()){
                sql = sql_email;
            }else if(cb_telephone_number.isChecked()){
                sql = sql_telephone_number;
            }else{
                throw new error("未选择身份");
            }
            if(information.equals("")){
                throw new error_car_number();//搜索框没有信息
            }else{
                new BmobQuery<user>().doSQLQuery(sql,new SQLQueryListener<user>(){
                    @Override
                    public void done(BmobQueryResult<user> bmobQueryResult, BmobException e) {
                        if(e ==null){
                            List<user> list = (List<user>) bmobQueryResult.getResults();
                            if(list!=null && list.size()>0){
                                for(user udata : list){
                                    int id = udata.getId();
                                    String name = udata.getName();
                                    String account = udata.getAccount();
                                    String Telephone_number = udata.getTelephone_number();
                                    String identity = udata.getIdentity();
                                    UserList person = new UserList();
                                    person.setId(id);
                                    person.setName(name);
                                    person.setAccount(account);
                                    person.setTelephone_number(Telephone_number);
                                    person.setIdentity(identity);
                                    userdata.add(person);
                                }
                            }else{
                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                            }
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SelectUserActivity.this));
                            recyclerView.setAdapter(new SelectUserLinAdapter(SelectUserActivity.this,userdata));
                        }else{
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },information);
            }
        } catch (com.org.parking.Excention.error error) {
            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
            Toast.makeText(recyclerView.getContext(),"请选择查找的信息", Toast.LENGTH_SHORT).show();
        } catch (com.org.parking.Excention.error_car_number error_car_number) {
            Toast.makeText(recyclerView.getContext(),"请输入需要查找的信息", Toast.LENGTH_SHORT).show();
        }
    }

    private void void_select_all() {
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        userdata.clear();
        String sql = "select * from user";
        new BmobQuery<user>().doSQLQuery(sql,new SQLQueryListener<user>(){
                    @Override
                    public void done(BmobQueryResult<user> bmobQueryResult, BmobException e) {
                        if(e ==null){
                            List<user> list = (List<user>) bmobQueryResult.getResults();
                            if(list!=null && list.size()>0){
                                for(user udata : list){
                                    int id = udata.getId();
                                    String name = udata.getName();
                                    String account = udata.getAccount();
                                    String Telephone_number = udata.getTelephone_number();
                                    String identity = udata.getIdentity();
                                    UserList person = new UserList();
                                    person.setId(id);
                                    person.setName(name);
                                    person.setAccount(account);
                                    person.setTelephone_number(Telephone_number);
                                    person.setIdentity(identity);
                                    userdata.add(person);
                                }
                            }else{
                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                            }
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            recyclerView.setLayoutManager(new LinearLayoutManager(SelectUserActivity.this));
                            recyclerView.setAdapter(new SelectUserLinAdapter(SelectUserActivity.this,userdata));
                        }else{
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}