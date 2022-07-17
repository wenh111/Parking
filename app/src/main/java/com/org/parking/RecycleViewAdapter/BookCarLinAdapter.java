package com.org.parking.RecycleViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.List.BookCarList;
import com.org.parking.R;

import java.util.List;

public class BookCarLinAdapter extends RecyclerView.Adapter<BookCarLinAdapter.LinearViewHolder> {
    private Context context;
    public List<BookCarList> book_cars_data;

    public BookCarLinAdapter(Context context, List<BookCarList> book_cars_data) {
        this.context = context;
        this.book_cars_data = book_cars_data;
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.select_book_car_item,parent,false));
    }

    @Override
    public void onBindViewHolder(BookCarLinAdapter.LinearViewHolder holder, int position) {
        holder.out_location.setText(book_cars_data.get(position).getLocation());
        holder.car_number.setText(book_cars_data.get(position).getCar_number());
        holder.out_in_time.setText(book_cars_data.get(position).getBook_time());
        holder.out_expire_time.setText(book_cars_data.get(position).getExpiration_time());
    }


    @Override
    public int getItemCount() {
        return book_cars_data ==  null ? 0 : book_cars_data.size();
    }

    public class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView out_location,car_number,out_in_time,out_expire_time;
        public LinearViewHolder(View itemView) {
            super(itemView);
            out_location = itemView.findViewById(R.id.out_location);
            car_number = itemView.findViewById(R.id.car_number);
            out_in_time = itemView.findViewById(R.id.out_in_time);
            out_expire_time = itemView.findViewById(R.id.out_expire_time);
        }
    }
}
