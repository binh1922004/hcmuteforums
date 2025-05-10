package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.FollowerAdapter;
import com.example.hcmuteforums.adapter.FollowingAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.example.hcmuteforums.viewmodel.FollowViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class FollowFragment extends Fragment {

    private RecyclerView recyclerView;
    private FollowerAdapter followerAdapter;
    private FollowingAdapter followingAdapter;
    private TabLayout tabLayout;
    private ImageButton backButton;
    private List<FollowerResponse> followerResponses;
    private List<FollowingResponse> followingResponses;
    private String username;
    private FollowViewModel followViewModel;
    private int defaultTab = 1; // Mặc định là tab "Đang theo dõi" (index 1)

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lấy username từ Bundle hoặc nguồn khác
        if (getArguments() != null) {
            username = getArguments().getString("username", "default_username");
            defaultTab = getArguments().getInt("defaultTab", 1); // Lấy defaultTab, mặc định là 1
        } else {
            username = "default_username";
        }
        Log.d("Username", username);
        Log.d("DefaultTab", String.valueOf(defaultTab));
        // Khởi tạo ViewModel
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        // Khởi tạo nút quay lại
        backButton = view.findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        // Khởi tạo RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo Adapter
        followerAdapter = new FollowerAdapter(getContext(), this::handleFollowClick, this::handleMoreClick);
        followingAdapter = new FollowingAdapter(getContext(), this::handleMoreClick);

        // Khởi tạo TabLayout
        tabLayout = view.findViewById(R.id.tabLayout);
        initializeTabs();

        setupObservers();
        fetchFollowData();

        return view;
    }

    private void fetchFollowData() {
        followViewModel.getFollower(username, 0);
        followViewModel.getFollowing(username, 0);
    }

    private void setupObservers() {
        // Quan sát danh sách Followers
        followViewModel.getGetListFollower().observe(getViewLifecycleOwner(), new Observer<PageResponse<FollowerResponse>>() {
            @Override
            public void onChanged(PageResponse<FollowerResponse> pageResponse) {
                if (pageResponse != null) {
                    followerResponses = pageResponse.getContent();
                    Log.d("FollowFragment", "Follower data loaded: " + (followerResponses != null ? followerResponses.size() : 0));
                    updateFollowerData();
                }
            }
        });

        followViewModel.getGetFollowerError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi lấy danh sách người theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        followViewModel.getGetListFollowing().observe(getViewLifecycleOwner(), new Observer<PageResponse<FollowingResponse>>() {
            @Override
            public void onChanged(PageResponse<FollowingResponse> pageResponse) {
                if (pageResponse != null) {
                    followingResponses = pageResponse.getContent();
                    Log.d("FollowFragment", "Following data loaded: " + (followingResponses != null ? followingResponses.size() : 0));
                    updateFollowingData();
                }
            }
        });

        followViewModel.getGetFollowingError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi lấy danh sách đang theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        followViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String message = event.getContent();
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        followViewModel.getToFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean success = event.getContent();
                if (success != null && success) {
                    Toast.makeText(getContext(), "Theo dõi thành công", Toast.LENGTH_SHORT).show();
                    fetchFollowData();
                }
            }
        });

        followViewModel.getToFollowError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        followViewModel.getUnFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean success = event.getContent();
                if (success != null && success) {
                    Toast.makeText(getContext(), "Bỏ theo dõi thành công", Toast.LENGTH_SHORT).show();
                    fetchFollowData();
                }
            }
        });

        followViewModel.getUnFollowError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi bỏ theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeTabs() {
        tabLayout.removeAllTabs();
        tabLayout.addTab(tabLayout.newTab().setText("0 Người theo dõi"));
        tabLayout.addTab(tabLayout.newTab().setText("0 Đang theo dõi"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        // Chọn tab và adapter dựa trên defaultTab ngay khi khởi tạo
        if (defaultTab == 0) {
            recyclerView.setAdapter(followerAdapter);
            followerAdapter.updateData(new ArrayList<>());
        } else {
            recyclerView.setAdapter(followingAdapter);
            followingAdapter.updateData(new ArrayList<>());
        }
        if (defaultTab == 0 || defaultTab == 1) {
            tabLayout.getTabAt(defaultTab).select();
        }

        // Thêm listener để xử lý khi tab được chọn
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                updateTabContent(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                updateTabContent(tab.getPosition());
            }
        });
    }

    private void updateTabContent(int position) {
        if (position == 0) {
            recyclerView.setAdapter(followerAdapter);
            if (followerResponses != null && !followerResponses.isEmpty()) {
                followerAdapter.updateData(followerResponses);
            } else {
                followerAdapter.updateData(new ArrayList<>());
                Toast.makeText(getContext(), "Tài khoản này không có người theo dõi.", Toast.LENGTH_SHORT).show();
            }
        } else if (position == 1) {
            recyclerView.setAdapter(followingAdapter);
            if (followingResponses != null && !followingResponses.isEmpty()) {
                followingAdapter.updateData(followingResponses);
            } else {
                followingAdapter.updateData(new ArrayList<>());
                Toast.makeText(getContext(), "Tài khoản này không theo dõi ai.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateFollowerData() {
        int followerCount = (followerResponses != null) ? followerResponses.size() : 0;
        TabLayout.Tab followerTab = tabLayout.getTabAt(0);
        if (followerTab != null) {
            followerTab.setText(followerCount + " Người theo dõi");
        }
        // Cập nhật nếu tab hiện tại là "Người theo dõi"
        if (tabLayout.getSelectedTabPosition() == 0) {
            updateTabContent(0);
        }
    }

    private void updateFollowingData() {
        int followingCount = (followingResponses != null) ? followingResponses.size() : 0;
        TabLayout.Tab followingTab = tabLayout.getTabAt(1);
        if (followingTab != null) {
            followingTab.setText(followingCount + " Đang theo dõi");
        }
        // Cập nhật nếu tab hiện tại là "Đang theo dõi"
        if (tabLayout.getSelectedTabPosition() == 1) {
            updateTabContent(1);
        }
    }

    private void handleFollowClick(String followId, String targetUsername, int position) {
        if (followerResponses == null || position >= followerResponses.size()) return;
        followViewModel.toFollow(targetUsername);
    }

    private void handleMoreClick(String followId, int position) {
        Toast.makeText(getContext(), "More clicked for followId: " + followId, Toast.LENGTH_SHORT).show();
    }
}