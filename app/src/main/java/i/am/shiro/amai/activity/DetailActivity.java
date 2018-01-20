package i.am.shiro.amai.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import i.am.shiro.amai.R;

/**
 * Created by Shiro on 1/20/2018.
 */

public class DetailActivity extends AppCompatActivity {

    private static final String BOOK_ID = "bookId";

    public static Intent makeIntent(Context context, int bookId) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(BOOK_ID, bookId);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_detail);
    }
}
