package com.org.parking.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.org.parking.CarNumberDiscernUntil.FileUtil;
import com.org.parking.CarNumberDiscernUntil.RecognizeServiceCarNumber;
import com.org.parking.R;
import com.org.parking.BmobTable.Card;
import com.org.parking.BmobTable.book;
import com.org.parking.BmobTable.history;
import com.org.parking.Excention.error;
import com.org.parking.Excention.error_car_number;
import com.org.parking.Excention.null_car_number;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;

public class UserActivity extends AppCompatActivity {
    private EditText car_number1, car_number2, car_number3, car_number4, car_number5, car_number6, car_number7, car_number8;
    private Button photo, summit, parking_information, car_out, clear, log_out, book;
    private TextView cost;
    private SharedPreferences shareData;
    private ProgressBar pb;
    public String USER_EMAIL = "user_email";
    public String USER_PASSWORD = "user_password";
    public String USER_ISUSED = "user_isused";
    private final String TAG = "UserActivity";
    public boolean isdelete_Monthly_car = false;
    public String oid = null;
    public String msg;
    public double cost1;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
        this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        initAccessToken();
        findid();
        shareData = getSharedPreferences("data", 0);
        String dc1 = shareData.getString("dc", "10");
        cost.setText(dc1);
        //VehicleKeyboardHelper.bind(car_number1);
        car_number2.setTransformationMethod(replacementTransformationMethod);
        car_number3.setTransformationMethod(replacementTransformationMethod);
        car_number4.setTransformationMethod(replacementTransformationMethod);
        car_number5.setTransformationMethod(replacementTransformationMethod);
        car_number6.setTransformationMethod(replacementTransformationMethod);
        car_number7.setTransformationMethod(replacementTransformationMethod);
        car_number8.setTransformationMethod(replacementTransformationMethod);

        setlisten();
        initView();

    }

    private void initAccessToken() {
        OCR.getInstance(getApplicationContext()).initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Toast.makeText(UserActivity.this, "licence????????????token??????" + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }, getApplicationContext());
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token??????????????????", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    private void TextView(final String message) {
        //car_number1.setEnabled(false);
        EditText[] editTexts = new EditText[]{car_number1, car_number2, car_number3, car_number4, car_number5, car_number6, car_number7, car_number8};
        String car_number_message = message.toUpperCase();
        String[] numbers = car_number_message.split("");
        for (int i = 0; i < numbers.length; i++) {
            editTexts[i].setText(numbers[i]);
        }
    }

    private void infoPopText(final String result) {
        TextView(result);
    }

    private void infoErrorText(final String Error) {
        Toast.makeText(getApplicationContext(), "?????????????????????" + Error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "??????android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }


    //????????????????????????????????????????????????????????????
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
        car_out.setOnClickListener(onclick);
        //user_back.setOnClickListener(onclick);
        summit.setOnClickListener(onclick);
        parking_information.setOnClickListener(onclick);
        clear.setOnClickListener(onclick);
        log_out.setOnClickListener(onclick);
        book.setOnClickListener(onclick);
        photo.setOnClickListener(onclick);
    }

    //????????????
    private void findid() {
        pb = findViewById(R.id.pb);
        car_number1 = findViewById(R.id.car_number1);
        car_number2 = findViewById(R.id.car_number2);
        car_number3 = findViewById(R.id.car_number3);
        car_number4 = findViewById(R.id.car_number4);
        car_number5 = findViewById(R.id.car_number5);
        car_number6 = findViewById(R.id.car_number6);
        car_number7 = findViewById(R.id.car_number7);
        car_number8 = findViewById(R.id.car_number8);
        clear = findViewById(R.id.clear);
        photo = findViewById(R.id.photo);
        car_out = findViewById(R.id.car_out);
        summit = findViewById(R.id.number_summit);
        parking_information = findViewById(R.id.parking_information);
        cost = findViewById(R.id.cost);
        log_out = findViewById(R.id.log_out);
        book = findViewById(R.id.book);
    }

    //??????????????????????????????
    @NonNull
    private String GetCarNumber() throws error_car_number {
        String car_number = String.valueOf(car_number1.getText()) +
                car_number2.getText() +
                car_number3.getText() +
                car_number4.getText() +
                car_number5.getText() +
                car_number6.getText() +
                car_number7.getText() +
                car_number8.getText();
        String good_car_number = car_number.toUpperCase();
        if (good_car_number.equals("") || good_car_number.length() < 7) {
            throw new error_car_number();
        }
        return good_car_number;
    }

    //?????????????????????
    class Onclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.photo:
                    if (!checkTokenStatus()) {
                        return;
                    }
                    intent = new Intent(UserActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                            CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
                    //intentActivityResultLauncher.launch(intent);
                    break;
                case R.id.number_summit:
                    try {
                        pb.setVisibility(findViewById(R.id.pb).VISIBLE);
                        //?????????????????????????????????.
                        String good_car_number = GetCarNumber();
                        car_in(good_car_number);
                    } catch (error_car_number e) {
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(UserActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.parking_information:
                    intent = new Intent(UserActivity.this, ParkingInformationActivity.class);
                    startActivity(intent);
                    break;
                case R.id.car_out:
                    car_out();
                    break;
                case R.id.clear:
                    clearText();
                    break;
                case R.id.book:
                    intent = new Intent(UserActivity.this, BookActivity.class);
                    startActivity(intent);
                    break;
                case R.id.log_out:
                    logout();
                    break;
            }
        }
    }

    //???????????????????????????
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // ?????????????????????????????????
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            RecognizeServiceCarNumber.recLicensePlate(getApplicationContext(), FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeServiceCarNumber.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            infoPopText(result);
                        }

                        @Override
                        public void onError(String Error) {
                            infoErrorText(Error);
                        }
                    });
        }
    }

    //???????????????????????????
    private void initView() {
        VehicleKeyboardHelper.bind(car_number1);
        car_number2.setEnabled(false);
        car_number3.setEnabled(false);
        car_number4.setEnabled(false);
        car_number5.setEnabled(false);
        car_number6.setEnabled(false);
        car_number7.setEnabled(false);
        car_number8.setEnabled(false);
        car_number1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    car_number2.setEnabled(true);
                    car_number1.clearFocus();
                    car_number2.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(UserActivity.this.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(car_number2, 1);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number1.setEnabled(false);
                } else {
                    car_number1.setEnabled(true);
                    car_number2.setEnabled(false);
                }
            }
        });


        car_number2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number3.setEnabled(true);
                    car_number2.clearFocus();
                    car_number3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number2.setEnabled(false);
                } else {
                    car_number2.setEnabled(true);
                    car_number3.setEnabled(false);
                }
            }
        });

        car_number3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number4.setEnabled(true);
                    car_number3.clearFocus();
                    car_number4.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number3.setEnabled(false);
                } else {
                    car_number3.setEnabled(true);
                    car_number4.setEnabled(false);
                }
            }
        });
        car_number4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number5.setEnabled(true);
                    car_number4.clearFocus();
                    car_number5.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number4.setEnabled(false);
                } else {
                    car_number4.setEnabled(true);
                    car_number5.setEnabled(false);
                }
            }
        });


        car_number5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number6.setEnabled(true);
                    car_number5.clearFocus();
                    car_number6.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number5.setEnabled(false);
                } else {
                    car_number5.setEnabled(true);
                    car_number6.setEnabled(false);
                }
            }
        });
        car_number6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number7.setEnabled(true);
                    car_number6.clearFocus();
                    car_number7.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    car_number6.setEnabled(false);
                } else {
                    car_number6.setEnabled(true);
                    car_number7.setEnabled(false);
                }
            }
        });

        car_number7.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 0) {
                    car_number8.setEnabled(true);
                    car_number7.clearFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        car_number8.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    car_number7.setEnabled(false);
                    car_number8.setHint("");
                } else {
                    car_number8.setHint("???");
                }
            }
        });

        car_number2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number2.getText().length() <= 0) {
                        car_number1.setEnabled(true);
                        car_number1.requestFocus();
                        car_number2.clearFocus();
                        car_number1.setText("");
                    }
                }
                return false;
            }
        });

        car_number3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number3.getText().length() <= 0) {
                        car_number2.setEnabled(true);
                        car_number2.requestFocus();
                        car_number3.clearFocus();
                        car_number2.setText("");
                    }
                }
                return false;
            }
        });

        car_number4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number4.getText().length() <= 0) {
                        car_number3.setEnabled(true);
                        car_number3.requestFocus();
                        car_number4.clearFocus();
                        car_number3.setText("");
                    }
                }
                return false;
            }
        });
        car_number5.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number5.getText().length() <= 0) {
                        car_number4.setEnabled(true);
                        car_number4.requestFocus();
                        car_number5.clearFocus();
                        car_number4.setText("");
                    }
                }
                return false;
            }
        });
        car_number6.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number6.getText().length() <= 0) {
                        car_number5.setEnabled(true);
                        car_number5.requestFocus();
                        car_number6.clearFocus();
                        car_number5.setText("");
                    }
                }
                return false;
            }
        });
        car_number7.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number7.getText().length() <= 0) {
                        car_number6.setEnabled(true);
                        car_number6.requestFocus();
                        car_number7.clearFocus();
                        car_number6.setText("");
                    }
                }
                return false;
            }
        });

        car_number8.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KeyEvent.KEYCODE_DEL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    if (car_number8.getText().length() <= 0) {
                        car_number7.setEnabled(true);
                        car_number7.requestFocus();
                        car_number8.clearFocus();
                        car_number7.setText("");
                        car_number8.setHint("???");
                        car_number8.setEnabled(false);
                    }
                }
                return false;
            }
        });

    }

    //????????????????????????
    private void car_in(String good_car_number) {
        //???????????????
        String car_number = "select * from Card where Car_number = ?";
        new BmobQuery<Card>().doSQLQuery(car_number, new SQLQueryListener<Card>() {
            @Override
            public void done(BmobQueryResult<Card> bmobQueryResult, BmobException e) {
                try {
                    if (e == null) {
                        List<Card> list = (List<Card>) bmobQueryResult.getResults();
                        if (list.size() != 0) {
                            throw new error("??????????????????");
                        } else if (list.size() == 0) {
                            //???????????????????????????
                            String null_card = "select * from Card where isused = false order by id limit 1";
                            new BmobQuery<Card>().doSQLQuery(null_card, new SQLQueryListener<Card>() {
                                @Override
                                public void done(BmobQueryResult<Card> bmobQueryResult1, BmobException e) {
                                    if (e == null) {
                                        List<Card> list_null = (List<Card>) bmobQueryResult1.getResults();//??????????????????
                                        if (list_null.size() == 0) {//?????????0????????????????????????
                                            try {
                                                //????????????????????????
                                                Date date = new Date();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String in_time = dateFormat.format(date);
                                                Date date1 = dateFormat.parse(in_time);
                                                BmobDate good_in_time = new BmobDate(date1);
                                                Card c1 = new Card();
                                                c1.setCar_number(good_car_number);
                                                c1.setCar_in_time(good_in_time);
                                                c1.setIsused(true);
                                                c1.save(new SaveListener<String>() {
                                                    @Override
                                                    public void done(String s, BmobException e) {//????????????
                                                        if (e == null) {
                                                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                            clearText();
                                                            Toast.makeText(UserActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                            Toast.makeText(UserActivity.this, "????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } catch (ParseException parseException) {
                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                parseException.printStackTrace();
                                            }

                                        } else {
                                            try {
                                                String id = null;
                                                for (Card oid : list_null) {
                                                    id = oid.getObjectId();
                                                }
                                                Date date = new Date();
                                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                String in_time = dateFormat.format(date);
                                                Date date1 = dateFormat.parse(in_time);
                                                ;
                                                BmobDate good_in_time = new BmobDate(date1);
                                                Card c2 = new Card();
                                                c2.setIsused(true);
                                                c2.setCar_number(good_car_number);
                                                c2.setCar_in_time(good_in_time);
                                                c2.update(id, new UpdateListener() {
                                                    @Override
                                                    public void done(BmobException e) {//??????????????????
                                                        if (e == null) {
                                                            clearText();
                                                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                            clearText();
                                                            Toast.makeText(UserActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                                        } else {
                                                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                            Toast.makeText(UserActivity.this, "????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                            } catch (ParseException parseException) {
                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                parseException.printStackTrace();
                                            }

                                        }
                                    } else {
                                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        }
                    } else {
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(UserActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (com.org.parking.Excention.error error) {
                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                    Toast.makeText(UserActivity.this, "??????????????????", Toast.LENGTH_SHORT).show();
                }
            }
        }, good_car_number);
    }

    //????????????????????????
    private void car_out() {
        try {
            pb.setVisibility(findViewById(R.id.pb).VISIBLE);
            String good_car_number = GetCarNumber();
            String car_number = "select * from Card where Car_number = ?";
            new BmobQuery<Card>().doSQLQuery(car_number, new SQLQueryListener<Card>() {
                @Override
                public void done(BmobQueryResult<Card> bmobQueryResult, BmobException e) {
                    if (e == null) {
                        try {
                            List<Card> list_car_number = (List<Card>) bmobQueryResult.getResults();
                            if (list_car_number.size() == 0) {
                                throw new null_car_number();
                            } else {
                                //?????????????????????
                                //????????????
                                final String[] car_number_out = {null};
                                final String[] obid = {null};
                                final BmobDate[] bmob_car_in_time = {null};
                                final Integer[] crad_id = {null};
                                for (Card cn : list_car_number) {
                                    car_number_out[0] = cn.getCar_number();
                                    obid[0] = cn.getObjectId();
                                    bmob_car_in_time[0] = cn.getCar_in_time();
                                    crad_id[0] = cn.getId();
                                }

                                //??????????????????
                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String out_time = dateFormat.format(date);
                                Date date1 = dateFormat.parse(out_time);
                                BmobDate good_out_time = new BmobDate(date1);

                                //????????????????????????
                                String sql1 = "select * from book where Car_number = ? limit 1";
                                new BmobQuery<com.org.parking.BmobTable.book>().doSQLQuery(sql1, new SQLQueryListener<book>() {
                                    @Override
                                    public void done(BmobQueryResult<com.org.parking.BmobTable.book> bmobQueryResult2, BmobException e) {
                                        List<book> list_book_car_number = (List<book>) bmobQueryResult2.getResults();//??????????????????
                                        if (e == null) {
                                            BmobDate book_car_expire_time = null;
                                            for (book bn : list_book_car_number) {
                                                oid = bn.getObjectId();
                                                book_car_expire_time = bn.getExpiration_time();
                                            }
                                            //???????????????

                                            Timestamp in_time_cost = Timestamp.valueOf(bmob_car_in_time[0].getDate());
                                            Timestamp out_time_cost = Timestamp.valueOf(good_out_time.getDate());

                                            shareData = getSharedPreferences("data", 0);
                                            String dc1 = shareData.getString("dc", "10");

                                            long cha = out_time_cost.getTime() - in_time_cost.getTime();
                                            double good_cha = cha / (1000 * 60 * 60);
                                            double good_dc = Double.parseDouble(dc1);

                                            Log.d(TAG, "???????????????" + list_book_car_number.size());
                                            if (list_book_car_number.size() != 0) {
                                                Timestamp good_book_car_expire_time = Timestamp.valueOf(book_car_expire_time.getDate());
                                                long cha1 = good_book_car_expire_time.getTime() - out_time_cost.getTime();
                                                if (cha1 >= 0) {
                                                    cost1 = 0;
                                                    isdelete_Monthly_car = false;
                                                } else {
                                                    long cha2 = out_time_cost.getTime() - good_book_car_expire_time.getTime();
                                                    double good_cha1 = cha2 / (1000 * 60 * 60);
                                                    cost1 = good_cha1 * good_dc;
                                                    isdelete_Monthly_car = true;
                                                }
                                            } else {
                                                cost1 = good_cha * good_dc;
                                            }
                                            Log.d(TAG, "????????????" + list_car_number.size());
                                            msg = "?????????" + cost1;
                                            AlertDialog.Builder builder = new AlertDialog.Builder(UserActivity.this);
                                            builder.setTitle("??????");
                                            builder.setMessage(msg);
                                            builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    pb.setVisibility(findViewById(R.id.pb).VISIBLE);
                                                    //Toast.makeText(UserActivity.this,"???????????????????????????????????????...", Toast.LENGTH_SHORT).show();
                                                    //history??????????????????????????????
                                                    history h1 = new history();
                                                    //????????????
                                                    h1.setCar_number(car_number_out[0]);
                                                    //??????????????????
                                                    h1.setIn_time(bmob_car_in_time[0]);
                                                    //??????????????????
                                                    h1.setOut_time(good_out_time);
                                                    //???????????????
                                                    h1.setCost(cost1);
                                                    //??????????????????????????????ID
                                                    h1.setCard_id(crad_id[0]);
                                                    //????????????
                                                    h1.save(new SaveListener<String>() {
                                                        @Override
                                                        public void done(String s, BmobException e) {
                                                            if (e == null) {
                                                                try {
                                                                    Card c1 = new Card();
                                                                    //???????????????null
                                                                    String createdAt = "2000-01-01 00:00:00";
                                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                                    Date createdAtDate = sdf.parse(createdAt);
                                                                    BmobDate null_time = new BmobDate(createdAtDate);
                                                                    c1.setCar_in_time(null_time);
                                                                    //isused?????????false
                                                                    c1.setIsused(false);
                                                                    //???????????????null
                                                                    c1.setCar_number("");
                                                                    c1.update(obid[0], new UpdateListener() {
                                                                        @Override
                                                                        public void done(BmobException e) {
                                                                            if (e == null) {
                                                                                if (isdelete_Monthly_car == true) {
                                                                                    book bd = new book();
                                                                                    bd.setObjectId(oid);
                                                                                    bd.delete(new UpdateListener() {
                                                                                        @Override
                                                                                        public void done(BmobException e) {
                                                                                            if (e == null) {
                                                                                                clearText();
                                                                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                                                Toast.makeText(UserActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                                                                            } else {
                                                                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                                                Toast.makeText(UserActivity.this, "????????????????????????????????????", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        }
                                                                                    });
                                                                                } else {
                                                                                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                                    clearText();
                                                                                    Toast.makeText(UserActivity.this, "????????????????????????", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            } else {
                                                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                                Toast.makeText(UserActivity.this, "????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                                            }
                                                                        }
                                                                    });
                                                                } catch (ParseException parseException) {
                                                                    pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                    parseException.printStackTrace();
                                                                }


                                                            } else {
                                                                pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                                                Toast.makeText(UserActivity.this, "????????????????????????????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                                }
                                            });
                                            builder.show();
                                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                                        } else {
                                            Toast.makeText(UserActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }, good_car_number);

                            }
                        } catch (ParseException parseException) {
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            parseException.printStackTrace();
                        } catch (com.org.parking.Excention.null_car_number null_car_number) {
                            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                            Toast.makeText(UserActivity.this, "?????????????????????", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
                        Toast.makeText(UserActivity.this, "????????????????????????" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }, good_car_number);

        } catch (com.org.parking.Excention.error_car_number error_car_number) {
            pb.setVisibility(findViewById(R.id.pb).INVISIBLE);
            Toast.makeText(UserActivity.this, "??????????????????????????????", Toast.LENGTH_SHORT).show();
        }
    }

    //?????????????????????
    private void clearText() {
        car_number1.setText("");
        car_number2.setText("");
        car_number3.setText("");
        car_number4.setText("");
        car_number5.setText("");
        car_number6.setText("");
        car_number7.setText("");
        car_number8.setText("");
        car_number1.setEnabled(true);
        car_number2.setEnabled(false);
        car_number3.setEnabled(false);
        car_number4.setEnabled(false);
        car_number5.setEnabled(false);
        car_number6.setEnabled(false);
        car_number7.setEnabled(false);
        car_number8.setEnabled(false);
    }

    //????????????
    private void logout() {
        SharedPreferences userdata = null;
        if (userdata == null) {
            userdata = getSharedPreferences("user", Context.MODE_PRIVATE);
        }
        SharedPreferences.Editor user_editor = userdata.edit();
        user_editor.putString(USER_EMAIL, "");
        user_editor.putString(USER_PASSWORD, "");
        user_editor.putBoolean(USER_ISUSED, false);
        user_editor.commit();
        Log.d(TAG, "??????????????????" + userdata.getBoolean("user_isused", false));
        Log.d(TAG, "??????????????????" + userdata.getString("user_email", ""));
        Log.d(TAG, "??????????????????" + userdata.getString("user_password", ""));
        Intent intent = new Intent();
        intent = new Intent(UserActivity.this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    //????????????????????????
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // ??????????????????
        OCR.getInstance(getApplicationContext()).release();

    }
}