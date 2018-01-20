package i.am.shiro.amai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.fragment.DownloadsFragment;
import i.am.shiro.amai.fragment.QueueFragment;
import i.am.shiro.amai.fragment.SourceFragment;

public class MainActivity extends AppCompatActivity {

    private final DownloadsFragment downloadsFragment = new DownloadsFragment();

    private final SourceFragment sourceFragment = new SourceFragment();

    private final QueueFragment queueFragment = new QueueFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Preferences.isFirstRun()) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
            finish();
        }

        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(this::onNavigationItemSelected);
        navigation.setSelectedItemId(R.id.navigation_source);
    }

    private boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_downloads:
                switchFragment(downloadsFragment);
                return true;
            case R.id.navigation_source:
                switchFragment(sourceFragment);
                return true;
            case R.id.navigation_queue:
                switchFragment(queueFragment);
                return true;
        }
        return false;
    }

    private void switchFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragmentContainer, fragment)
                .commit();
    }
}
