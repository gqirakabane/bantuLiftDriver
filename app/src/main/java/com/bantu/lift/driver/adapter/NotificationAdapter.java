package com.bantu.lift.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.modelclass.NotificationModel.Datum;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {
    Context context;
    List<Datum> data;
    public NotificationAdapter(Context context, List<Datum> data) {
        this.context = context;
        this.data=data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_notification, viewGroup, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.natificationName.setText(data.get(i).getMessage());
        myViewHolder.startLocation.setText(data.get(i).getPickupLocation());
        myViewHolder.endLocation.setText(data.get(i).getDropLocation());
        myViewHolder.startdate.setText(data.get(i).getDateAndTime());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView natificationName,  startLocation, endLocation, startdate;
        TextView tvSmoking,tvSmokingDivider;
        Button acceptBtn;
        Button rejectBtn;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            natificationName=itemView.findViewById(R.id.natificationName);
            startLocation=itemView.findViewById(R.id.startLocation);
            endLocation=itemView.findViewById(R.id.endLocation);
            startdate=itemView.findViewById(R.id.startdate);

        }
    }
}

