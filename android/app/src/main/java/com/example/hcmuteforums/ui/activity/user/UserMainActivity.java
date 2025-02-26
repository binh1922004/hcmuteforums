package com.example.hcmuteforums.ui.activity.user;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.ui.fragment.HomeFragment;
import com.example.hcmuteforums.ui.fragment.NotificationFragment;
import com.example.hcmuteforums.ui.fragment.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment profileFragment = new ProfileFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    HomeFragment homeFragment = new HomeFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_home);

        //mapping
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //set up first fragment
        setCurrentFragment(homeFragment);
        //set up for items
        bottomNavigationViewSetup();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void setCurrentFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }

    private void bottomNavigationViewSetup(){
        bottomNavigationView.setSelectedItemId(R.id.itemHome);
        bottomNavigationView.setOnItemSelectedListener(item -> {
           int itemId = item.getItemId();
           if (itemId == R.id.itemHome){
                setCurrentFragment(homeFragment);
           }
           if (itemId == R.id.itemNotification){
               setCurrentFragment(notificationFragment);
           }
           if (itemId == R.id.itemProfile){
               setCurrentFragment(profileFragment);
           }
           return true;
        });
    }
}