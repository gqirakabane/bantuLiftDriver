package com.bantu.lift.driver.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.bantu.lift.driver.R;
import com.bantu.lift.driver.constant.CommonMeathod;
import com.bantu.lift.driver.modelclass.GetPullCreatedModelclass.Datum;

import java.util.List;

public class GetLiftAdapter extends RecyclerView.Adapter<GetLiftAdapter.MyViewHolder> {
    List<Datum> data;
    Context context;
    public GetLiftAdapter(Context context, List<Datum> data) {
        this.context = context;
        this.data = data;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_lift, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.luggage.setText(data.get(i).getLuggage());
        myViewHolder.passerger.setText(data.get(i).getPassengers());
        myViewHolder.carName.setText(data.get(i).getCarName());
        myViewHolder.carNumber.setText(data.get(i).getCarNumber());
        myViewHolder.endLocation.setText(data.get(i).getDropAddress());
        myViewHolder.startLocation.setText(data.get(i).getPickupAddress());
        myViewHolder.price.setText("R "+data.get(i).getAmount().toString().trim());
        myViewHolder.startdate.setText(CommonMeathod.parseDateToddMMyyyy(data.get(i).getStartDateTime()));
        if(TextUtils.isEmpty(data.get(i).getCarName()))
        {
            myViewHolder.tvCarNameDivider.setVisibility(View.GONE);
        }

        //Smoking divider
        if (data.get(i).getSmoking().equalsIgnoreCase("1")) {
            myViewHolder.smoking.setVisibility(View.VISIBLE);
            myViewHolder.tvSmokingDivider.setVisibility(View.VISIBLE);
        } else {
            myViewHolder.smoking.setVisibility(View.GONE);
            myViewHolder.tvSmokingDivider.setVisibility(View.GONE);
        }


        //Smoking divider
        if(TextUtils.isEmpty(data.get(i).getLuggage())||data.get(i).getLuggage().equalsIgnoreCase("Luggage")) {
            myViewHolder.luggage.setVisibility(View.GONE);
            myViewHolder.tvSmokingDivider.setVisibility(View.GONE);
        } else {
            myViewHolder.luggage.setVisibility(View.VISIBLE);
            myViewHolder.tvSmokingDivider.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView smoking, luggage, passerger, carName, carNumber, endLocation, startLocation, startdate, price;
        TextView tvSmokingDivider;
        TextView tvCarNameDivider;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            smoking = itemView.findViewById(R.id.smoking);
            luggage = itemView.findViewById(R.id.luggage);
            passerger = itemView.findViewById(R.id.passerger);
            carName = itemView.findViewById(R.id.carName);
            carNumber = itemView.findViewById(R.id.carNumber);
            startLocation = itemView.findViewById(R.id.startLocation);
            endLocation = itemView.findViewById(R.id.endLocation);
            startdate = itemView.findViewById(R.id.startdate);
            price = itemView.findViewById(R.id.price);
            tvSmokingDivider=itemView.findViewById(R.id.tv_smoking_divider);
            tvCarNameDivider=itemView.findViewById(R.id.tv_car_name_divider);
        }
    }
}
