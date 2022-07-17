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
import com.org.parking.BmobTable.book;
import com.org.parking.RecycleViewAdapter.BookCarLinAdapter;
import com.org.parking.RecycleViewAdapter.SpacesItemDecoration;
import com.org.parking.List.BookCarList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class SelectBookCarActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<BookCarList> book_cars_data = new ArrayList<>();
    private ProgressBar pb;
    private EditText select;
    private Button select_one,select_all;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_book_car);

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
        recyclerView.setLayoutManager(new LinearLayoutManager(SelectBookCarActivity.this));
        recyclerView.setAdapter(new BookCarLinAdapter(SelectBookCarActivity.this,book_cars_data));

    }

    //获取用户输入的字母并且转换为大写字母显示
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

    private String void_select_text() {
        String car_number = String.valueOf(select.getText()).toUpperCase();
        return car_number;
    }

    private void void_select_one() {
        String input_car_number = void_select_text();
        book_cars_data.clear();
        String sql = "select * from book where Car_number = ? order by location";
        new BmobQuery<book>().doSQLQuery(sql,new SQLQueryListener<book>(){
            @Override
            public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                List<book> list = (List<book>) bmobQueryResult.getResults();
                if(e==null){
                    if(list!=null && list.size()>0){
                        for(book bc : list){
                            String location = bc.getLocation();
                            String car_number = bc.getCar_number();
                            BmobDate book_time = bc.getBook_time();
                            BmobDate expiration_time = bc.getExpiration_time();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long m = book_time.getTimeStamp(book_time.getDate());
                            long n = expiration_time.getTimeStamp(expiration_time.getDate());
                            Date date = new Date(m);
                            Date date1 = new Date(n);
                            String good_book_time = dateFormat.format(date);
                            String good_expiration_time = dateFormat.format(date1);

                            BookCarList book_cars = new BookCarList();
                            book_cars.setLocation(location);
                            book_cars.setCar_number(car_number);
                            book_cars.setBook_time(good_book_time);
                            book_cars.setExpiration_time(good_expiration_time);
                            book_cars_data.add(book_cars);
                        }
                    }else{
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(SelectBookCarActivity.this));
                    recyclerView.setAdapter(new BookCarLinAdapter(SelectBookCarActivity.this,book_cars_data));
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                }else{
                    Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        },input_car_number);
    }


    private void void_select_all() {
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        book_cars_data.clear();
        String sql = "select * from book order by location";
        new BmobQuery<book>().doSQLQuery(sql,new SQLQueryListener<book>(){
            @Override
            public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                List<book> list = (List<book>) bmobQueryResult.getResults();
                if(e==null){
                    if(list!=null && list.size()>0){
                        for(book bc : list){
                            String location = bc.getLocation();
                            String car_number = bc.getCar_number();
                            BmobDate book_time = bc.getBook_time();
                            BmobDate expiration_time = bc.getExpiration_time();
                            SimpleDateFormat dateFormat= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long m = book_time.getTimeStamp(book_time.getDate());
                            long n = expiration_time.getTimeStamp(expiration_time.getDate());
                            Date date = new Date(m);
                            Date date1 = new Date(n);
                            String good_book_time = dateFormat.format(date);
                            String good_expiration_time = dateFormat.format(date1);

                            BookCarList book_cars = new BookCarList();
                            book_cars.setLocation(location);
                            book_cars.setCar_number(car_number);
                            book_cars.setBook_time(good_book_time);
                            book_cars.setExpiration_time(good_expiration_time);
                            book_cars_data.add(book_cars);
                        }
                    }else{
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(),"查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }
                    recyclerView.setLayoutManager(new LinearLayoutManager(SelectBookCarActivity.this));
                    recyclerView.setAdapter(new BookCarLinAdapter(SelectBookCarActivity.this,book_cars_data));
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                }else{
                    Toast.makeText(recyclerView.getContext(),"查询失败"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}