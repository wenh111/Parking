package com.org.parking.CarNumberDiscernUntil;

import android.content.Context;

import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class RecognizeServiceCarNumber {
    public interface ServiceListener {
        void onResult(String result);
        void onError(String Error);
    }
    public static void recLicensePlate(Context ctx, String filePath, final ServiceListener listener) {
        OcrRequestParams param = new OcrRequestParams();
        param.setImageFile(new File(filePath));
        OCR.getInstance(ctx).recognizeLicensePlate(param, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult result) {
                try {
                    JSONObject car_number_json;
                    String json = result.getJsonRes();
                    car_number_json  = new JSONObject(json);
                    String words_result =  car_number_json.getString("words_result");
                    car_number_json = new JSONObject(words_result);
                    String car_number = car_number_json.getString("number");
                    listener.onResult(car_number);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(OCRError error) {
                listener.onError(error.getMessage());
            }
        });
    }

}
