package i.am.shiro.amai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.fragment.BrowseFragment;
import i.am.shiro.amai.fragment.DownloadsFragment;
import i.am.shiro.amai.fragment.QueueFragment;
import io.realm.Realm;

public class MainActivity extends AppCompatActivity {

    // Needed to maintain at least one instance of Realm
    private final Realm realm = Realm.getDefaultInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        if (Preferences.isFirstRun()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);

        if (savedInstanceState == null) {
            navigation.setSelectedItemId(R.id.navigation_browse);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        realm.close();
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        FragmentManager fragmentManager = getSupportFragmentManager();
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
        }
        throw new IllegalArgumentException("No corresponding fragment for given itemId");
    }

    private Fragment getFragment(@IdRes int itemId) {
        switch (itemId) {
            case R.id.navigation_downloads:
                return new DownloadsFragment();
            case R.id.navigation_browse:
                return new BrowseFragment();
            case R.id.navigation_queue:
                return new QueueFragment();
        }
        throw new IllegalArgumentException("No corresponding fragment for given itemId");
    }
}
