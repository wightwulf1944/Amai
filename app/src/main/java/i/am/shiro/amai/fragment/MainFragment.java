package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import i.am.shiro.amai.R;
import i.am.shiro.amai.widget.OnBackPressListener;

import static androidx.core.view.ViewCompat.requireViewById;

public final class MainFragment extends Fragment {

    private long lastBackPressTime;

    public MainFragment() {
        super(R.layout.fragment_main);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.requestFocus();
        view.setOnKeyListener(new OnBackPressListener(this::onBackPress));

        BottomNavigationView navigation = requireViewById(view, R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_nhentai);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        FragmentManager childFragmentManager = getChildFragmentManager();
        if (childFragmentManager.findFragmentById(R.id.fragmentContainer) == null) {
            NhentaiFragment initialFragment = new NhentaiFragment();
            String fragmentTag = NhentaiFragment.class.getName();
            childFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, initialFragment, fragmentTag)
                    .setPrimaryNavigationFragment(initialFragment)
                    .commit();
        }
    }

    private void onBackPress() {
        long now = SystemClock.elapsedRealtime();
        if (now > lastBackPressTime + 1000) {
            lastBackPressTime = now;
            Snackbar.make(requireView(), R.string.confirm_exit, Snackbar.LENGTH_SHORT)
                .setAnchorView(R.id.navigation)
                .show();
        } else {
            requireActivity().finish();
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
        fragmentTransaction.commit();
        return true;
    }

    private String getFragmentTag(@IdRes int itemId) {
        switch (itemId) {
            case R.id.navigation_saved:
                return SavedFragment.class.getName();
            case R.id.navigation_nhentai:
                return NhentaiFragment.class.getName();
            case R.id.navigation_downloads:
                return DownloadsFragment.class.getName();
            default:
                throw new IllegalArgumentException("No corresponding fragment for given itemId");
        }
    }

    private Fragment getFragment(@IdRes int itemId) {
        switch (itemId) {
            case R.id.navigation_saved:
                return new SavedFragment();
            case R.id.navigation_nhentai:
                return new NhentaiFragment();
            case R.id.navigation_downloads:
                return new DownloadsFragment();
            default:
                throw new IllegalArgumentException("No corresponding fragment for given itemId");
        }
    }
}
