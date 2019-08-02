package i.am.shiro.amai;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import i.am.shiro.amai.fragment.MainFragment;
import i.am.shiro.amai.fragment.WelcomeFragment;

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
            return new WelcomeFragment();
        } else {
            return new MainFragment();
        }
    }
}
