package com.michaelt.paycheckmileagecalculator;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import com.michaelt.paycheckmileagecalculator.adapter.PagerAdapter;
import com.michaelt.paycheckmileagecalculator.adapter.PayFragmentCoordinator;


public class MainActivity extends FragmentActivity {

    ViewPager mViewPager;
    SharedPreferences.Editor editor;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
        mViewPager.setAdapter(mPagerAdapter);
    }
}
