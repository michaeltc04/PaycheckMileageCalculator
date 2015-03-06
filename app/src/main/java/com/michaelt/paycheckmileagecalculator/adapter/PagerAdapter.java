package com.michaelt.paycheckmileagecalculator.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.michaelt.paycheckmileagecalculator.fragment.PaycheckInputFragment;
import com.michaelt.paycheckmileagecalculator.fragment.SalaryFragment;

public class PagerAdapter extends FragmentPagerAdapter {

    Context mContext;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //((Activity) mContext).findViewById(R.id.pager_tab_strip).setBackgroundColor(R.color.green);
                return new PaycheckInputFragment();
            //case 1:
                //((Activity) mContext).findViewById(R.id.pager_tab_strip).setBackgroundColor(R.color.red);
                //return new SalaryFragment();
            default:
                break;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Paycheck Estimator";
            case 1:
                return "MPG Calculator";
            default:
                break;
        }
        return null;
    }
}
