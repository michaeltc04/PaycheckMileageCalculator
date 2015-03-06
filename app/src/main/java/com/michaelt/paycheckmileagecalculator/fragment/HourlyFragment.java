package com.michaelt.paycheckmileagecalculator.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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
    private int mFilingStatusSelection;
    SharedPreferences.Editor editor;
    SharedPreferences sp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.fragment_hourly, null);
        editSavedRate = (EditText) mView.findViewById(R.id.edit_hourly_rate);
        editHoursValue = (EditText) mView.findViewById(R.id.edit_hours_worked);

        //Creates the desired spinner for Filing Status
        status = new String[]{"Single", "Joint", "Married", "Head"};
        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner_hr);
        PayFragmentAdapter myAdapter = new PayFragmentAdapter(mContext, status);
        spinner.setAdapter(myAdapter);

        //Inputs the last input hourly rate into its EditText box
        sp = getActivity().getSharedPreferences("Hourly Rate", Context.MODE_PRIVATE);
        value = sp.getString("Hourly Rate", "");
        if (!value.equals("")) {
            editSavedRate.setText("" + sp.getString("Hourly Rate", ""));
        }

        //Inputs the last input amount of hours worked into its EditText box
        sp = getActivity().getSharedPreferences("Hours Worked", Context.MODE_PRIVATE);
        value = sp.getString("Hours Worked", "");
        if (!value.equals("")) {
            editHoursValue.setText("" + sp.getString("Hours Worked", ""));
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mFilingStatusSelection = parent.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //do nothing
            }
        });

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        //Stores entered hourly rate value into preferences
        String hourlyR = editSavedRate.getText().toString();
        if (hourlyR != null && !hourlyR.equals("")) {
            sp = getActivity().getSharedPreferences("Hourly Rate", Context.MODE_PRIVATE);
            editor = sp.edit();
            value = editSavedRate.getText().toString();
            editor.putString("Hourly Rate", value);
            editor.commit();
        }

        //Stores entered hours worked value into preferences
        String hours = editHoursValue.getText().toString();
        if (hours != null && !hours.equals("")) {
            sp = getActivity().getSharedPreferences("Hours Worked", Context.MODE_PRIVATE);
            editor = sp.edit();
            value = editHoursValue.getText().toString();
            editor.putString("Hours Worked", value);
            editor.commit();
        }
    }

    public int getSelection() {
        return mFilingStatusSelection;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


}