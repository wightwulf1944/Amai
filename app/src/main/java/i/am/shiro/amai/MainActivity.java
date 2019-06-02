package i.am.shiro.amai;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.fragment.IntroFragment;
import i.am.shiro.amai.fragment.MainFragment;

public final class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, getInitialFragment())
                    .commit();
        }
    }

    private Fragment getInitialFragment() {
        if (Preferences.isFirstRun()) {
            return new IntroFragment();
        } else {
            return new MainFragment();
        }
    }
}
