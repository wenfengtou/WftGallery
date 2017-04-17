package com.wenfengtou.wftgallery;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import com.wenfengtou.wftgallery.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout mDrawerLayout;
    NavigationView mNavigationView;

    private int mCurrentUIIndex = 0;
    private static final int INDEX_HOME = 0;
    private static final int INDEX_COLLECTION = 1;
    private static final int INDEX_Blog = 2;
    private static final int INDEX_TODAY = 3;
    private static final int REQUEST_CODE = 111;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
    Fragment mHomeFragment;
    Fragment mCurrentFragment;

    private void updateUI() {
        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerLayout.closeDrawer(GravityCompat.START);
        }
        switch (mCurrentUIIndex) {
            case INDEX_HOME:
                if (mHomeFragment == null) {
                    mHomeFragment = new HomeFragment();
                }
                switchFragment(mHomeFragment);
                break;

        }
    }

    private void switchFragment(Fragment fragment) {
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
