package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import i.am.shiro.amai.R;

import static android.support.v4.view.ViewCompat.requireViewById;

public final class MainFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        BottomNavigationView navigation = requireViewById(view, R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_browse);
        }
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // detach previous fragment if any
        Fragment detachingFragment = fragmentManager.getPrimaryNavigationFragment();
        if (detachingFragment != null) {
            fragmentTransaction.detach(detachingFragment);
        }

        int itemId = item.getItemId();
        String fragmentTag = getFragmentTag(itemId);
        Fragment attachingFragment = fragmentManager.findFragmentByTag(fragmentTag);
        if (attachingFragment == null) {
            // add fragment if it has not been added
            attachingFragment = getFragment(itemId);
            fragmentTransaction.add(R.id.fragmentContainer, attachingFragment, fragmentTag);
        } else {
            // attach fragment if it has already been added
            fragmentTransaction.attach(attachingFragment);
        }
        fragmentTransaction.setPrimaryNavigationFragment(attachingFragment);

        fragmentTransaction.setReorderingAllowed(true);
        fragmentTransaction.commitNowAllowingStateLoss();
        return true;
    }

    private String getFragmentTag(@IdRes int itemId) {
        switch (itemId) {
            case R.id.navigation_downloads:
                return DownloadsFragment.class.getSimpleName();
            case R.id.navigation_browse:
                return BrowseFragment.class.getSimpleName();
            case R.id.navigation_queue:
                return QueueFragment.class.getSimpleName();
            default:
                throw new IllegalArgumentException("No corresponding fragment for given itemId");
        }
    }

    private Fragment getFragment(@IdRes int itemId) {
        switch (itemId) {
            case R.id.navigation_downloads:
                return new DownloadsFragment();
            case R.id.navigation_browse:
                return new BrowseFragment();
            case R.id.navigation_queue:
                return new QueueFragment();
            default:
                throw new IllegalArgumentException("No corresponding fragment for given itemId");
        }
    }
}
