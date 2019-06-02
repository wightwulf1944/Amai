package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import i.am.shiro.amai.Preferences;
import i.am.shiro.amai.R;
import i.am.shiro.amai.adapter.IntroAdapter;

import static android.support.v4.view.ViewCompat.requireViewById;

public final class IntroFragment extends Fragment {

    private final Fragment[] fragments = {
            new AboutIntroFragment(),
            new StorageIntroFragment()
    };

    private ViewPager viewPager;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        IntroAdapter adapter = new IntroAdapter(getChildFragmentManager());
        adapter.setFragments(fragments);

        viewPager = requireViewById(view, R.id.viewPager);
        viewPager.setAdapter(adapter);

        Button button = requireViewById(view, R.id.nextButton);
        button.setOnClickListener(v -> onNext());
    }

    private void onNext() {
        int nextItem = viewPager.getCurrentItem() + 1;
        if (nextItem < fragments.length) {
            viewPager.setCurrentItem(nextItem);
        } else {
            Preferences.setFirstRunDone();
            requireFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new MainFragment())
                    .commit();
        }
    }
}
