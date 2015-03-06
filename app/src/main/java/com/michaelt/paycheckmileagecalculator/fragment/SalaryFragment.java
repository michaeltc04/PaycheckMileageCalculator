package com.michaelt.paycheckmileagecalculator.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

public class SalaryFragment extends Fragment {
    private View mView;
    private String[] status;
    private Context mContext;
    private EditText editGrossValue;
    private int mFilingStatusSelection;
    SharedPreferences.Editor editor;
    SharedPreferences sp;
    String value;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.fragment_salary, null);
        editGrossValue = (EditText) mView.findViewById(R.id.edit_gross_pay);

        //Creates the desired spinner for Filing Status
        status = new String[]{"Single", "Joint", "Married", "Head"};
        Spinner spinner = (Spinner) mView.findViewById(R.id.filing_status_spinner_sal);
        PayFragmentAdapter myAdapter = new PayFragmentAdapter(mContext, status);
        spinner.setAdapter(myAdapter);

        //Inputs the last input gross pay value into its EditText box
        sp = getActivity().getSharedPreferences("Gross Pay", Context.MODE_PRIVATE);
        value = sp.getString("Gross Pay", "");
        if (!value.equals("")) {
            editGrossValue.setText("" + sp.getString("Gross Pay", ""));
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

        //Stores entered gross value into preferences
        String gross = editGrossValue.getText().toString();
        if (gross != null && !gross.equals("")) {
            sp = getActivity().getSharedPreferences("Gross Pay", Context.MODE_PRIVATE);
            editor = sp.edit();
            value = editGrossValue.getText().toString();
            editor.putString("Gross Pay", value);
            editor.commit();
        }
    }

    public int getSelection() {
        return mFilingStatusSelection;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
