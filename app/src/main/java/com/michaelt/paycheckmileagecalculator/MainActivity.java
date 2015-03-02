package com.michaelt.paycheckmileagecalculator;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.PagerTabStrip;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.michaelt.paycheckmileagecalculator.fragment.HourlyFragment;
import com.michaelt.paycheckmileagecalculator.fragment.SalaryFragment;


public class MainActivity extends ActionBarActivity {

    //CustomPagerAdapter mCustomPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mCustomPagerAdapter = new CustomPagerAdapter(getFragmentManager(), this);
        //mViewPager = (ViewPager) findViewById(R.id.pager);
        //mViewPager.setAdapter(mCustomPagerAdapter);

        HourlyFragment hourlyFragment = new HourlyFragment();
        SalaryFragment salaryFragment = new SalaryFragment();

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        ft.add(R.id.pay_fragment_container, hourlyFragment);
        ft.commit();
    }
}
