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
import com.example.hcmuteforums.adapter.PersonFollowingAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnSwitchFragmentProfile;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.FollowerResponse;
import com.example.hcmuteforums.model.dto.response.FollowingResponse;
import com.example.hcmuteforums.viewmodel.FollowViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class PersonFollowFragment extends Fragment implements OnSwitchFragmentProfile {

    private RecyclerView recyclerView;
    private FollowerAdapter followerAdapter;
    private PersonFollowingAdapter followingAdapter;
    private TabLayout tabLayout;
    private ImageButton backButton;
    private List<FollowerResponse> followerResponses;
    private List<FollowingResponse> followingResponses;
    private Set<String> followingUsernames;
    private String username;
    private FollowViewModel followViewModel;
    private TextView tv_username;
    private int defaultTab = 1;
    private Map<String, Boolean> followButtonVisibilityMap;
    private String currentUsername;
    private Map<String, Boolean> followStatusMap;
    private String lastCheckedUsername;
    private String targetUsername;
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
            currentUsername = getArguments().getString("currentUsername", "default_current_user");
        } else {
            username = "default_username";
            currentUsername = "default_current_user";
        }
        Log.d("PersonFollowFragment", "Viewing: " + username);
        Log.d("PersonFollowFragment", "DefaultTab: " + defaultTab);
        Log.d("PersonFollowFragment", "Logged in as: " + currentUsername);
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);
        followerResponses = new ArrayList<>();
        followingResponses = new ArrayList<>();
        followingUsernames = new HashSet<>();
        followButtonVisibilityMap = new HashMap<>();
        followStatusMap = new HashMap<>();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_follow, container, false);

        backButton = view.findViewById(R.id.backButton);
        tv_username = view.findViewById(R.id.tvUsername);
        tv_username.setText(username);

        setupBackButton();

        recyclerView = view.findViewById(R.id.recyclerView);


        tabLayout = view.findViewById(R.id.tabLayout);
        initializeTabs();
        showMoreFollower();
        showMoreFollowing();
        //Todo: Phân trang cho từng tab
        recyclerViewConfigFollowing();
        recyclerViewConfig();

        setupObservers();

        return view;
    }




    private void setupBackButton() {
        backButton.setOnClickListener(v -> {
            // Tạo instance mới của AnyProfileUserFragment
            AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();

            // Truyền username và currentUsername qua Bundle
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            bundle.putString("currentUsername", currentUsername);
            anyProfileUserFragment.setArguments(bundle);

            // Thay thế Fragment hiện tại
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, anyProfileUserFragment)
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
                    Log.d("PersonFollowFragment", "Follower data loaded: " + (followerResponses != null ? followerResponses.size() : 0));
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
                    Log.d("PersonFollowFragment", "Following data loaded: " + (followingResponses != null ? followingResponses.size() : 0));
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

        followViewModel.getFollowStatus().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean isFollowing = event.getContent();
                if (isFollowing != null && lastCheckedUsername != null) {
                    followStatusMap.put(lastCheckedUsername, isFollowing);
                    followButtonVisibilityMap.put(lastCheckedUsername, !isFollowing); // Ẩn nếu đã theo dõi
                    Log.d("PersonFollowFragment", "Follow status for " + lastCheckedUsername + ": " + isFollowing + ", Button visible: " + !isFollowing);
                    if (followerAdapter != null) {
                        followerAdapter.updateData(followerResponses, followingUsernames);
                    }
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
                    /*if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, false);

                    }*/
                }
            }
        });

        followViewModel.getToFollowError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean error = event.getContent();
                if (error != null && error) {
                    Toast.makeText(getContext(), "Lỗi khi theo dõi", Toast.LENGTH_SHORT).show();
                    if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, true);
                    }
                }
            }
        });

        followViewModel.getUnFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean success = event.getContent();
                if (success != null && success) {
                    Toast.makeText(getContext(), "Bỏ theo dõi thành công", Toast.LENGTH_SHORT).show();
                    /*if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, true);
                        followStatusMap.put(targetUsername, false);
                    }*/
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
        tabLayout.addTab(tabLayout.newTab().setText("0 Đang theo dõi")); // Đổi thứ tự, tab 0 là "Đang theo dõi"
        tabLayout.addTab(tabLayout.newTab().setText("0 Người theo dõi")); // Tab 1 là "Người theo dõi"
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        followerAdapter = new FollowerAdapter(
                getContext(),
                this::handleFollowClick,
                this::handleFollowerMoreClick,
                this
        );
        followingAdapter = new PersonFollowingAdapter(
                getContext(),
                followingResponses,
                this::handleFollowClick,
                this
        );

        if (defaultTab == 0) {
            recyclerView.setAdapter(followingAdapter);
            followingAdapter.updateData(new ArrayList<>());
        } else {
            recyclerView.setAdapter(followerAdapter);
            followerAdapter.updateData(new ArrayList<>(), followingUsernames);
        }
        if (defaultTab == 0 || defaultTab == 1) {
            tabLayout.getTabAt(defaultTab).select();
        }

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
        followingAdapter = new PersonFollowingAdapter(getContext(),
                followingResponses,
                this::handleFollowClick, this);
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
    /*private void updateFollowerData() {
        int followerCount = (followerResponses != null) ? followerResponses.size() : 0;
        TabLayout.Tab followerTab = tabLayout.getTabAt(0);
        if (followerTab != null) {
            followerTab.setText(followerCount + " Người theo dõi");
        }
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
        if (tabLayout.getSelectedTabPosition() == 1) {
            updateTabContent(1);
        }
    }*/

    private void handleFollowClick(String followId, String targetUsername, int position, boolean isFollowing) {
        if (!isUserLoggedIn()) {
            // Nếu chưa đăng nhập, điều hướng đến ProfileFragment
            Toast.makeText(getContext(), "Bạn cần đăng nhập để theo dõi người dùng này", Toast.LENGTH_LONG).show();
            ProfileFragment profileFragment = ProfileFragment.newInstance("param1", "Bạn cần đăng nhập để theo dõi " + targetUsername);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, profileFragment)
                    .addToBackStack(null)
                    .commit();
            return;
        }
        if (followViewModel != null) {
            this.targetUsername = targetUsername;
            if (isFollowing) {
                followViewModel.unFollow(targetUsername);
            } else {
                followViewModel.toFollow(targetUsername);
            }
        }
    }


    private void handleFollowerMoreClick(String followId, int position) {
        Toast.makeText(getContext(), "More clicked for follower followId: " + followId, Toast.LENGTH_SHORT).show();
    }

    private void handleFollowingMoreClick(String followId, String targetUsername, int position) {
        PopupMenu popupMenu = new PopupMenu(getContext(), recyclerView.getChildAt(position).findViewById(R.id.moreButton));
        popupMenu.getMenu().add(0, 1, 0, "Bỏ theo dõi");

        popupMenu.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == 1) {
                if (followViewModel != null) {
                    this.targetUsername = targetUsername;
                    followViewModel.unFollow(targetUsername);
                }
                return true;
            }
            return false;
        });

        popupMenu.show();
    }
    private boolean isUserLoggedIn() {
        // Kiểm tra trạng thái đăng nhập từ SharedPreferences với key "User"
        SharedPreferences prefs = getContext().getSharedPreferences("User", getContext().MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean("isLoggedIn", false);
        Log.d("PersonFollowFragment", "isLoggedIn: " + isLoggedIn);
        return isLoggedIn;
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