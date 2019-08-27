package com.joe.preview.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.joe.preview.R;
import com.joe.preview.databinding.MainActivityBinding;
import com.joe.preview.ui.custom.menu.MenuDrawerToggle;
import com.joe.preview.utils.NavigationUtil;
import com.joe.preview.utils.PreviewUtil;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;

public class MainActivity extends BaseActivity implements HasSupportFragmentInjector,
        BottomNavigationView.OnNavigationItemSelectedListener {

    MainActivityBinding binding;
    MenuDrawerToggle menuDrawerToggle;

    @Inject
    DispatchingAndroidInjector<Fragment> dispatchingAndroidInjector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidInjection.inject(this);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        initializeView();
    }

    private void initializeView() {
        setSupportActionBar(binding.includedLayout.toolbar);
        getSupportActionBar().setHomeButtonEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        binding.bottomNav.setOnNavigationItemSelectedListener(this);

        menuDrawerToggle = new MenuDrawerToggle(this, binding.drawerLayout, binding.includedLayout.toolbar,
                binding.leftDrawer, R.string.drawer_open, R.string.drawer_close, PreviewUtil.getMenuList(getApplicationContext())) {
            @Override
            public void onSwitch(int selectedPosition, int topPosition) {
                binding.bottomNav.getSelectedItemId();
            }
        };

        menuDrawerToggle.syncState();
        binding.drawerLayout.addDrawerListener(menuDrawerToggle);
    }

    public void displayToolbar() {
        binding.includedLayout.toolbar.setVisibility(View.VISIBLE);
    }

    public void hideToolbar() {
        binding.includedLayout.toolbar.setVisibility(View.INVISIBLE);
    }

    public void updateBackground(String url) {
        binding.overlayLayout.updateCurrentBackground(url);
    }

    public void clearBackground() {
        binding.overlayLayout.clearImage();
    }

    public void handleSearchIconClick(View view) {
        switch (binding.bottomNav.getSelectedItemId()) {
            case R.id.movie_menu:
                NavigationUtil.redirectToMovieSearch(this);
                break;
            case R.id.series_menu:
                NavigationUtil.redirectToSeriesSearch(this);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        menuDrawerToggle.onDestroy();
    }

    @Override
    public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingAndroidInjector;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.movie_menu:
                NavigationUtil.replaceFragment(this, R.id.moviesListFragment, menuDrawerToggle.getSelectedPosition());
                break;
            case R.id.series_menu:
                NavigationUtil.replaceFragment(this, R.id.seriesListFragment, menuDrawerToggle.getSelectedPosition());
                break;
            default:
                return false;
        }
        return true;
    }

}
