package com.foxminded.android.task2.ui;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.ui.operations.OperationsFragment;

public class MyPagerAdapter extends FragmentPagerAdapter {
    private static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String COLLECTIONS_FRAGMENT = "CollectionsFragment";

    public MyPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return OperationsFragment.newInstance(COLLECTIONS_FRAGMENT);
            case 1:
                return OperationsFragment.newInstance(MAPS_FRAGMENT);
            default:
                return OperationsFragment.newInstance(MAPS_FRAGMENT);
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title;
        if (position == 0) {
            title = "Collections";
        } else {
            title = "Maps";
        }
        return title;
    }
}
