package com.example.hcmuteforums.ui.fragment;

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
import com.example.hcmuteforums.adapter.PersonFollowerAdapter;
import com.example.hcmuteforums.adapter.PersonFollowingAdapter;
import com.example.hcmuteforums.event.Event;
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

public class PersonFollowFragment extends Fragment {

    private RecyclerView recyclerView;
    private PersonFollowerAdapter followerAdapter;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        tabLayout = view.findViewById(R.id.tabLayout);
        initializeTabs();

        setupObservers();
        fetchFollowData();

        return view;
    }

    private void setupBackButton() {
        backButton.setOnClickListener(v -> {
            // Tạo instance của MenuFragment
            AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();
            // Thay thế fragment hiện tại bằng MenuFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, anyProfileUserFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void fetchFollowData() {
        followViewModel.getFollower(username, 0);
        followViewModel.getFollowing(username, 0);
    }

    private void setupObservers() {
        followViewModel.getGetListFollower().observe(getViewLifecycleOwner(), new Observer<PageResponse<FollowerResponse>>() {
            @Override
            public void onChanged(PageResponse<FollowerResponse> pageResponse) {
                if (pageResponse != null) {
                    followerResponses = pageResponse.getContent();
                    Log.d("PersonFollowFragment", "Follower data loaded: " + (followerResponses != null ? followerResponses.size() : 0));
                    updateFollowerData();
                    // Kiểm tra trạng thái theo dõi cho từng follower
                    if (!currentUsername.equals("default_current_user")) {
                        for (FollowerResponse follower : followerResponses) {
                            String targetUsername = follower.getUserGeneral().getUsername();
                            Log.d("PersonFollowFragment", "Checking follow status for: " + targetUsername);
                            if (targetUsername.equals(currentUsername)) {
                                followButtonVisibilityMap.put(targetUsername, false); // Ẩn nút nếu là chính mình
                                Log.d("PersonFollowFragment", "Hiding follow button for self: " + targetUsername);
                            } else {
                                lastCheckedUsername = targetUsername;
                                followViewModel.checkFollowStatus(currentUsername, targetUsername);
                            }
                        }
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
                    Log.d("PersonFollowFragment", "Following data loaded: " + (followingResponses != null ? followingResponses.size() : 0));
                    followingUsernames.clear();
                    if (followingResponses != null) {
                        for (FollowingResponse following : followingResponses) {
                            followingUsernames.add(following.getUserGeneral().getUsername());
                        }
                    }
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
                    if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, false);
                        followStatusMap.put(targetUsername, true);
                        followerAdapter.updateButtonVisibility(targetUsername, false);
                    }
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
                    if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, true);
                        followStatusMap.put(targetUsername, false);
                        followerAdapter.updateButtonVisibility(targetUsername, true);
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
                    if (followerAdapter != null && targetUsername != null) {
                        followButtonVisibilityMap.put(targetUsername, true);
                        followStatusMap.put(targetUsername, false);
                        followerAdapter.updateButtonVisibility(targetUsername, true);
                    }
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

        followerAdapter = new PersonFollowerAdapter(
                getContext(),
                this::handleFollowClick,
                this::handleFollowerMoreClick
        );
        followingAdapter = new PersonFollowingAdapter(
                followingResponses,
                getContext(),
                this::handleFollowingMoreClick
        );

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
                followerAdapter.updateData(followerResponses, followingUsernames);
            } else {
                followerAdapter.updateData(new ArrayList<>(), followingUsernames);
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
    }

    private void handleFollowClick(String followId, String targetUsername, int position, boolean isFollowing) {
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

    private String targetUsername;
}