package com.org.parking.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.org.parking.R;
import com.org.parking.BmobTable.history;
import com.org.parking.RecycleViewAdapter.CarHistroyLinAdapter;
import com.org.parking.RecycleViewAdapter.SpacesItemDecoration;
import com.org.parking.List.CarHistoryList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class HistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<CarHistoryList> hs_Cardata = new ArrayList<>();
    private ProgressBar pb;
    private EditText select;
    private Button select_one,select_all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        findid();
        setlisten();

        select.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    select.setHint("");
                }else{
                    select.setHint("请输入车牌");
                }
            }
        });
        select.setTransformationMethod(replacementTransformationMethod);

        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
        recyclerView.setAdapter(new CarHistroyLinAdapter(HistoryActivity.this,hs_Cardata));

    }

    ReplacementTransformationMethod replacementTransformationMethod = new ReplacementTransformationMethod() {
        @Override
        protected char[] getOriginal() {
            char[] aa = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
            return aa;
        }
        @Override
        protected char[] getReplacement() {
            char[] cc = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
            return cc;
        }
    };

    private void setlisten() {
        Onclick onclick = new Onclick();
        select_one.setOnClickListener(onclick);
        select_all.setOnClickListener(onclick);
    }

    private void findid() {
        pb = findViewById(R.id.pb);
        recyclerView = findViewById(R.id.rv_main);

        select_one = findViewById(R.id.select_one);
        select_all = findViewById(R.id.select_all);

        select = findViewById(R.id.select);
    }

    class Onclick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()){
                case R.id.select_one:
                    void_select_one();
                    break;
                case R.id.select_all:
                    void_select_all();
                    break;
            }
        }
    }

    private void void_select_all() {
        hs_Cardata.clear();
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        String sql = "select * from history order by Card_id";
        new BmobQuery<history>().doSQLQuery(sql,new SQLQueryListener<history>(){
            @Override
            public void done(BmobQueryResult<history> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<history> list = (List<history>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        for(history chdata : list){
                            int card_id = chdata.getCard_id();
                            String car_number = chdata.getCar_number();

                            BmobDate car_in_time = chdata.getIn_time();
                            BmobDate car_out_time = chdata.getOut_time();
                            double cost = chdata.getCost();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long d_car_in_time = car_in_time.getTimeStamp(car_in_time.getDate());
                            long d_car_out_time = car_out_time.getTimeStamp(car_out_time.getDate());
                            Date date1 = new Date(d_car_in_time);
                            Date date2 = new Date(d_car_out_time);
                            String good_car_in_time = dateFormat.format(date1);
                            String good_car_out_time = dateFormat.format(date2);

                            CarHistoryList ch = new CarHistoryList();
                            ch.setCard_id(card_id);
                            ch.setCar_number(car_number);
                            ch.setIn_time(good_car_in_time);
                            ch.setOut_time(good_car_out_time);
                            ch.setCost(cost);
                            hs_Cardata.add(ch);
                        }
                    }else{
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    recyclerView.setAdapter(new CarHistroyLinAdapter(HistoryActivity.this,hs_Cardata));
                }else{
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String void_select_text() {
        String car_number = String.valueOf(select.getText()).toUpperCase();
        return car_number;
    }

    private void void_select_one() {
        String input_car_number = void_select_text();
        hs_Cardata.clear();
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        String sql = "select * from history where Car_number = ? order by in_time";
        new BmobQuery<history>().doSQLQuery(sql,new SQLQueryListener<history>(){
            @Override
            public void done(BmobQueryResult<history> bmobQueryResult, BmobException e) {
                if(e==null){
                    List<history> list = (List<history>) bmobQueryResult.getResults();
                    if(list!=null && list.size()>0){
                        for(history chdata : list){
                            int card_id = chdata.getCard_id();
                            String car_number = chdata.getCar_number();

                            BmobDate car_in_time = chdata.getIn_time();
                            BmobDate car_out_time = chdata.getOut_time();
                            double cost = chdata.getCost();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long d_car_in_time = car_in_time.getTimeStamp(car_in_time.getDate());
                            long d_car_out_time = car_out_time.getTimeStamp(car_out_time.getDate());
                            Date date1 = new Date(d_car_in_time);
                            Date date2 = new Date(d_car_out_time);
                            String good_car_in_time = dateFormat.format(date1);
                            String good_car_out_time = dateFormat.format(date2);

                            CarHistoryList ch = new CarHistoryList();
                            ch.setCard_id(card_id);
                            ch.setCar_number(car_number);
                            ch.setIn_time(good_car_in_time);
                            ch.setOut_time(good_car_out_time);
                            ch.setCost(cost);
                            hs_Cardata.add(ch);
                        }
                    }else{
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HistoryActivity.this));
                    recyclerView.setAdapter(new CarHistroyLinAdapter(HistoryActivity.this,hs_Cardata));
                }else{
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },input_car_number);
    }
}