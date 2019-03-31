package com.bantu.lift.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.interFace.AdapterCallback;
import com.bantu.lift.driver.modelclass.RequestPollModelData.Datum;
import java.util.List;

public class GetRequestPullAdapter extends RecyclerView.Adapter<GetRequestPullAdapter.MyViewHolder> {
    List<Datum> data;
    Context context;
    AdapterCallback callback;
    public GetRequestPullAdapter(Context context, List<Datum> data, AdapterCallback callback) {
        this.callback = callback;
        this.context = context;
        this.data = data;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_requestpoll, viewGroup, false);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
//Display smoking data
        if(data.get(i).getSmoking().equalsIgnoreCase("0"))
        {
            myViewHolder.tvSmokingDivider.setVisibility(View.GONE);
            myViewHolder.tvSmoking.setVisibility(View.GONE);
        }else
        {
            myViewHolder.tvSmokingDivider.setVisibility(View.VISIBLE);
            myViewHolder.tvSmoking.setVisibility(View.VISIBLE);
        }if(data.get(i).getBudget().equalsIgnoreCase("") || Float.parseFloat(data.get(i).getBudget())<1)
        {
            myViewHolder.budgetLayout.setVisibility(View.GONE);
        }else
        {
            myViewHolder.budgetLayout.setVisibility(View.VISIBLE);
            myViewHolder.budget.setText("R "+data.get(i).getBudget());
        }

        //Display luggage value
        if(TextUtils.isEmpty(data.get(i).getLuggage())||data.get(i).getLuggage().equalsIgnoreCase("Luggage"))
        {
            myViewHolder.luggage.setVisibility(View.GONE);
        }else {
            myViewHolder.luggage.setText(data.get(i).getLuggage());
        }

        myViewHolder.passerger.setText(data.get(i).getPassengers());
        if (data.get(i).getCarName().equalsIgnoreCase("")&& data.get(i).getCarNumber().equalsIgnoreCase(""))
        {
            myViewHolder.carNamedot.setVisibility(View.GONE);
        }else {
            myViewHolder.carNamedot.setVisibility(View.VISIBLE);

        }
        myViewHolder.carName.setText(data.get(i).getCarName());
        myViewHolder.carNumber.setText(data.get(i).getCarNumber());
        myViewHolder.endLocation.setText(data.get(i).getDropAddress());
        myViewHolder.startLocation.setText(data.get(i).getPickupAddress());
        myViewHolder.requestedSeat.setText(data.get(i).getSeats());
        myViewHolder.price.setText("R "+data.get(i).getAmount());
        myViewHolder.startdate.setText(CommonMeathod.parseDateToddMMyyyy(data.get(i).getStartDateTime()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView requestedSeat,carNamedot,luggage, passerger, carName, carNumber, endLocation, startLocation, startdate, price;
        TextView tvSmoking,tvSmokingDivider,budget;
        Button acceptBtn;
        Button rejectBtn;
        LinearLayout budgetLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            luggage = itemView.findViewById(R.id.luggage);
            passerger = itemView.findViewById(R.id.passerger);
            carName = itemView.findViewById(R.id.carName);
            carNumber = itemView.findViewById(R.id.carNumber);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            startdate = itemView.findViewById(R.id.startdate);
            price = itemView.findViewById(R.id.price);
            tvSmoking=itemView.findViewById(R.id.tv_smoking);
            tvSmoking=itemView.findViewById(R.id.tv_smoking);
            tvSmokingDivider=itemView.findViewById(R.id.tv_smoking_divider);
            acceptBtn = itemView.findViewById(R.id.acceptBtn);
            rejectBtn = itemView.findViewById(R.id.rejectBtn);
            carNamedot = itemView.findViewById(R.id.carNamedot);
            budgetLayout = itemView.findViewById(R.id.budgetLayout);
            budget = itemView.findViewById(R.id.budget);
            requestedSeat = itemView.findViewById(R.id.requestedSeat);
            acceptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemClicked("2", data.get(getLayoutPosition()).getRequestId(),getLayoutPosition());
                    }
                }
            });
            rejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (callback != null) {
                        callback.onItemClicked("3", data.get(getLayoutPosition()).getRequestId(),getLayoutPosition());
                    }
                }
            });
        }
    }
}
