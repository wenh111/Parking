package com.org.parking.RecycleViewAdapter;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.List.CarList;
import com.org.parking.R;

import java.util.List;

public class CarLinAdapter extends RecyclerView.Adapter<CarLinAdapter.LinearViewHolder> {


    private Context context;
    public List<CarList> CarData;
    public CarLinAdapter(Context context, List<CarList> cars) {
        this.context = context;
        this.CarData = cars;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setCarData(List<CarList> carData) {
        CarData = carData;
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.select_parking_item,parent,false));
    }


    @Override
    public void onBindViewHolder(LinearViewHolder holder, int position) {
        //String sid= Integer.toString(Cardata.get(position).getId());
        holder.is_book.setText(CarData.get(position).getIsbook());
        holder.car_number.setText(CarData.get(position).getCar_number());
        holder.out_in_time.setText(String.valueOf(CarData.get(position).getCar_in_time()));
    }

    @Override
    public int getItemCount() {
        return CarData ==  null ? 0 : CarData.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView car_number,is_book,out_in_time;
        public LinearViewHolder(View itemView) {
            super(itemView);
            is_book = itemView.findViewById(R.id.is_book);
            car_number = itemView.findViewById(R.id.car_number);
            out_in_time = itemView.findViewById(R.id.out_in_time);
        }
    }
}
