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
import com.example.hcmuteforums.model.modelAdapter.User;
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Lấy username từ Bundle hoặc nguồn khác
        if (getArguments() != null) {
            username = getArguments().getString("username", "default_username");
        } else {
            username = "default_username";
        }
        Log.d("Username", username);
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

        // Mặc định sử dụng followingAdapter
        recyclerView.setAdapter(followingAdapter);

        // Khởi tạo TabLayout
        tabLayout = view.findViewById(R.id.tabLayout);

        fetchFollowData();
        setupObservers();

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
                    updateTabsAndData();
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
                    updateTabsAndData();
                }
            }
        });

        // Quan sát lỗi khi lấy Following
        followViewModel.getGetFollowingError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi lấy danh sách đang theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quan sát thông báo lỗi từ API
        followViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String message = event.getContent();
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quan sát sự kiện follow thành công
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

        // Quan sát sự kiện follow thất bại
        followViewModel.getToFollowError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi theo dõi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quan sát sự kiện unfollow thành công
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

        // Quan sát sự kiện unfollow thất bại
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

    private void updateTabsAndData() {
//        if (followerResponses != null && followingResponses != null) {
//            // Cập nhật tab với số lượng
//            tabLayout.removeAllTabs();
//            tabLayout.addTab(tabLayout.newTab().setText(followerResponses.size() + " Người theo dõi"));
//            tabLayout.addTab(tabLayout.newTab().setText(followingResponses.size() + " Đang theo dõi"));
//            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
//
//            // Xử lý sự kiện khi chọn tab
//            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                @Override
//                public void onTabSelected(TabLayout.Tab tab) {
//                    if (tab.getPosition() == 0) {
//                        // Hiển thị danh sách Người theo dõi
//                        recyclerView.setAdapter(followerAdapter);
//                        followerAdapter.updateData(followerResponses);
//                    } else if (tab.getPosition() == 1) {
//                        // Hiển thị danh sách Đang theo dõi
//                        recyclerView.setAdapter(followingAdapter);
//                        followingAdapter.updateData(followingResponses);
//                    }
//                }
//
//                @Override
//                public void onTabUnselected(TabLayout.Tab tab) {}
//
//                @Override
//                public void onTabReselected(TabLayout.Tab tab) {}
//            });
//
//            // Hiển thị danh sách mặc định (Đang theo dõi)
//            recyclerView.setAdapter(followingAdapter);
//            followingAdapter.updateData(followingResponses);
//        }
        if(followerResponses!=null){
            Log.d("Follower ID", followerResponses.get(0).getFollowId());
            Log.d("Avatar Id", followerResponses.get(0).getUserGeneral().getAvt());
            // Cập nhật tab với số lượng
            tabLayout.removeAllTabs();
            tabLayout.addTab(tabLayout.newTab().setText(followerResponses.size() + " Người theo dõi"));
            //tabLayout.addTab(tabLayout.newTab().setText(followingResponses.size() + " Đang theo dõi"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            // Xử lý sự kiện khi chọn tab
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    if (tab.getPosition() == 0) {
                        // Hiển thị danh sách Người theo dõi
                        recyclerView.setAdapter(followerAdapter);
                        followerAdapter.updateData(followerResponses);
                    }
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {}

                @Override
                public void onTabReselected(TabLayout.Tab tab) {}
            });

        }
    }

    private void handleFollowClick(String followId, String targetUsername, int position) {
        if (followerResponses == null || position >= followerResponses.size()) return;

        // Giả định nút "Theo dõi" chuyển thành "Bỏ theo dõi" sau khi nhấn
        // Hiện tại tôi sẽ gọi toFollow() hoặc unFollow() trực tiếp
        // Bạn có thể thêm logic để kiểm tra trạng thái (ví dụ: gọi API kiểm tra isFollowing)
        followViewModel.toFollow(targetUsername); // Hoặc unFollow nếu cần logic toggle
    }

    private void handleMoreClick(String followId, int position) {
        // Xử lý khi nhấn nút More (ví dụ: mở PopupMenu)
        Toast.makeText(getContext(), "More clicked for followId: " + followId, Toast.LENGTH_SHORT).show();
    }
}