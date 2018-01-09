package i.am.shiro.amai.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Shiro on 1/6/2018.
 */

public class IntroAdapter extends FragmentPagerAdapter {

    private Fragment[] fragments;

    public IntroAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragments(Fragment... fragments) {
        this.fragments = fragments;
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }
}
