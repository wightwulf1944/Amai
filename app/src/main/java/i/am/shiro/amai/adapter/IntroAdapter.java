package i.am.shiro.amai.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
