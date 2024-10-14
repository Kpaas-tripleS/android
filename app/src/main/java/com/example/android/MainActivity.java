////package com.example.android;
////
////import android.os.Bundle;
////import android.util.Log;  // Log 추가
////import android.view.View;
////import androidx.appcompat.app.AppCompatActivity;
////import androidx.core.graphics.Insets;
////import androidx.core.view.ViewCompat;
////import androidx.core.view.WindowInsetsCompat;
////import androidx.fragment.app.Fragment;
////import com.example.android.global.ui.FriendFragment;
////import com.example.android.global.ui.HomeFragment;
////import com.example.android.global.ui.MatchFragment;
////import com.google.android.material.bottomnavigation.BottomNavigationView;
////
////public class MainActivity extends AppCompatActivity {
////
////    private static final String TAG = "MainActivity";  // 로그 태그 추가
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////
////        setupWindowInsets();
////
////        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
////        loadFragment(new HomeFragment());
////
////        bottomNavigationView.setOnItemSelectedListener(item -> {
////            Fragment selectedFragment = null;
////            int itemId = item.getItemId(); // item ID를 가져옴
////
////            if (itemId == R.id.nav_home) {
////                selectedFragment = new HomeFragment();
////                Log.d(TAG, "HomeFragment 선택됨");
////            } else if (itemId == R.id.nav_friends) {
////                selectedFragment = new FriendFragment();
////                Log.d(TAG, "FriendFragment 선택됨");
////            } else if (itemId == R.id.nav_match) {
////                selectedFragment = new MatchFragment();
////                Log.d(TAG, "MatchFragment 선택됨");
////            }
////            return loadFragment(selectedFragment);
////        });
////    }
////
////    private void setupWindowInsets() {
////        View mainView = findViewById(R.id.main);
////        ViewCompat.setOnApplyWindowInsetsListener(mainView, (v, insets) -> {
////            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
////            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
////            return insets;
////        });
////    }
////
////    private boolean loadFragment(Fragment fragment) {
////        if (fragment != null) {
////            getSupportFragmentManager()
////                    .beginTransaction()
////                    .replace(R.id.fragment_container, fragment)
////                    .commit();
////            return true;
////        }
////        return false;
////    }
////}
//
//package com.example.android;
//
//import android.os.Bundle;
//import androidx.appcompat.app.AppCompatActivity;
//import com.example.android.databinding.ActivityMainBinding;
//import com.example.android.global.ui.FriendFragment;
//import com.example.android.global.ui.HomeFragment;
//import com.example.android.global.ui.MatchFragment;
//
//public class MainActivity extends AppCompatActivity {
//    private ActivityMainBinding binding;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        setBottomNavigationView();
//
//        // 앱 초기 실행 시 홈화면으로 설정
//        if (savedInstanceState == null) {
//            binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit(); // 기본 프래그먼트 로드
//        }
//    }
//
//    private void setBottomNavigationView() {
//        binding.bottomNavigation.setOnItemSelectedListener(item -> {
//            switch (item.getItemId()) {
//                case R.id.nav_home:
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
//                    return true;
//                case R.id.nav_friends:  // 친구 프래그먼트로 이동
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FriendFragment()).commit();
//                    return true;
//                case R.id.nav_match:  // 대결 프래그먼트로 이동
//                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MatchFragment()).commit();
//                    return true;
//                default:
//                    return false;
//            }
//        });
//    }
//}
