package i.am.shiro.amai.fragment;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import i.am.shiro.amai.R;

public final class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        super(R.layout.fragment_welcome);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        view.findViewById(R.id.skipButton)
                .setOnClickListener(v -> onSkip());

        view.findViewById(R.id.nextButton)
                .setOnClickListener(v -> onNext());
    }

    private void onSkip() {
        requireFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new MainFragment())
                .commit();
    }

    private void onNext() {
        requireFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new StorageSetupFragment())
                .addToBackStack(null)
                .commit();
    }
}
