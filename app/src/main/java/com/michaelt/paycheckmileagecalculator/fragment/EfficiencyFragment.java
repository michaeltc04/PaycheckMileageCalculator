package com.michaelt.paycheckmileagecalculator.fragment;

import android.app.AlertDialog;
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
    private Button mButton, mClearButton;
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
        mClearButton = (Button) mView.findViewById(R.id.clear_button);
        mEfficiency = (TextView) mView.findViewById(R.id.mpg_view_value);
        mEfficiencyPercentage = (TextView) mView.findViewById(R.id.efficiency_value);

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp = getActivity().getSharedPreferences("Previous Distance", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("Previous Distance", -1);
                editor.commit();
                sp = getActivity().getSharedPreferences("Efficiency", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("Efficiency", -1);
                editor.commit();
                sp = getActivity().getSharedPreferences("Total Cost", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("Total Cost", -1);
                editor.commit();
                sp = getActivity().getSharedPreferences("Previous Amount Purchased", Context.MODE_PRIVATE);
                editor = sp.edit();
                editor.putInt("Previous Amount Purchased", -1);
                editor.commit();
            }
        });

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isEmpty(mCurrentDistanceValue) |
                        isEmpty(mGasolinePurchasedValue) |
                        isEmpty(mCostValue)) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Cannot leave boxes blank!").setTitle("Improper Input");
                    AlertDialog dialog = builder.create();
                    dialog.show();

                } else {
                    mCurrentDistance = Double.parseDouble(mCurrentDistanceValue.getText().toString());
                    mGasolinePurchased = Double.parseDouble(mGasolinePurchasedValue.getText().toString());
                    mTotalCost = Double.parseDouble(mCostValue.getText().toString());

                    //Save total cost
                    sp = getActivity().getSharedPreferences("Total Cost", Context.MODE_PRIVATE);
                    int d = new Double(mTotalCost + (sp.getInt("Total Cost", 0))/100).intValue();
                    editor = sp.edit();
                    editor.putInt("Total Cost", d);
                    calculateEfficiency();

                    mCurrentDistanceValue.setText("");
                    mGasolinePurchasedValue.setText("");
                    mCostValue.setText("");
                }
            }
        });
        return mView;
    }

    private void calculateEfficiency() {
        double oldEfficiency = 0, newEfficiency = 0, oldDistance = 0, currentPercentage = 0, oldAmount = 0;

        //mpg = new mileage - old mileage / old gallons purchased
        //effic% = new mpg / old mpg
        sp = getActivity().getSharedPreferences("Previous Distance", Context.MODE_PRIVATE);
        oldDistance = sp.getInt("Previous Distance", -1);
        sp = getActivity().getSharedPreferences("Efficiency", Context.MODE_PRIVATE);
        oldEfficiency = sp.getInt("Efficiency", -1);
        sp = getActivity().getSharedPreferences("Previous Amount Purchased", Context.MODE_PRIVATE);
        oldAmount = sp.getInt("Previous Amount Purchased", -1);

        NumberFormat nf = new DecimalFormat("0.##");

        //First run through after fresh data
        if (oldDistance == -1 || oldEfficiency == -1 || oldAmount == -1) {
            oldDistance = 0;
            currentPercentage = 0;
            newEfficiency = 100;
            mEfficiencyPercentage.setText("TBD");
            mEfficiencyPercentage.setText("TBD");

        //All following runs
        } else {
            newEfficiency = (mCurrentDistance - oldDistance) / oldAmount;

            if (oldEfficiency == 100) {
                currentPercentage = 0;
                mEfficiencyPercentage.setText("TBD");
            } else {
                currentPercentage = newEfficiency / oldEfficiency;
                mEfficiencyPercentage.setText("" + nf.format(currentPercentage * 100) + "%");
            }
            mEfficiency.setText("" + nf.format(newEfficiency));

        }

        if (currentPercentage < 1) {
            mEfficiencyPercentage.setTextColor(getResources().getColor(R.color.red));
        } else {
            mEfficiencyPercentage.setTextColor(getResources().getColor(R.color.green));
        }

        //save current efficiency
        int ne = new Double(newEfficiency).intValue();
        sp = getActivity().getSharedPreferences("Efficiency", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("Efficiency", ne);
        editor.commit();

        //save current distance
        int cd = new Double(mCurrentDistance).intValue();
        sp = getActivity().getSharedPreferences("Previous Distance", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("Previous Distance", cd);
        editor.commit();

        //save current amount of gasoline purchased
        int gp = new Double(mGasolinePurchased).intValue();
        sp = getActivity().getSharedPreferences("Previous Amount Purchased", Context.MODE_PRIVATE);
        editor = sp.edit();
        editor.putInt("Previous Amount Purchased", gp);
        editor.commit();
    }


    /**
     * Checks to see if the EditText is empty of text
     * @param mEditText The EditText that is being tested
     * @return False if not empty, True if empty
     */
    private boolean isEmpty(EditText mEditText) {
        return mEditText.getText().toString().trim().length() == 0;
    }
}
