package i.am.shiro.amai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.IntroAdapter;

/**
 * Created by Shiro on 1/6/2018.
 */

public class IntroActivity extends AppCompatActivity {

    private final Fragment[] fragments = {
            new Page1Fragment(),
            new Page2Fragment()
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
            Preferences.setFirstRunDone(this);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public static class Page1Fragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro_page_1, container, false);
        }
    }

    public static class Page2Fragment extends Fragment {

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_intro_page_2, container, false);
        }
    }
}
