package com.example.android.main.ui;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.android.R;
import com.example.android.global.RetrofitClient;
import com.example.android.global.ui.FriendFragment;
import com.example.android.global.ui.HomeFragment;
import com.example.android.global.ui.MatchFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity"; // 로그 태그 추가

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RetrofitClient retrofit = RetrofitClient.getInstance(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        loadFragment(new HomeFragment());

        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            // if-else 문 사용
            if (item.getItemId() == R.id.nav_home) {
                Log.d(TAG, "Home selected");
                selectedFragment = new HomeFragment();
            } else if (item.getItemId() == R.id.nav_friends) {
                Log.d(TAG, "Friends selected");
                selectedFragment = new FriendFragment();
            } else if (item.getItemId() == R.id.nav_match) {
                Log.d(TAG, "Match selected");
                selectedFragment = new MatchFragment();
            }

            return loadFragment(selectedFragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null) // 뒤로 가기 기능 추가
                    .commit();
            return true;
        }
        return false;
    }
}
