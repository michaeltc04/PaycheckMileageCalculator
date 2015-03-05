package com.michaelt.paycheckmileagecalculator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

public class SalaryFragment extends Fragment {
    private View mView;
    private String[] status;
    private Context mContext;
    private EditText gross_pay_value;
    private double savedGrossValue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.fragment_salary, null);
        gross_pay_value = (EditText) mView.findViewById(R.id.edit_gross_pay);
        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner);
        status = new String[]{"Single", "Joint", "Married", "Head"};

        PayFragmentAdapter myAdapter = new PayFragmentAdapter(mContext, status);
        spinner.setAdapter(myAdapter);

        savedGrossValue = savedInstanceState == null ? 0 : savedInstanceState.getDouble("hoursWorkedInput", 0.0);
        if (savedGrossValue != 0.0)gross_pay_value.setText("" + savedGrossValue);

        return mView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
