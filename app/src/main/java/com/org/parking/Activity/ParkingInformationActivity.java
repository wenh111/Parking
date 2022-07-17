package com.org.parking.Activity;

import android.os.Build;
import android.os.Bundle;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.DiffCallBack.CarDiffCallBack;
import com.org.parking.R;
import com.org.parking.BmobTable.Card;
import com.org.parking.BmobTable.book;
import com.org.parking.List.CarList;
import com.org.parking.RecycleViewAdapter.CarLinAdapter;
import com.org.parking.RecycleViewAdapter.SpacesItemDecoration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;

public class ParkingInformationActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<CarList> CarData = new ArrayList<>();
    private ProgressBar pb;
    private EditText select;
    private Button select_one, select_all;
    private CarLinAdapter linAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parking_information);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }

        finid();
        setlisten();

        select.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    select.setHint("");
                } else {
                    select.setHint("请输入车牌");
                }
            }
        });
        select.setTransformationMethod(replacementTransformationMethod);

        linAdapter = new CarLinAdapter(ParkingInformationActivity.this, CarData);
        recyclerView.addItemDecoration(new SpacesItemDecoration(30));
        recyclerView.setLayoutManager(new LinearLayoutManager(ParkingInformationActivity.this));
        recyclerView.setAdapter(linAdapter);
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

    private String void_select_text() {

        String car_number = String.valueOf(select.getText()).toUpperCase();
        return car_number;
    }

    private void setlisten() {
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
    }


    class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
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
        String input_car_number = void_select_text();
        List<CarList> CarDates = new ArrayList<>();
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        String sql = "select * from Card where Car_number = ? order by Car_in_time";
        new BmobQuery<Card>().doSQLQuery(sql, new SQLQueryListener<Card>() {
            @Override
            public void done(BmobQueryResult<Card> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<Card> list = (List<Card>) bmobQueryResult.getResults();
                    if (list != null && list.size() > 0) {
                        for (Card cdata : list) {
                            int id = cdata.getId();
                            String car_number = cdata.getCar_number();
                            BmobDate car_in_time = cdata.getCar_in_time();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long m = car_in_time.getTimeStamp(car_in_time.getDate());
                            Date date = new Date(m);
                            String good_car_in_time = dateFormat.format(date);
                            String sql_book = "select * from book where Car_number = ?";
                            new BmobQuery<book>().doSQLQuery(sql_book, new SQLQueryListener<book>() {
                                @Override
                                public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                                    if (e == null) {
                                        List<book> book_list = (List<book>) bmobQueryResult.getResults();
                                        if (book_list.size() == 0) {
                                            CarList cars = new CarList();
                                            cars.setIsbook("否");
                                            cars.setCar_number(car_number);
                                            cars.setCar_in_time(good_car_in_time);
                                            CarDates.add(cars);
                                        } else {
                                            CarList cars = new CarList();
                                            cars.setIsbook("是");
                                            cars.setCar_number(car_number);
                                            cars.setCar_in_time(good_car_in_time);
                                            CarDates.add(cars);

                                        }
                                        CarData = CarDates;
                                        linAdapter.setCarData(CarData);
                                        linAdapter.notifyDataSetChanged();
                                    } else {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(recyclerView.getContext(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, car_number);
                        }
                    } else {
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(), "查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                } else {
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    Toast.makeText(recyclerView.getContext(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, input_car_number);
    }

    private void void_select_all() {
        List<CarList> CarDates = new ArrayList<>();
        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
        String sql = "select * from Card where isused = true order by Car_number";
        new BmobQuery<Card>().doSQLQuery(sql, new SQLQueryListener<Card>() {
            @Override
            public void done(BmobQueryResult<Card> bmobQueryResult, BmobException e) {
                if (e == null) {
                    List<Card> list = bmobQueryResult.getResults();
                    if (list != null && list.size() > 0) {
                        for (Card cdata : list) {
                            int id = cdata.getId();
                            String objectId = cdata.getObjectId();
                            String car_number = cdata.getCar_number();
                            BmobDate car_in_time = cdata.getCar_in_time();
                            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            long m = car_in_time.getTimeStamp(car_in_time.getDate());
                            Date date = new Date(m);
                            String good_car_in_time = dateFormat.format(date);
                            String sql_book = "select * from book where Car_number = ?";
                            new BmobQuery<book>().doSQLQuery(sql_book, new SQLQueryListener<book>() {
                                @Override
                                public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                                    if (e == null) {
                                        List<book> book_list = (List<book>) bmobQueryResult.getResults();
                                        if (book_list.size() == 0) {
                                            CarList cars = new CarList();
                                            cars.setObjectId(objectId);
                                            cars.setId(id);
                                            cars.setIsbook("否");
                                            cars.setCar_number(car_number);
                                            cars.setCar_in_time(good_car_in_time);
                                            CarDates.add(cars);

                                        } else {
                                            CarList cars = new CarList();
                                            cars.setObjectId(objectId);
                                            cars.setId(id);
                                            cars.setIsbook("是");
                                            cars.setCar_number(car_number);
                                            cars.setCar_in_time(good_car_in_time);
                                            CarDates.add(cars);
                                        }
                                        //DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new CarDiffCallBack(CarDates, CarData));
                                        CarData = CarDates;
                                        linAdapter.setCarData(CarData);
                                        linAdapter.notifyDataSetChanged();
                                        //diffResult.dispatchUpdatesTo(linAdapter);
                                    } else {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(recyclerView.getContext(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, car_number);

                        }
                    } else {
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(recyclerView.getContext(), "查询成功：车库中无数据", Toast.LENGTH_SHORT).show();
                    }

                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                } else {
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    Toast.makeText(recyclerView.getContext(), "查询失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}