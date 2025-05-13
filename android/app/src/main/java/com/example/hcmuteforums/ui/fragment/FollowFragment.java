package com.example.hcmuteforums.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.TextView;
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
import com.example.hcmuteforums.listeners.OnSwitchFragmentProfile;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.example.hcmuteforums.viewmodel.FollowViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FollowFragment extends Fragment implements OnSwitchFragmentProfile {

    private RecyclerView recyclerView;
    private FollowerAdapter followerAdapter;
    private FollowingAdapter followingAdapter;
    private TabLayout tabLayout;
    private ImageButton backButton;
    private List<FollowerResponse> followerResponses;
    private List<FollowingResponse> followingResponses;
    private Set<String> followingUsernames; // Danh sách username của những người đang follow
    private String username;
    private FollowViewModel followViewModel;
    TextView tv_username;
    private String targetUsername;
    private int defaultTab = 1;
    private boolean isLoading = false;
    private boolean isLastPageFollower = false;
    private boolean isLastPageFollowing = false;
    private int currentPageFollower = 0;
    private int currentPageFollowing = 0;
    private final int pageSize = 5;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username", "default_username");
            defaultTab = getArguments().getInt("defaultTab", 1);
        } else {
            username = "default_username";
        }
        Log.d("Username", username);
        Log.d("DefaultTab", String.valueOf(defaultTab));
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);
        followingUsernames = new HashSet<>();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        backButton = view.findViewById(R.id.backButton);
        tv_username = view.findViewById(R.id.tvUsername);
        tv_username.setText(username);

        EventBackProfile();

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        // Khởi tạo Adapter với các listener riêng
        followerAdapter = new FollowerAdapter(
                getContext(),
                this::handleFollowClick,
                this::handleFollowerMoreClick,
                this
        );
        followingAdapter = new FollowingAdapter(
                getContext(),
                this::handleFollowingMoreClick,
                this
        );

        tabLayout = view.findViewById(R.id.tabLayout);
        initializeTabs();

        setupObservers();
        showMoreFollower();
        showMoreFollowing();
        //Todo: Phân trang cho từng tab
        recyclerViewConfigFollowing();
        recyclerViewConfig();

        return view;
    }

    private void recyclerViewConfig(){
        followerAdapter = new FollowerAdapter(getContext(), this::handleFollowClick,
                this::handleFollowerMoreClick, this);
        RecyclerView.LayoutManager linearlayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.setAdapter(followerAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(!isLastPageFollower || !isLoading){
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        Log.d("Load", "co load");
                        showMoreFollower();
                    }
                }
            }
        });

    }

    private void recyclerViewConfigFollowing(){
        followingAdapter = new FollowingAdapter(getContext(), this::handleFollowingMoreClick, this);
        RecyclerView.LayoutManager linearlayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager(linearlayout);
        recyclerView.setAdapter(followingAdapter);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if(layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if(!isLastPageFollowing || !isLoading){
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        Log.d("Load", "co load");
                        showMoreFollowing();
                    }
                }
            }
        });

    }

    private void showMoreFollower(){
        followViewModel.getFollower(username, currentPageFollower);
        currentPageFollower++;
    }
    private void showMoreFollowing(){
        followViewModel.getFollowing(username, currentPageFollowing);
        currentPageFollowing++;
    }
    private void EventBackProfile(){
        backButton.setOnClickListener(v -> {
            // Tạo instance của MenuFragment
            MyProfileUserFragment myProfileUserFragment = new MyProfileUserFragment();
            // Thay thế fragment hiện tại bằng MenuFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, myProfileUserFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }



    private void setupObservers() {
        followViewModel.getGetListFollower().observe(getViewLifecycleOwner(), new Observer<PageResponse<FollowerResponse>>() {
            @Override
            public void onChanged(PageResponse<FollowerResponse> pageResponse) {
                if (pageResponse != null) {
                    followerResponses = pageResponse.getContent();
                    followerAdapter.addData(followerResponses);
                    isLastPageFollower = pageResponse.isLast();
                    Log.d("FollowFragment", "Follower data loaded: " + (followerResponses != null ? followerResponses.size() : 0));
                    TabLayout.Tab followerTab = tabLayout.getTabAt(0);
                    if (followerTab != null) {
                        followerTab.setText(pageResponse.getTotalElements() + " Người theo dõi");
                    }
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
                    TabLayout.Tab followingTab = tabLayout.getTabAt(1);
                    if (followingTab != null) {
                        followingTab.setText(pageResponse.getTotalElements() + " Đang theo dõi");
                    }
                    Log.d("FollowFragment", "Following data loaded: " + (followingResponses != null ? followingResponses.size() : 0));
                    // Cập nhật danh sách followingUsernames
                    followingAdapter.addData(followingResponses);
                    followingUsernames.clear();
                    if (followingResponses != null) {
                        for (FollowingResponse following : followingResponses) {
                            followingUsernames.add(following.getUserGeneral().getUsername());
                        }
                    }
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
        Log.d("TAB", defaultTab+"");
        if (defaultTab == 0) {
            recyclerView.setAdapter(followerAdapter);
            followerAdapter.updateData(new ArrayList<>(), followingUsernames);
        } else {
            recyclerView.setAdapter(followingAdapter);
            followingAdapter.updateData(new ArrayList<>());
        }
        if (defaultTab == 0 || defaultTab == 1) {
            tabLayout.getTabAt(defaultTab).select();
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("All tab selection", tab.getPosition()+"");
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
            followerAdapter.clearData();
            currentPageFollower = 0;
            isLastPageFollower = false;
            showMoreFollower();
            recyclerView.setAdapter(followerAdapter);
        } else if (position == 1) {
            followingAdapter.clearData();
            currentPageFollowing =0;
            isLastPageFollowing = false;
            showMoreFollowing();
            recyclerView.setAdapter(followingAdapter);
        }
    }

    private void handleFollowClick(String followId, String targetUsername, int position, boolean isFollowing) {
        if (followViewModel != null) {
            this.targetUsername = targetUsername; // Lưu targetUsername để sử dụng trong setupObservers
            if (isFollowing) {
                followViewModel.unFollow(targetUsername); // Bỏ theo dõi
            } else {
                followViewModel.toFollow(targetUsername); // Theo dõi
            }
        }
    }

    // Xử lý "More" cho FollowerAdapter
    private void handleFollowerMoreClick(String followId, int position) {
        Toast.makeText(getContext(), "More clicked for follower followId: " + followId, Toast.LENGTH_SHORT).show();
    }

    // Xử lý "More" cho FollowingAdapter
    private void handleFollowingMoreClick(String followId, String targetUsername, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), recyclerView.getChildAt(position).findViewById(R.id.moreButton));
        popupMenu.getMenu().add(0, 1, 0, "Bỏ theo dõi");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                if (followViewModel != null) {
                    this.targetUsername = targetUsername; // Lưu targetUsername để sử dụng trong setupObservers
                    followViewModel.unFollow(targetUsername);
                }
                return true;
            }
            return false;
        });

        popupMenu.show();
    }


    @Override
    public void onClickAnyProfile(String username) {
        SharedPreferences preferences = getContext().getSharedPreferences("User", MODE_PRIVATE);
        String currentUserName = preferences.getString("username", "guest"); // Giá trị mặc định "guest" nếu chưa đăng nhập
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("currentUsername", currentUserName);
        bundle.putBoolean("isLoggedIn", isLoggedIn); // Truyền trạng thái đăng nhập
        bundle.putString("loginPrompt", isLoggedIn ? null : "Bạn cần đăng nhập để theo dõi người dùng này"); // Thông điệp tùy chỉnh
        anyProfileUserFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.flFragment, anyProfileUserFragment)
                .addToBackStack(null)
                .commit();
    }
}