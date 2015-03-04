package com.michaelt.paycheckmileagecalculator.fragment;

import android.app.AlertDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.michaelt.paycheckmileagecalculator.R;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentAdapter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class PaycheckInputFragment extends Fragment {
    private View mView;
    private Context mContext;
    private String[] mStates, mPayTypes;
    private Spinner mStateSpinner, mPaySpinner;
    private String mFragTag, mSelectedState;
    private HourlyFragment mHourlyFragment;
    private SalaryFragment mSalaryFragment;
    private Button mButton;
    private TextView mFedTaxView, mMedTaxView, mSSTaxView, mStateTaxView;
    private EditText hourly_rate, hours_worked, gross_pay, num_paychecks;
    private ArrayMap<String, Double> mFlatStateTaxes;
    private ArrayMap<String, double[]> mStateTaxes;
    private ArrayMap<String, Integer> mStandardDeduction;
    double[] mSingleFederalTaxBracket, mJointFederalTaxBracket, mMarriedFederalTaxBracket,
             mTaxBracketRates, mHeadFederalTaxBracket;
    private double mIncomeTax;
    private boolean isFirst = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity().getApplicationContext();
        mView = inflater.inflate(R.layout.paycheck_input_fragment, null);
        createData();
        mHourlyFragment = new HourlyFragment();
        mSalaryFragment = new SalaryFragment();
        mButton = (Button) mView.findViewById(R.id.calculate_button);
        mFedTaxView = (TextView) mView.findViewById(R.id.federal_tax_value);
        mMedTaxView = (TextView) mView.findViewById(R.id.medicare_tax_value);
        mSSTaxView = (TextView) mView.findViewById(R.id.social_security_tax_value);
        mStateTaxView = (TextView) mView.findViewById(R.id.state_income_tax_value);

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
                DecimalFormat df = new DecimalFormat("#.00");
                //States without income tax
                if (mSelectedState.equals("Alaska") |
                        mSelectedState.equals("Florida") |
                        mSelectedState.equals("Nevada") |
                        mSelectedState.equals("South Dakota") |
                        mSelectedState.equals("Texas") |
                        mSelectedState.equals("Washington") |
                        mSelectedState.equals("Wyoming") |
                        mSelectedState.equals("New Hampshire") |
                        mSelectedState.equals("Tennessee")) {
                    mIncomeTax = 0;
                    //mStateTaxView.setText(Double.valueOf(df.format(mIncomeTax)) + "%");
                } else {
                    if(mFlatStateTaxes.get(mSelectedState) != null) {
                        mIncomeTax = mFlatStateTaxes.get(mSelectedState);
                        //mStateTaxView.setText(Double.valueOf(df.format(mIncomeTax)) + "%");
                        calculateStateIncomeTax(true);
                    } else {
                        double[] state_values = mStateTaxes.get(mSelectedState);
                        //mStateTaxView.setText(Double.valueOf(df.format(state_values[3])) + " to " + Double.valueOf(df.format(state_values[4])) + "%");
                        calculateStateIncomeTax(false);
                    }
                }
            }
        });
    }

    public void createData() {
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

        //Tax Bracket Due Data
        mSingleFederalTaxBracket = new double[]{922.5,5156.25,18481.25,46075.25,119401.25,119996.25};
        mJointFederalTaxBracket = new double[]{1845,10312.5,29387.5,51577.5,111324,129996.5};
        mMarriedFederalTaxBracket = new double[]{922.5,5156.25,14693.75,25788.75,55662,64998.25};
        mHeadFederalTaxBracket = new double[]{1315,6872.5,26775.5,49192.5,115737,125362};
        mTaxBracketRates = new double[] {.1, .15, .25, .28, .33, .35, .396};

        //Standard Deduction Data
        mStandardDeduction = new ArrayMap<String, Integer>();
        mStandardDeduction.put("Single", 6300);
        mStandardDeduction.put("Married", 6300);
        mStandardDeduction.put("Joint", 12600);
        mStandardDeduction.put("Head", 9250);

        //Flat Income Tax Rates
        mFlatStateTaxes = new ArrayMap<String, Double>();
        mFlatStateTaxes.put("Colorado", 4.63);
        mFlatStateTaxes.put("Illinois", 3.8);
        mFlatStateTaxes.put("Indiana", 3.3);
        mFlatStateTaxes.put("Massachusetts", 5.2);
        mFlatStateTaxes.put("Michigan", 4.25);
        mFlatStateTaxes.put("North Carolina", 5.8);
        mFlatStateTaxes.put("Pennsylvania", 3.07);
        mFlatStateTaxes.put("Utah", 5.0);

        doStateIncomeTax();
    }

    private void doStateIncomeTax() {
        mStateTaxes = new ArrayMap<String, double[]>();

        /*
        double[0] = F
        double[1] = G
        double[2] = J
        double[3] = B
        double[4] = D

        incomeBracketSteps = (J-G)/(F-1)
        taxRateSteps = (D-B)/(F-1)
        numSteps = (F-1)
         */
        double[] alabama = new double[]     {3, 500, 3001, 2, 5};
        mStateTaxes.put("Alabama", alabama);
        double[] arizona = new double[]     {5, 10000, 150001, 2.59, 4.54};
        mStateTaxes.put("Arizona", arizona);
        double[] arkansas = new double[]    {6, 4299, 35100, 0.9, 6.9};
        mStateTaxes.put("Arkansas", arkansas);
        double[] california = new double[]  {9, 7749, 519687, 1.0, 12.3};
        mStateTaxes.put("California", california);
        double[] connecticut = new double[] {6, 10000, 250000, 3, 6.7};
        mStateTaxes.put("Connecticut", connecticut);
        double[] delaware = new double[]    {7, 2000, 60001, 0, 6.6};
        mStateTaxes.put("Delaware", delaware);
        double[] georgia = new double[]     {6, 750, 7001, 1.0, 6.0};
        mStateTaxes.put("Georgia", georgia);
        double[] hawaii = new double[]      {12, 2400, 200001, 1.4, 11.0};
        mStateTaxes.put("Hawaii", hawaii);
        double[] idaho = new double[]       {7, 1429, 10718, 1.6, 7.4};
        mStateTaxes.put("Idaho", idaho);
        double[] iowa = new double[]        {9, 1539, 69255, 0.36,	8.98};
        mStateTaxes.put("Iowa", iowa);
        double[] kansas = new double[]      {2, 15000, 30000, 2.7, 4.6};
        mStateTaxes.put("Kansas", kansas);
        double[] kentucky = new double[]    {6, 3000, 75001, 2.0, 6.0};
        mStateTaxes.put("Kentucky", kentucky);
        double[] louisiana = new double[]   {3, 12500, 50001, 2.0, 6.0};
        mStateTaxes.put("Louisiana", louisiana);
        double[] maine = new double[]       {3, 5200, 20900, 0.0, 7.95};
        mStateTaxes.put("Maine", maine);
        double[] maryland = new double[]    {8, 1000, 250000, 2.0, 5.75};
        mStateTaxes.put("Maryland", maryland);
        double[] minnesota = new double[]   {4, 25070, 154951, 5.35, 9.85};
        mStateTaxes.put("Minnesota", minnesota);
        double[] mississippi = new double[] {3, 5000, 10001, 3.0, 5.0};
        mStateTaxes.put("Mississippi", mississippi);
        double[] missouri = new double[]    {10, 1000, 9001, 1.5, 6.0};
        mStateTaxes.put("Missouri", missouri);
        double[] montana = new double[]     {7, 2800, 17100, 1.0, 6.9};
        mStateTaxes.put("Montana", montana);
        double[] nebraska = new double[]    {4, 3050, 39460, 2.46, 6.84};
        mStateTaxes.put("Nebraska", nebraska);
        double[] newjersey = new double[]   {6, 20000, 500000, 1.4, 8.97};
        mStateTaxes.put("New Jersey", newjersey);
        double[] newmexico = new double[]   {4, 5500, 16001, 1.7, 4.9};
        mStateTaxes.put("New Mexico", newmexico);
        double[] newyork = new double[]     {8, 8200, 1029250, 4.0, 8.82};
        mStateTaxes.put("New York", newyork);
        double[] northdakota = new double[] {5, 37450, 411500, 1.22, 3.22};
        mStateTaxes.put("North Dakota", northdakota);
        double[] ohio = new double[]        {9, 5200, 208000, 0.528, 5.333};
        mStateTaxes.put("Ohio", ohio);
        double[] oklahoma = new double[]    {7, 1000, 8701, 0.5, 5.25};
        mStateTaxes.put("Oklahoma", oklahoma);
        double[] oregon = new double[]      {4, 3350, 125000, 5.0, 9.9};
        mStateTaxes.put("Oregon", oregon);
        double[] rhodeisland = new double[] {3, 60550, 137650, 3.75, 5.99};
        mStateTaxes.put("Rhode Island", rhodeisland);
        double[] southcarolina = new double[] {6, 2910, 14550, 0.0, 7.0};
        mStateTaxes.put("South Carolina", southcarolina);
        double[] vermont = new double[]     {5, 37450, 411500, 3.55, 8.95};
        mStateTaxes.put("Vermont", vermont);
        double[] virginia = new double[]    {4, 3000, 17001, 2.0, 5.75};
        mStateTaxes.put("Virginia", virginia);
        double[] westvirginia = new double[] {5, 10000, 60000, 3.0, 6.5};
        mStateTaxes.put("West Virginia", westvirginia);
        double[] wisconsin = new double[]   {4, 11090, 244270, 4.4, 7.65};
        mStateTaxes.put("Wisconsin", wisconsin);
        double[] dc = new double[]          {4, 10000, 350000, 4.0, 8.95};
        mStateTaxes.put("Washington D.C.", dc);
    }

    private void calculateStateIncomeTax(boolean isFlat) {
        double rate = 0, hours = 0, gross = 0, paychecks = 0;
        boolean input = false;
        boolean hrorsal;

        if(mFragTag.equals("Hourly"))hrorsal = true;
        else hrorsal = false;

        try {
            if (mFragTag.equals(getResources().getString(R.string.hourly))) {
                hourly_rate = (EditText) mView.findViewById(R.id.edit_hourly_rate);
                hours_worked = (EditText) mView.findViewById(R.id.edit_hours_worked);
                hourly_rate.setText("10");
                hours_worked.setText("80");
                rate = Double.parseDouble(hourly_rate.getText().toString());
                hours = Double.parseDouble(hours_worked.getText().toString());
            } else {
                gross_pay = (EditText) mView.findViewById(R.id.edit_gross_pay);
                num_paychecks = (EditText) mView.findViewById(R.id.edit_num_paychecks);
                gross = Double.parseDouble(gross_pay.getText().toString());
                paychecks = Double.parseDouble(num_paychecks.getText().toString());
            }
            input = true;
        } catch (NumberFormatException e) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("All your values need to be numbers!").setTitle("Improper Input");
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        //gross - standard deduction - personal exemption
        /*
        double[0] = F
        double[1] = G
        double[2] = J
        double[3] = B
        double[4] = D

        incomeBracketSteps = (J-G)/(F-2)
        taxRateSteps = (D-B)/(F-2)
        numSteps = (F-2)
         */
        double total = 0;
        double state_tax = 0;

        if(isFlat && input) {
            state_tax = mFlatStateTaxes.get(mSelectedState) / 100;
            total = (rate * hours * state_tax);
            System.out.println(rate + " " + hours + " " + state_tax + " " + total);
        } else if (input) {
            double[] state_info = mStateTaxes.get(mSelectedState);
            //The dollar range of each tax bracket (estimated)
            double incomeBracketSteps = (state_info[2] - state_info[1])/(state_info[0] - 2);
            //The tax rate increase per tax bracket (estimated)
            double taxRateSteps = ((state_info[4] - state_info[3]) / (state_info[0] - 1)) / 100;
            //The max number of steps to get through to each bracket for calculation purposes
            double maxSteps = state_info[0] - 2;
            //The lowest starting tax bracket dollar cap
            double stateBracket = state_info[1];
            //The lowest starting tax bracket tax rate
            double stateTax = state_info[3] / 100;
            //Dummy variable to hold income
            double d = 0;
            //For calculation purposes, works with maxSteps
            int count = 0;
            //Initialize gross amount of money this person can make
            if (hrorsal) d = (rate * hours * R.integer.pay_periods);
            else d = gross;

            //Subtract lowest tax bracket cap amount
            d = d - state_info[1];

            //Get a proper count to ensure how many calculations need to be done
            while( d > 0 && maxSteps > 0) {
                maxSteps--;
                count++;
                d = d - incomeBracketSteps;
            }

            //Reaffirm dummy variable for gross income
            if (hrorsal) d = (rate * hours * R.integer.pay_periods);
            else d = gross;

            //Calculate State Income Tax
            if (count == 0) {
                total = (d * stateTax) / R.integer.pay_periods;
            } else {
                d = d - stateBracket;
                total = stateBracket * stateTax;
                while (count > 0) {
                    count--;
                    stateTax = stateTax + taxRateSteps;
                    d = d - incomeBracketSteps;
                    total = total + (incomeBracketSteps * stateTax);
                }
                stateTax = stateTax + taxRateSteps;
                total = total + (d * stateTax);
                total = total / R.integer.pay_periods;
            }

        }

        //Display State Income Tax, formatted to 2 decimals
        //FORMATTING NEEDS TO GET FIXED HERE
        //FORMATTING NEEDS TO GET FIXED HERE
        //FORMATTING NEEDS TO GET FIXED HERE
        //FORMATTING NEEDS TO GET FIXED HERE
        DecimalFormat df = new DecimalFormat("#.00");
        mStateTaxView.setText("$" + Double.valueOf(df.format(total)));
    }
}
