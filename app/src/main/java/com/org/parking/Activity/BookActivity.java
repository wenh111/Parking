package com.org.parking.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.org.parking.CarNumberDiscernUntil.FileUtil;
import com.org.parking.CarNumberDiscernUntil.RecognizeServiceCarNumber;
import com.org.parking.R;
import com.org.parking.Until.CustomDialog;
import com.org.parking.BmobTable.book;
import com.org.parking.Excention.error;
import com.org.parking.Excention.error_car_number;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobQueryResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SQLQueryListener;
import cn.bmob.v3.listener.SaveListener;
import wang.relish.widget.vehicleedittext.VehicleKeyboardHelper;

public class BookActivity extends AppCompatActivity {
    private EditText car_number1, car_number2, car_number3, car_number4, car_number5, car_number6, car_number7, car_number8, location;
    private Button back, add_summit, clean, photo;
    private String car_number;
    private String car_location;
    private boolean hasGotToken = false;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private String[] region;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        initAccessToken();
        findid();
        VehicleKeyboardHelper.bind(car_number1);
        car_number2.setTransformationMethod(replacementTransformationMethod);
        car_number3.setTransformationMethod(replacementTransformationMethod);
        car_number4.setTransformationMethod(replacementTransformationMethod);
        car_number5.setTransformationMethod(replacementTransformationMethod);
        car_number6.setTransformationMethod(replacementTransformationMethod);
        car_number7.setTransformationMethod(replacementTransformationMethod);
        car_number8.setTransformationMethod(replacementTransformationMethod);
        location.setTransformationMethod(replacementTransformationMethod);
        initView();
        setlisten();
    }

    private void setlisten() {
        Onclick onclick = new Onclick();
        back.setOnClickListener(onclick);
        add_summit.setOnClickListener(onclick);
        clean.setOnClickListener(onclick);
        photo.setOnClickListener(onclick);


    }

    class Onclick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.photo:
                    Intent intent;
                    if (!checkTokenStatus()) {
                        return;
                    }
                    intent = new Intent(BookActivity.this, CameraActivity.class);
                    intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                            FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath());
                    intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                            CameraActivity.CONTENT_TYPE_GENERAL);
                    startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
                    break;
                case R.id.back:
                    finish();
                    break;
                case R.id.clean:
                    clearText();
                    break;
                case R.id.add_summit:
                    add_carnumber();
                    break;
            }
        }
    }

    private void findid() {
        clean = findViewById(R.id.clean);
        back = findViewById(R.id.back);
        add_summit = findViewById(R.id.add_summit);
        location = findViewById(R.id.location);
        photo = findViewById(R.id.photo);
        car_number1 = findViewById(R.id.car_number1);
        car_number2 = findViewById(R.id.car_number2);
        car_number3 = findViewById(R.id.car_number3);
        car_number4 = findViewById(R.id.car_number4);
        car_number5 = findViewById(R.id.car_number5);
        car_number6 = findViewById(R.id.car_number6);
        car_number7 = findViewById(R.id.car_number7);
        car_number8 = findViewById(R.id.car_number8);
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
                Toast.makeText(BookActivity.this, "licence方式获取token失败" + error.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }, getApplicationContext());
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取", Toast.LENGTH_LONG).show();
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
        Toast.makeText(getApplicationContext(), "识别发生错误：" + Error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 识别成功回调，车牌识别
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

    //输入框实现自动跳转
    private void initView() {
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

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if (charSequence.length() > 0) {
                    car_number2.setEnabled(true);
                    car_number1.clearFocus();
                    car_number2.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(BookActivity.this.INPUT_METHOD_SERVICE);
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
                    car_number8.setHint("新");
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
                        car_number8.setHint("新");
                        car_number8.setEnabled(false);
                    }
                }
                return false;
            }
        });
        location.setInputType(InputType.TYPE_NULL);
        location.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                CustomDialog cd = new CustomDialog(BookActivity.this);
                cd.setBut_2(new CustomDialog.IOconfirmListener() {
                    @Override
                    public void oncofirm(CustomDialog dialog, int a, int b) {
                        region = getResources().getStringArray(R.array.region);
                        String letter = region[a];
                        String book_location;
                        if (b < 10) {
                            book_location = letter + "0" + b;
                        } else {
                            book_location = letter + b;
                        }
                        location.setText(book_location);

                    }
                }).show();
            }
        });

        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomDialog cd = new CustomDialog(BookActivity.this);
                cd.setBut_2(new CustomDialog.IOconfirmListener() {
                    @Override
                    public void oncofirm(CustomDialog dialog, int a, int b) {
                        region = getResources().getStringArray(R.array.region);
                        String letter = region[a];
                        String book_location;
                        if (b < 10) {
                            book_location = letter + "0" + b;
                        } else {
                            book_location = letter + b;
                        }
                        location.setText(book_location);
                    }
                }).show();
            }
        });
    }

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

    private void add_carnumber() {
        car_number = String.valueOf(car_number1.getText()) +
                String.valueOf(car_number2.getText()) +
                String.valueOf(car_number3.getText()) +
                String.valueOf(car_number4.getText()) +
                String.valueOf(car_number5.getText()) +
                String.valueOf(car_number6.getText()) +
                String.valueOf(car_number7.getText()) +
                String.valueOf(car_number8.getText());
        String good_car_number = car_number.toUpperCase();
        car_location = String.valueOf(location.getText());
        String good_car_location = car_location.toUpperCase();
        try {
            if (good_car_number.equals("") || good_car_location.equals("")) {
                throw new error("资料不完善");
            } else if (good_car_number.length() < 7) {
                throw new error_car_number();
            } else {
                //查找月租车表中有没有这个位置上有没有车或这个辆车是否已经存在
                String sql1 = "select * from book where Car_number = ?";
                String sql2 = "select * from book where location = ?";
                new BmobQuery<book>().doSQLQuery(sql1, new SQLQueryListener<book>() {
                    @Override
                    public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                        try {
                            if (e == null) {
                                List<book> list_car_number_null = (List<book>) bmobQueryResult.getResults();//获取数据条数
                                if (list_car_number_null.size() != 0) {
                                    throw new error_car_number();
                                } else {
                                    new BmobQuery<book>().doSQLQuery(sql2, new SQLQueryListener<book>() {
                                        @Override
                                        public void done(BmobQueryResult<book> bmobQueryResult, BmobException e) {
                                            try {
                                                if (e == null) {
                                                    List<book> list_location_null = (List<book>) bmobQueryResult.getResults();//获取数据条数
                                                    if (list_location_null.size() != 0) {
                                                        throw new error("位置重复");
                                                    } else {
                                                        //时间问题
                                                        Date date = new Date();
                                                        Calendar calendar = Calendar.getInstance();
                                                        calendar.add(Calendar.MONTH, 1);
                                                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                                        String book_time = dateFormat.format(date);
                                                        String expire_time = dateFormat.format(calendar.getTime());
                                                        Date date1 = dateFormat.parse(book_time);
                                                        Date date2 = dateFormat.parse(expire_time);
                                                        BmobDate good_book_time = new BmobDate(date1);
                                                        BmobDate good_expire_time = new BmobDate(date2);
                                                        //插入数据
                                                        book b1 = new book();
                                                        b1.setCar_number(good_car_number);
                                                        b1.setLocation(good_car_location);
                                                        b1.setBook_time(good_book_time);
                                                        b1.setExpiration_time(good_expire_time);
                                                        b1.save(new SaveListener<String>() {
                                                            @Override
                                                            public void done(String s, BmobException e) {
                                                                if (e == null) {
                                                                    Toast.makeText(BookActivity.this, "插入成功，到期时间为：" + expire_time, Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(BookActivity.this, "插入失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });

                                                    }
                                                } else {
                                                    Toast.makeText(BookActivity.this, "位置查询错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            } catch (com.org.parking.Excention.error error) {
                                                Toast.makeText(BookActivity.this, "位置上已经有车了", Toast.LENGTH_SHORT).show();
                                            } catch (ParseException parseException) {
                                                parseException.printStackTrace();
                                            }

                                        }
                                    }, good_car_location);
                                }
                            } else {
                                Toast.makeText(BookActivity.this, "车牌查询错误" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        } catch (com.org.parking.Excention.error_car_number error_car_number) {
                            Toast.makeText(BookActivity.this, "车牌已经存在", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, good_car_number);
            }
        } catch (com.org.parking.Excention.error error) {
            Toast.makeText(BookActivity.this, "请完善资料", Toast.LENGTH_SHORT).show();
        } catch (com.org.parking.Excention.error_car_number error_car_number) {
            Toast.makeText(BookActivity.this, "请填写正确的车牌号码", Toast.LENGTH_SHORT).show();
        }

    }

}