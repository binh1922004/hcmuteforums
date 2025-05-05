package com.example.hcmuteforums.ui.activity.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.data.remote.interceptor.LocalAuthInterceptor;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.listeners.OnNotificationListener;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.example.hcmuteforums.ui.fragment.CategoryFragment;
import com.example.hcmuteforums.ui.fragment.HomeFragment;
import com.example.hcmuteforums.ui.fragment.MenuFragment;
import com.example.hcmuteforums.ui.fragment.NotificationFragment;
import com.example.hcmuteforums.ui.fragment.ProfileFragment;
import com.example.hcmuteforums.ui.fragment.ProfileUserFragment;
import com.example.hcmuteforums.utils.WebSocketManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity implements OnNotificationListener {
    BottomNavigationView bottomNavigationView;
    Fragment profileFragment = new ProfileFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    HomeFragment homeFragment = new HomeFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    ProfileUserFragment profileUserFragment = new ProfileUserFragment();
    MenuFragment menuFragment = new MenuFragment();
    int currentNoti = 0;

    //Websocket
    WebSocketManager webSocketManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_user_home);

        //dung trong viec inject JWT
        // Khởi tạo Interceptor với Application Context
        LocalAuthInterceptor.setInstance(this);
        // Cấu hình Retrofit với Interceptor đã khởi tạo
        LocalRetrofit.setInterceptor();

        //mapping
        bottomNavigationView = findViewById(R.id.bottomNavigationView);

        //set up first fragment
        setCurrentFragment(homeFragment);
        //set up for items
        bottomNavigationViewSetup();
        //set up for websocket
        webSocketManager = WebSocketManager.getInstance();
        connectWebSocket();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký lắng nghe thông báo
        webSocketManager.addNotificationListener(this);

        // Cập nhật trạng thái badge ngay khi activity được resume
        updateNotificationBadge(webSocketManager.hasUnreadNotifications());
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Hủy đăng ký lắng nghe khi activity không hiển thị
        webSocketManager.removeNotificationListener(this);
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
               currentNoti = 0;
               updateNotificationBadge(false);
               setCurrentFragment(notificationFragment);
           }
           if(itemId == R.id.itemMenu){
               if(isLoggedIn()){
                   setCurrentFragment(menuFragment);
               }else{
                   setCurrentFragment(profileFragment);
               }

            }
           return true;
        });
    }
    private boolean isLoggedIn() {
        SharedPreferences preferences = getSharedPreferences("User", MODE_PRIVATE);
        return preferences.getBoolean("isLoggedIn", false); // Mặc định là false nếu chưa đăng nhập
    }

    //set up for websocket
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ngắt kết nối WebSocket khi activity bị hủy
        // Tùy theo yêu cầu của ứng dụng, bạn có thể chọn giữ kết nối
        // nếu muốn nhận thông báo ngay cả khi ứng dụng đang chạy nền
        // webSocketManager.disconnect();
    }

    private void connectWebSocket() {
        // Lấy token và userId từ SharedPreferences
        SharedPreferences prefsManager = getSharedPreferences("User", MODE_PRIVATE);
        if (!prefsManager.getBoolean("isLoggedIn", false))
            return;
        String authToken = prefsManager.getString("jwtLocal", null);
        String userId = prefsManager.getString("userId", null);
        Log.d("JWT ID", userId + " " + authToken);
        // Kết nối đến WebSocket server
        if (authToken != null && userId != null) {
            webSocketManager.connect(this, authToken, userId);
        }
    }

    // Phương thức để cập nhật badge thông báo
    private void updateNotificationBadge(boolean hasUnread) {
        if (hasUnread) {
            // Tạo và hiển thị badge
            BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.itemNotification);
            badge.setVisible(true);
            // Không hiển thị số, chỉ hiển thị badge
            currentNoti++;
            badge.setNumber(currentNoti);
        }
        else {
            // Ẩn badge
            BadgeDrawable badge = bottomNavigationView.getBadge(R.id.itemNotification);
            if (badge != null) {
                badge.setVisible(false);
                bottomNavigationView.removeBadge(R.id.itemNotification);
            }
        }
    }

    @Override
    public void onNewNotification(NotificationDTO notificationData) {

    }

    @Override
    public void onNotificationStatusChanged(boolean hasUnread) {
        runOnUiThread(() -> {
            updateNotificationBadge(hasUnread);
        });
    }
}