package com.example.hcmuteforums.ui.activity.user;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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
import com.example.hcmuteforums.ui.activity.topic.TopicDetailActivity;
import com.example.hcmuteforums.ui.fragment.CategoryFragment;
import com.example.hcmuteforums.ui.fragment.ChatFragment;
import com.example.hcmuteforums.ui.fragment.HomeFragment;
import com.example.hcmuteforums.ui.fragment.MenuFragment;
import com.example.hcmuteforums.ui.fragment.MyProfileUserFragment;
import com.example.hcmuteforums.ui.fragment.NotificationFragment;
import com.example.hcmuteforums.ui.fragment.ProfileFragment;
import com.example.hcmuteforums.utils.WebSocketManager;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserMainActivity extends AppCompatActivity implements OnNotificationListener {
    BottomNavigationView bottomNavigationView;
    Fragment profileFragment = new ProfileFragment();
    NotificationFragment notificationFragment = new NotificationFragment();
    HomeFragment homeFragment = new HomeFragment();
    ChatFragment chatFragment = new ChatFragment();
    CategoryFragment categoryFragment = new CategoryFragment();
    MyProfileUserFragment profileUserFragment = new MyProfileUserFragment();
    MenuFragment menuFragment = new MenuFragment();
    int currentNoti = 0;

    //Websocket
    WebSocketManager webSocketManager;

    //banner
    private LinearLayout notificationBanner;
    private TextView notificationMessage;
    private ImageView notificationClose;
    private Handler bannerHandler;
    private static final long BANNER_DURATION = 5000;
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
        //banner for notification
        bannerConfig();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void bannerConfig() {
        // Khởi tạo banner
        notificationBanner = findViewById(R.id.banner_notification);
        notificationMessage = findViewById(R.id.notification_message);
        notificationClose = findViewById(R.id.notification_close);
        bannerHandler = new Handler(Looper.getMainLooper());

        // Sự kiện đóng banner
        notificationClose.setOnClickListener(v -> hideNotificationBanner());

    }

    private void showNotificationBanner(NotificationDTO notification) {
        // Cập nhật nội dung banner
        notificationMessage.setText(notification.getContent());
        String sender = notification.getSenderName();
        String message = sender + notification.getContent();
        SpannableString spannable = new SpannableString(message);

        // In đậm tên người gửi
        spannable.setSpan(new StyleSpan(Typeface.BOLD), 0, sender.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        notificationMessage.setText(spannable);
        notificationBanner.setVisibility(View.VISIBLE);

        // Sự kiện nhấn vào banner (chuyển đến NotificationFragment hoặc activity chi tiết)
        notificationBanner.setOnClickListener(v -> {
            Intent topicIntent = new Intent(this, TopicDetailActivity.class);
            topicIntent.putExtra("topicId", notification.getTopicId());
            if (notification.getType().equals("REPLY"))
                topicIntent.putExtra("replyId", notification.getActionId());
            startActivity(topicIntent);
        });

        // Tự động ẩn sau BANNER_DURATION
        bannerHandler.removeCallbacksAndMessages(null);
        bannerHandler.postDelayed(this::hideNotificationBanner, BANNER_DURATION);
    }

    private void hideNotificationBanner() {
        notificationBanner.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Đăng ký lắng nghe thông báo
        webSocketManager.addNotificationListener(this);
        currentNoti = webSocketManager.getUnreadNotificationCount(); // Cập nhật currentNoti
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
               if (isLoggedIn()){
                   currentNoti = 0;
                   webSocketManager.markAllNotificationsAsRead();
                   updateNotificationBadge(false);
                   setCurrentFragment(notificationFragment);
               }
               else{
                   setCurrentFragment(profileFragment);
               }
           }
           if(itemId == R.id.itemMenu){
               if(isLoggedIn()){
                   setCurrentFragment(menuFragment);
               }else{
                   setCurrentFragment(profileFragment);
               }

            }
           if (itemId == R.id.itemChatbot){
               setCurrentFragment(chatFragment);
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
         webSocketManager.disconnect();
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
        BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.itemNotification);

        if (hasUnread) {
            // Chỉ tăng currentNoti nếu có thông báo mới thực sự
            badge.setVisible(true);
            badge.setNumber(currentNoti); // Hiển thị số lượng hiện tại
        } else {
            // Ẩn badge và đặt lại currentNoti
            currentNoti = 0;
            badge.setVisible(false);
            bottomNavigationView.removeBadge(R.id.itemNotification);
        }
    }

    @Override
    public void onNewNotification(NotificationDTO notificationData) {
        runOnUiThread(() -> {
            showNotificationBanner(notificationData);
        });
    }

    @Override
    public void onNotificationStatusChanged(boolean hasUnread) {
        runOnUiThread(() -> {
            currentNoti = webSocketManager.getUnreadNotificationCount(); // Cập nhật currentNoti
            updateNotificationBadge(hasUnread);
        });
    }
}