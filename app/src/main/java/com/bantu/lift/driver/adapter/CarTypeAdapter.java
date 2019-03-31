package com.bantu.lift.driver.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bantu.lift.driver.R;
import com.bantu.lift.driver.modelclass.GetCarTypeModel.Datum;

import java.util.List;

public class CarTypeAdapter extends BaseAdapter {

    List<Datum> jobtypeList;
    Context context;

    public CarTypeAdapter(Context context, List<Datum> jobtypeList) {
        this.context = context;
        this.jobtypeList = jobtypeList;
    }

    @Override
    public int getCount() {
        return jobtypeList.size();
    }

    @Override
    public Object getItem(int position) {
        return jobtypeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        String cur_obj = jobtypeList.get(position).getCarTypeName();
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();
        View row = inflater.inflate(R.layout.spinner_jobtype, parent, false);
        TextView label = row.findViewById(R.id.text_spinner);
        label.setText(cur_obj);
            /*label.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            label.setTextSize(18);*/
        return row;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        String cur_obj = jobtypeList.get(position).getCarTypeName();
        LayoutInflater inflater =((Activity)context). getLayoutInflater();
        View dropDownView = inflater.inflate(R.layout.spinner_jobtypedropdown, parent, false);
        TextView dd_GoText = dropDownView.findViewById(R.id.text_spinnerDropdown);
        dd_GoText.setText(cur_obj);
        return dropDownView;
    }
}