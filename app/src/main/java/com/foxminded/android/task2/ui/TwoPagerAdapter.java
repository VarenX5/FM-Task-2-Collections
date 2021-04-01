package com.foxminded.android.task2.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.foxminded.android.task2.R;
import com.foxminded.android.task2.ui.operations.OperationsFragment;

public class TwoPagerAdapter extends FragmentPagerAdapter {
    private final Context mContext;
    private static final String MAPS_FRAGMENT = "MapsFragment";
    private static final String COLLECTIONS_FRAGMENT = "CollectionsFragment";

    public TwoPagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
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
            title = mContext.getString(R.string.collections);
        } else {
            title = mContext.getString(R.string.maps);
        }
        return title;
    }
}
