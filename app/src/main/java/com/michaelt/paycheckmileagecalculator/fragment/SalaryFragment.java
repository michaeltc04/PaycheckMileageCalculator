package com.michaelt.paycheckmileagecalculator.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.michaelt.paycheckmileagecalculator.R;

public class SalaryFragment extends Fragment {

    private View mView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_salary, null);

        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner);
        // Create an ArrayAdapter using the string array and a default spinner status_item
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity().getApplicationContext(),
                R.array.filing_status_array, android.R.layout.simple_spinner_item);
        // Specify the status_item to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the com.michaelt.paycheckmileagecalculator.adapter to the spinner
        spinner.setAdapter(adapter);

        // Inflate the status_item for this com.michaelt.paycheckmileagecalculator.fragment

        return mView;
    }

}
