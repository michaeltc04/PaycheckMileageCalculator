package com.michaelt.paycheckmileagecalculator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.michaelt.paycheckmileagecalculator.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Wilson on 3/7/2015.
 */
public class EfficiencyFragment extends Fragment {

    private Context mContext;
    private View mView;
    private TextView mEfficiency, mEfficiencyPercentage;
    private EditText mCurrentDistanceValue, mGasolinePurchasedValue, mCostValue;
    private double mCurrentDistance, mGasolinePurchased, mTotalCost;
    private Button mButton;
    SharedPreferences sp;
    SharedPreferences.Editor editor;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.mileage_fragment, null);
        mCurrentDistanceValue = (EditText) mView.findViewById(R.id.current_mileage_value);
        mGasolinePurchasedValue = (EditText) mView.findViewById(R.id.amount_purchased_value);
        mCostValue = (EditText) mView.findViewById(R.id.gas_cost_value);
        mButton = (Button) mView.findViewById(R.id.efficiency_calculate);
        mEfficiency = (TextView) mView.findViewById(R.id.mpg_view_value);
        mEfficiencyPercentage = (TextView) mView.findViewById(R.id.efficiency_value);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentDistanceValue.setText("4140");
                mGasolinePurchasedValue.setText("15");
                mCostValue.setText("44.85");

                mCurrentDistance = Double.parseDouble(mCurrentDistanceValue.getText().toString());
                mGasolinePurchased = Double.parseDouble(mGasolinePurchasedValue.getText().toString());
                mTotalCost = Double.parseDouble(mCostValue.getText().toString());
                sp = getActivity().getSharedPreferences("Total Cost", Context.MODE_PRIVATE);
                int d = new Double(mTotalCost + (sp.getInt("Total Cost", 0))/100).intValue();
                editor = sp.edit();
                editor.putInt("Total Cost", d);
                //editor.putInt("Total Cost", 0);
                calculateEfficiency();
            }
        });
        return mView;
    }

    private void calculateEfficiency() {

        //mpg = new mileage - old mileage / old gallons purchased
        //effic% = new mpg / old mpg

        boolean flag = true;
        sp = getActivity().getSharedPreferences("Previous Distance", Context.MODE_PRIVATE);
        double oldDistance = sp.getInt("Previous Distance", 0);
        sp = getActivity().getSharedPreferences("Efficiency", Context.MODE_PRIVATE);
        double oldEfficiency = sp.getInt("Efficiency", 0) / 100;
        if (oldDistance == 0) flag = false;
        oldDistance = 3900;
        oldEfficiency = 16;

        double newEfficiency = (mCurrentDistance - oldDistance) / mGasolinePurchased;
        double currentPercentage = newEfficiency / oldEfficiency;

        if (currentPercentage < 0) {
            mEfficiencyPercentage.setTextColor(getResources().getColor(R.color.red));
        } else {
            mEfficiencyPercentage.setTextColor(getResources().getColor(R.color.green));
        }

        NumberFormat nf = new DecimalFormat("00.##");
        mEfficiency.setText("" + nf.format(newEfficiency));
        mEfficiencyPercentage.setText("" + nf.format(currentPercentage) + "%");

        //sp = getActivity().getSharedPreferences("Efficiency", );
//        sp = getActivity().getSharedPreferences("Previous Distance", Context.MODE_PRIVATE);
//        sp = getActivity().getSharedPreferences("Efficiency", Context.MODE_PRIVATE);
//        sp = getActivity().getSharedPreferences("Efficiency Percentage", Context.MODE_PRIVATE);
    }
}
