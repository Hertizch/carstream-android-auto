package com.thekirankumar.youtubeauto;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.google.android.apps.auto.sdk.CarActivity;
import com.google.android.apps.auto.sdk.CarUiController;

import java.util.HashSet;

public class MainCarActivity extends CarActivity {
    private static final String FRAGMENT_MAIN = "main";
    private static final String CURRENT_FRAGMENT_KEY = "app_current_fragment";
    private String mCurrentFragmentTag;
    private final FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycleCallbacks
            = new FragmentManager.FragmentLifecycleCallbacks() {
        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            updateStatusBarTitle();
        }
    };
    private HashSet<OnConfigurationChangedListener> onConfigListener = new HashSet<>(0);

    @Override
    public void onWindowFocusChanged(boolean b, boolean b1) {
        super.onWindowFocusChanged(b, b1);
    }

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_car_main);

        CarUiController carUiController = getCarUiController();
        carUiController.getStatusBarController().hideAppHeader();
        carUiController.getStatusBarController().hideConnectivityLevel();

        FragmentManager fragmentManager = getSupportFragmentManager();
        WebViewCarFragment webViewCarFragment = new WebViewCarFragment();

        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, webViewCarFragment, FRAGMENT_MAIN)
                .detach(webViewCarFragment)
                .commitNow();

        String initialFragmentTag = FRAGMENT_MAIN;
        if (bundle != null && bundle.containsKey(CURRENT_FRAGMENT_KEY)) {
            initialFragmentTag = bundle.getString(CURRENT_FRAGMENT_KEY);
        }
        switchToFragment(initialFragmentTag);
        getSupportFragmentManager().registerFragmentLifecycleCallbacks(mFragmentLifecycleCallbacks,
                false);
    }

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {
        bundle.putString(CURRENT_FRAGMENT_KEY, mCurrentFragmentTag);
        super.onSaveInstanceState(bundle);
    }

    @Override
    public void onStart() {
        super.onStart();
        switchToFragment(mCurrentFragmentTag);
    }

    private void switchToFragment(String tag) {
        if (tag.equals(mCurrentFragmentTag)) {
            return;
        }
        FragmentManager manager = getSupportFragmentManager();
        Fragment currentFragment = mCurrentFragmentTag == null ? null : manager.findFragmentByTag(mCurrentFragmentTag);
        Fragment newFragment = manager.findFragmentByTag(tag);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (currentFragment != null) {
            transaction.detach(currentFragment);
        }
        transaction.attach(newFragment);
        transaction.commit();
        mCurrentFragmentTag = tag;
    }

    private void updateStatusBarTitle() {
        CarFragment fragment = (CarFragment) getSupportFragmentManager().findFragmentByTag(mCurrentFragmentTag);
        getCarUiController().getStatusBarController().setTitle(fragment.getTitle());
    }

    public Window getWindow() {
        return super.c();
    }

    @Override
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        for (OnConfigurationChangedListener next : onConfigListener) {
            next.onConfigChanged();
        }
    }

    public void addConfigurationChangedListener(OnConfigurationChangedListener listener) {
        this.onConfigListener.add(listener);
    }

    public void removeConfigurationChangedListener(OnConfigurationChangedListener listener) {
        this.onConfigListener.remove(listener);
    }

    public interface OnConfigurationChangedListener {
        void onConfigChanged();
    }
}
