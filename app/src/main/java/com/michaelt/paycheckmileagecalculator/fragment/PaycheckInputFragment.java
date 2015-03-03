package com.michaelt.paycheckmileagecalculator.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

public class PaycheckInputFragment extends Fragment {
    private View mView;
    private Context mContext;
    private String[] mStates, mPayTypes;
    private Spinner mStateSpinner, mPaySpinner;
    private String mFragTag, mSelectedState;
    private HourlyFragment mHourlyFragment;
    private SalaryFragment mSalaryFragment;
    private Button mButton;
    private TextView mFedTaxView, mMedTaxView, mSSTaxView;
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.paycheck_input_fragment, null);
        createSpinners();
        mHourlyFragment = new HourlyFragment();
        mSalaryFragment = new SalaryFragment();
        mButton = (Button) mView.findViewById(R.id.calculate_button);
        mFedTaxView = (TextView) mView.findViewById(R.id.federal_tax_value);
        mMedTaxView = (TextView) mView.findViewById(R.id.medicare_tax_value);
        mSSTaxView = (TextView) mView.findViewById(R.id.social_security_tax_value);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        mFragTag = "Salary";
        ft.add(R.id.pay_fragment_container, mSalaryFragment);
        ft.commit();

        createListeners();
        return mView;
    }

    private void createListeners() {
        mStateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mSelectedState = mStateSpinner.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mPaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isFirst) {
                    isFirst = false;
                    return;
                }
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                String tester = mFragTag.toString();
                if (tester.equalsIgnoreCase("Hourly")) {
                    mFragTag = "Salary";
                    ft.replace(R.id.pay_fragment_container, mSalaryFragment);
                } else {
                    mFragTag = "Hourly";
                    ft.replace(R.id.pay_fragment_container, mHourlyFragment);
                }
                ft.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (mSelectedState.equals("Alabama") | mSelectedState.equals("Washington") | mSelectedState.equals("Hawaii") |
                mSelectedState.equals("California") | mSelectedState.equals("Mississippi") | mSelectedState.equals("Ohio")) {
                    mFedTaxView.setText("8%");
                } else {
                    mFedTaxView.setText("4%");
                }
            }
        });
    }

    public void createSpinners() {
        mStateSpinner = (Spinner) mView.findViewById(R.id.state_spinner);
        mStates = new String[] {"Alabama", "Alaska", "Arizona", "Arkansas", "California", "Colorado", "Connecticut",
                "Delaware", "Florida", "Georgia", "Hawaii", "Idaho", "Illinois", "Indiana", "Iowa", "Kansas", "Kentucky",
                "Louisiana", "Maine", "Maryland", "Massachusetts", "Michigan", "Minnesota", "Mississippi", "Missouri",
                "Montana", "Nebraska", "Nevada", "New Hampshire", "New Jersey", "New Mexico", "New York", "North Carolina",
                "North Dakota", "Ohio", "Oklahoma", "Oregon", "Pennsylvania", "Rhode Island", "South Carolina",
                "South Dakota", "Tennessee", "Texas", "Utah", "Vermont", "Virginia", "Washington", "West Virginia",
                "Wisconsin", "Wyoming"};
        PayFragmentAdapter myAdapter = new PayFragmentAdapter(mContext, mStates);
        mStateSpinner.setAdapter(myAdapter);

        mPaySpinner = (Spinner) mView.findViewById(R.id.pay_type_spinner);
        mPayTypes = new String[] {"Salary", "Hourly"};
        PayFragmentAdapter myPayAdapter = new PayFragmentAdapter(mContext, mPayTypes);
        mPaySpinner.setAdapter(myPayAdapter);
    }
}
