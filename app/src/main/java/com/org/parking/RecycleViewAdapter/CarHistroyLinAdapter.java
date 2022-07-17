package com.org.parking.RecycleViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.List.CarHistoryList;
import com.org.parking.R;

import java.util.List;

public class CarHistroyLinAdapter extends RecyclerView.Adapter<CarHistroyLinAdapter.LinearViewHolder> {


    private Context context;
    public List<CarHistoryList> hs_Cardata;

    public CarHistroyLinAdapter(Context context, List<CarHistoryList> hs_Cardata) {
        this.context = context;
        this.hs_Cardata = hs_Cardata;
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.select_history_item,parent,false));
    }

    @Override
    public void onBindViewHolder(LinearViewHolder holder, int position) {
        String sid= Integer.toString(hs_Cardata.get(position).getCard_id());
        holder.out_card_id.setText(sid);
        holder.car_number.setText(hs_Cardata.get(position).getCar_number());
        holder.out_in_time.setText(String.valueOf(hs_Cardata.get(position).getIn_time()));
        holder.out_out_time.setText(String.valueOf(hs_Cardata.get(position).getOut_time()));
        holder.out_cost.setText(String.valueOf(hs_Cardata.get(position).getCost()));
    }

    @Override
    public int getItemCount() {
        return hs_Cardata ==  null ? 0 : hs_Cardata.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView out_card_id,car_number,out_in_time,out_out_time,out_cost;
        public LinearViewHolder(View itemView) {
            super(itemView);
            out_card_id = itemView.findViewById(R.id.out_card_id);
            car_number = itemView.findViewById(R.id.car_number);
            out_in_time = itemView.findViewById(R.id.out_in_time);
            out_out_time = itemView.findViewById(R.id.out_out_time);
            out_cost = itemView.findViewById(R.id.out_cost);
        }
    }
}
