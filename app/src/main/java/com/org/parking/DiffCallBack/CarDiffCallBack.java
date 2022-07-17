package com.org.parking.DiffCallBack;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.org.parking.List.CarList;

import java.util.List;

public class CarDiffCallBack extends DiffUtil.Callback {
    List<CarList> newList;
    List<CarList> oldList;

    public CarDiffCallBack(List<CarList> newList, List<CarList> oldList) {
        this.newList = newList;
        this.oldList = oldList;
    }

    @Override
    public int getOldListSize() {
        return oldList != null ? oldList.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return newList != null ? newList.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        CarList newitem = newList.get(newItemPosition);
        CarList olditem = oldList.get(oldItemPosition);

        return newitem.getId()==olditem.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        CarList newitem = newList.get(newItemPosition);
        CarList olditem = oldList.get(oldItemPosition);
        if(!TextUtils.equals(newitem.getCar_number(),olditem.getCar_number())){
            return false;
        }
        if(!TextUtils.equals(newitem.getIsbook(),olditem.getIsbook())){
            return false;
        }
        if(!TextUtils.equals(newitem.getCar_in_time(),olditem.getCar_in_time())){
            return false;
        }
        return true;
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        CarList newitem = newList.get(newItemPosition);
        CarList olditem = oldList.get(oldItemPosition);
        Bundle bundle = new Bundle();
        if(!TextUtils.equals(newitem.getCar_number(),olditem.getCar_number())){
            bundle.putString("Car_number",newitem.getCar_number());
        }
        if(!TextUtils.equals(newitem.getIsbook(),olditem.getIsbook())){
            bundle.putString("Isbook",newitem.getIsbook());
        }
        if(!TextUtils.equals(newitem.getCar_in_time(),olditem.getCar_in_time())){
            bundle.putString("Car_in_time",newitem.getCar_in_time());
        }
        return bundle;

    }
}
