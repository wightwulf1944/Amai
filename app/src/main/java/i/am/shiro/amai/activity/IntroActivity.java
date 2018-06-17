package i.am.shiro.amai.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.IntroAdapter;
import i.am.shiro.amai.fragment.AboutIntroFragment;
import i.am.shiro.amai.fragment.StorageIntroFragment;

/**
 * Created by Shiro on 1/6/2018.
 */

public class IntroActivity extends AppCompatActivity {

    private final Fragment[] fragments = {
            new AboutIntroFragment(),
            new StorageIntroFragment()
    };

    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_intro);

        IntroAdapter adapter = new IntroAdapter(getSupportFragmentManager());
        adapter.setFragments(fragments);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(adapter);

        Button button = findViewById(R.id.nextButton);
        button.setOnClickListener(v -> onNext());
    }

    private void onNext() {
        int nextItem = viewPager.getCurrentItem() + 1;
        if (nextItem < fragments.length) {
            viewPager.setCurrentItem(nextItem);
        } else {
            Preferences.setFirstRunDone();
            finish();
        }
    }
}
