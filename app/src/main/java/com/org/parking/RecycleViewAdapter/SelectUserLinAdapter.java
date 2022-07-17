package com.org.parking.RecycleViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.org.parking.List.UserList;
import com.org.parking.R;

import java.util.List;

public class SelectUserLinAdapter extends RecyclerView.Adapter<SelectUserLinAdapter.LinearViewHolder> {


    private Context context;
    public List<UserList> user_data;

    public SelectUserLinAdapter(Context context, List<UserList> user_data) {
        this.context = context;
        this.user_data = user_data;
    }

    @Override
    public LinearViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(context).inflate(R.layout.select_user_item,parent,false));
    }

    @Override
    public void onBindViewHolder(LinearViewHolder holder, int position) {
        holder.name.setText(user_data.get(position).getName());
        holder.out_email.setText(String.valueOf(user_data.get(position).getAccount()));
        holder.out_telephone_number.setText(String.valueOf(user_data.get(position).getTelephone_number()));
        holder.out_identity.setText(String.valueOf(user_data.get(position).getIdentity()));
    }

    @Override
    public int getItemCount() {
        return user_data ==  null ? 0 : user_data.size();
    }
    class LinearViewHolder extends RecyclerView.ViewHolder{
        private TextView name,out_email,out_identity,out_telephone_number;
        public LinearViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            out_email = itemView.findViewById(R.id.out_email);
            out_identity = itemView.findViewById(R.id.out_identity);
            out_telephone_number = itemView.findViewById(R.id.out_telephone_number);
        }
    }
}
