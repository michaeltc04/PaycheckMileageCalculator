package com.michaelt.paycheckmileagecalculator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

public class HourlyFragment extends Fragment {
    private View mView;
    private String[] status;
    private Context mContext;
    private String value;
    private EditText editSavedRate, editHoursValue;
    SharedPreferences.Editor editor;
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.fragment_hourly, null);
        editSavedRate = (EditText) mView.findViewById(R.id.edit_hourly_rate);
        editHoursValue = (EditText) mView.findViewById(R.id.edit_hours_worked);
        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner);
        status = new String[]{"Single", "Joint", "Married", "Head"};

        PayFragmentAdapter myAdapter = new PayFragmentAdapter(mContext, status);
        spinner.setAdapter(myAdapter);

        sp = getActivity().getSharedPreferences("Hourly Rate", Context.MODE_PRIVATE);
        value = sp.getString("Hourly Rate", "");
        if (!value.equals("")) {
            editSavedRate.setText("" + sp.getString("Hourly Rate", ""));
        }

        sp = getActivity().getSharedPreferences("Hours Worked", Context.MODE_PRIVATE);
        value = sp.getString("Hours Worked", "");
        if (!value.equals("")) {
            editHoursValue.setText("" + sp.getString("Hours Worked", ""));
        }

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        String hourlyR = editSavedRate.getText().toString();
        String hours = editHoursValue.getText().toString();

        if (hourlyR != null && !hourlyR.equals("")) {
            sp = getActivity().getSharedPreferences("Hourly Rate", Context.MODE_PRIVATE);
            editor = sp.edit();
            value = editSavedRate.getText().toString();
            editor.putString("Hourly Rate", value);
            editor.commit();
//            savedRateValue = Double.parseDouble(hourlyR);
//            outState.putDouble("hourlyRateInput", savedRateValue);

        }
        if (hours != null && !hours.equals("")) {
            sp = getActivity().getSharedPreferences("Hours Worked", Context.MODE_PRIVATE);
            editor = sp.edit();
            value = editSavedRate.getText().toString();
            editor.putString("Hours Worked", value);
            editor.commit();
//            savedHoursValue = Double.parseDouble(hours);
//            outState.putDouble("hoursWorkedInput", savedHoursValue);
        }
    }


}