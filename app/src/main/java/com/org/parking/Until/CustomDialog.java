package com.org.parking.Until;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;

import com.org.parking.R;

public class CustomDialog extends Dialog implements View.OnClickListener {
    public CustomDialog(@NonNull Context context) {
        super(context);
    }

    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    private NumberPicker region, number;
    private Button but_1, but_2;
    private IOconfirmListener confirmListener;
    private IOnCancelListener cancelListener;
    private String[] letter;
    //private String[] region_number;

    public CustomDialog setBut_1(IOnCancelListener listener) {
        this.cancelListener = listener;
        return this;
    }

    public CustomDialog setBut_2(IOconfirmListener listener) {
        this.confirmListener = listener;
        return this;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_location);
        region = findViewById(R.id.region);
        number = findViewById(R.id.number);
        but_1 = findViewById(R.id.but_1);
        but_2 = findViewById(R.id.but_2);
        but_1.setOnClickListener(this);
        but_2.setOnClickListener(this);
        letter = new String[]{"A", "B", "C", "D"};
        region.setMaxValue(3);
        region.setMinValue(0);
        region.setValue(0);
        number.setMaxValue(50);
        number.setMinValue(1);
        number.setValue(1);

        number.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int value) {

                String data;

                if (value < 10) {
                    data = "0" + value;//让小于10的数前面加个0再输出
                } else {
                    data = String.valueOf(value); //大于10的数就不变
                }

                return data;
            }
        });
        number.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        region.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        region.setDisplayedValues(letter);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.but_1:
                if (cancelListener != null) {
                    cancelListener.oncancel(this);
                }
                dismiss();
                break;
            case R.id.but_2:
                int a = region.getValue();
                int b = number.getValue();
                if (confirmListener != null) {
                    confirmListener.oncofirm(this, a, b);
                }
                dismiss();
                break;
        }
    }

    public interface IOnCancelListener {
        void oncancel(CustomDialog dialog);

    }

    public interface IOconfirmListener {
        void oncofirm(CustomDialog dialog, int a, int b);
    }
}
