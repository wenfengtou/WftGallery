package com.wenfengtou.wftgallery;

import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;


import com.wenfengtou.wftgallery.fragment.ComicFragment;
import com.wenfengtou.wftgallery.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;
    TabLayout mTabLayout;
    Toolbar mToolbar;

    private int mCurrentUIIndex = 0;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_COMIC = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        updateUI();
    }

    private void initView(){
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        mToolbar =(Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle(R.string.app_name);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mCurrentUIIndex = tab.getPosition();
                updateUI();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    Fragment mHomeFragment;
    Fragment mCurrentFragment;
    Fragment mComicFragment;

    private void updateUI() {
        Log.i("wenfeng","updateUI");
        switch (mCurrentUIIndex) {
            case INDEX_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
                switchFragment(mHomeFragment);
                break;
            case INDEX_COMIC:
                if (mComicFragment == null) {
                    mComicFragment = new ComicFragment();
                }
                switchFragment(mComicFragment);
                break;

        }
    }

    private void switchFragment(Fragment fragment) {
        Log.i("wenfeng","switchFragment");
        FragmentTransaction fragmentTransaction;
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (mCurrentFragment != null) {
            fragmentTransaction.hide(mCurrentFragment);
        }
        if (fragment.isAdded()) {
            fragmentTransaction.show(fragment);
        } else {
            fragmentTransaction.add(R.id.content, fragment);
        }
        fragmentTransaction.commit();
        mCurrentFragment = fragment;
    }
}
