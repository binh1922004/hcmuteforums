package com.example.hcmuteforums.ui.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.viewmodel.FollowViewModel;
import com.example.hcmuteforums.viewmodel.ProfileViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AnyProfileUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AnyProfileUserFragment extends Fragment {

    UserViewModel userViewModel;
    ProfileViewModel profileViewModel;
    FollowViewModel followViewModel;
    UserResponse currentUserResponse;

    String avatarProfile, coverProfile, bioProfile;
    ImageView coverPhoto;
    CircleImageView imgAvatar;
    ImageButton btn_follow, btn_back;
    String username;
    String currentUsername;
    TextView tv_username, tv_email, tv_countFollower, tv_countFollowing, tv_fullname;
    LinearLayout followingLayout, followerLayout;
    private boolean isLoggedIn = false;
    private String loginPrompt;
    private boolean isFollowing = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public AnyProfileUserFragment() {
        // Required empty public constructor
    }
    public static AnyProfileUserFragment newInstance(String param1, String param2) {
        AnyProfileUserFragment fragment = new AnyProfileUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            username = getArguments().getString("username");
            currentUsername = getArguments().getString("currentUsername", "guest"); // Mặc định "guest" nếu null
            isLoggedIn = getArguments().getBoolean("isLoggedIn", false);
            loginPrompt = getArguments().getString("loginPrompt");
            Log.d("AnyProfileUserFragment", "Received - username: " + username + ", currentUsername: " + currentUsername + ", isLoggedIn: " + isLoggedIn);
        }else{
            Log.e("AnyProfileUserFragment", "Bundle is null");
            username = "default_username";
            currentUsername = "guest";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_any_user, container, false);
        anhxa(view);
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);


        getInfo(tv_username, tv_email, tv_fullname);
        getPersonProfile(view);
        setupBackButton();
        setupFollowClickEvents();
        setupFollowButton();
        //topic fragment config
        topicFragmentConfig(username);
        return view;
    }

    void anhxa(View view) {
        tv_username = view.findViewById(R.id.tvName);
        tv_email = view.findViewById(R.id.tvUsername);
        coverPhoto = view.findViewById(R.id.coverPhoto);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        tv_countFollower = view.findViewById(R.id.tvFollowerCount);
        tv_countFollowing = view.findViewById(R.id.tvFollowingCount);
        followingLayout = view.findViewById(R.id.following);
        followerLayout = view.findViewById(R.id.follower);
        tv_fullname = view.findViewById(R.id.tvHeaderName);
        btn_back = view.findViewById(R.id.btnBack);
        btn_follow = view.findViewById(R.id.btnFollow);
    }

    private void getInfo(TextView tv_username, TextView tv_email, TextView tv_fullname) {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getInfoPerson(username);
        userViewModel.getPersonInfo().observe(getViewLifecycleOwner(), new Observer<Event<UserResponse>>() {
            @Override
            public void onChanged(Event<UserResponse> eUserResponse) {
                UserResponse userResponse = eUserResponse.getContent();
                if (userResponse == null) return;
                currentUserResponse = userResponse;
                tv_username.setText(userResponse.getUsername());
                tv_email.setText(userResponse.getEmail());
                tv_fullname.setText(userResponse.getFullName());
                if (userResponse.getUsername() != null) {
                    fetchFollowCounts(userResponse.getUsername());
                }
            }
        });

        userViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String message = event.getContent();
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        userViewModel.getPersonInfoError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent();
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getPersonProfile(View view) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getPersonProfile(username);
        profileViewModel.getPersonProfileInfo().observe(getViewLifecycleOwner(), new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                avatarProfile = profileResponse.getAvatarUrl();
                coverProfile = profileResponse.getCoverUrl();
                bioProfile = profileResponse.getBio();
                loadImage(view);
            }
        });

        profileViewModel.getPersonProfileInfoError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> booleanEvent) {
                Boolean errorOccurred = booleanEvent.getContent();
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        profileViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String message = stringEvent.getContent();
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadImage(View viewProfile) {
        CircleImageView avatar = viewProfile.findViewById(R.id.imgAvatar);
        Glide.with(requireContext()).load("http://10.0.2.2:8080/ute/" + avatarProfile)
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.user_2)
                .into(avatar);
        ImageView cover = viewProfile.findViewById(R.id.coverPhoto);
        Glide.with(requireContext()).load("http://10.0.2.2:8080/ute/" + coverProfile)
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.user_2)
                .centerCrop()
                .into(cover);
    }

    private void fetchFollowCounts(String username) {
        followViewModel.getFollower(username, 0);
        followViewModel.getGetListFollower().observe(getViewLifecycleOwner(), pageResponse -> {
            if (pageResponse != null) {
                long followerCount = pageResponse.getTotalElements();
                tv_countFollower.setText(String.valueOf(followerCount));
            }
        });

        followViewModel.getGetFollowerError().observe(getViewLifecycleOwner(), event -> {
            Boolean error = event.getContent();
            if (error != null && error) {
                tv_countFollower.setText("0");
                Toast.makeText(getContext(), "Lỗi khi lấy số lượng người theo dõi", Toast.LENGTH_SHORT).show();
            }
        });

        followViewModel.getFollowing(username, 0);
        followViewModel.getGetListFollowing().observe(getViewLifecycleOwner(), pageResponse -> {
            if (pageResponse != null) {
                long followingCount = pageResponse.getTotalElements();
                tv_countFollowing.setText(String.valueOf(followingCount));
            }
        });

        followViewModel.getGetFollowingError().observe(getViewLifecycleOwner(), event -> {
            Boolean error = event.getContent();
            if (error != null && error) {
                tv_countFollowing.setText("0");
                Toast.makeText(getContext(), "Lỗi khi lấy số lượng đang theo dõi", Toast.LENGTH_SHORT).show();
            }
        });

        followViewModel.getMessageError().observe(getViewLifecycleOwner(), event -> {
            String message = event.getContent();
            if (message != null) {
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBackButton() {
        btn_back.setOnClickListener(v -> {
            HomeFragment homeFragment = new HomeFragment();
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupFollowClickEvents() {
        followingLayout.setOnClickListener(v -> {
            if (currentUserResponse == null) {
                Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonFollowFragment followingFragment = new PersonFollowFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", currentUserResponse.getUsername());
            bundle.putString("currentUsername", currentUsername);
            bundle.putInt("defaultTab", 1); // 1: Tab "Đang theo dõi"
            followingFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, followingFragment)
                    .addToBackStack(null)
                    .commit();
        });

        followerLayout.setOnClickListener(v -> {
            if (currentUserResponse == null) {
                Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                return;
            }
            PersonFollowFragment followerFragment = new PersonFollowFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", currentUserResponse.getUsername());
            bundle.putString("currentUsername", currentUsername);
            bundle.putInt("defaultTab", 0); // 0: Tab "Người theo dõi"
            followerFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, followerFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void setupFollowButton() {
        /*if (btn_follow == null || currentUsername == null || username == null) {
            Log.e("AnyProfileUserFragment", "btn_follow or username is null");
            return;
        }

        checkFollowStatus();

        btn_follow.setOnClickListener(v -> {
            if (!isUserLoggedIn()) {
                // Nếu chưa đăng nhập, điều hướng đến ProfileFragment
                ProfileFragment profileFragment = ProfileFragment.newInstance("param1", "param2");
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, profileFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            }
            if (isFollowing) {
                followViewModel.unFollow(username);
                btn_follow.setImageResource(R.drawable.baseline_add_24); // Cập nhật UI ngay lập tức
                followViewModel.getUnFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = false;
                            fetchFollowCounts(username);
                            Toast.makeText(getContext(), "Đã hủy theo dõi", Toast.LENGTH_SHORT).show();
                            Log.d("AnyProfileUserFragment", "Unfollow success for " + username);
                            checkFollowStatus(); // Đồng bộ trạng thái với server
                        } else {
                            btn_follow.setImageResource(R.drawable.baseline_check_24); // Rollback nếu thất bại
                            Log.e("AnyProfileUserFragment", "Unfollow failed for " + username);
                        }
                    }
                });
            } else {
                followViewModel.toFollow(username);
                btn_follow.setImageResource(R.drawable.baseline_check_24); // Cập nhật UI ngay lập tức
                followViewModel.getToFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = true;
                            fetchFollowCounts(username);
                            Toast.makeText(getContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show();
                            Log.d("AnyProfileUserFragment", "Follow success for " + username);
                            checkFollowStatus(); // Đồng bộ trạng thái với server
                        } else {
                            btn_follow.setImageResource(R.drawable.baseline_add_24); // Rollback nếu thất bại
                            Log.e("AnyProfileUserFragment", "Follow failed for " + username);
                        }
                    }
                });
            }
        });*/
        if (btn_follow == null || username == null) {
            Log.e("AnyProfileUserFragment", "btn_follow or username is null");
            return;
        }

        btn_follow.setVisibility(View.VISIBLE); // Luôn hiển thị nút
        checkFollowStatus();

        btn_follow.setOnClickListener(v -> {
            if (!isUserLoggedIn()) {
                // Hiển thị thông báo trước khi điều hướng
                if (loginPrompt != null) {
                    Toast.makeText(getContext(), loginPrompt, Toast.LENGTH_LONG).show();
                }
                ProfileFragment profileFragment = ProfileFragment.newInstance("param1", loginPrompt);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, profileFragment)
                        .addToBackStack(null)
                        .commit();
                return;
            }

            if (isFollowing) {
                followViewModel.unFollow(username);
                btn_follow.setImageResource(R.drawable.baseline_add_24);
                followViewModel.getUnFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = false;
                            fetchFollowCounts(username);
                            Toast.makeText(getContext(), "Đã hủy theo dõi", Toast.LENGTH_SHORT).show();
                            Log.d("AnyProfileUserFragment", "Unfollow success for " + username);
                            checkFollowStatus();
                        } else {
                            btn_follow.setImageResource(R.drawable.baseline_check_24); // Rollback
                            Log.e("AnyProfileUserFragment", "Unfollow failed for " + username);
                        }
                    }
                });
            } else {
                followViewModel.toFollow(username);
                btn_follow.setImageResource(R.drawable.baseline_check_24);
                followViewModel.getToFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = true;
                            fetchFollowCounts(username);
                            Toast.makeText(getContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show();
                            Log.d("AnyProfileUserFragment", "Follow success for " + username);
                            checkFollowStatus();
                        } else {
                            btn_follow.setImageResource(R.drawable.baseline_add_24); // Rollback
                            Log.e("AnyProfileUserFragment", "Follow failed for " + username);
                        }
                    }
                });
            }
        });
    }

    private void checkFollowStatus() {
        if (currentUsername != null && username != null && !currentUsername.equals(username)) {
            Log.d("CurrenUsername", currentUsername);
            followViewModel.getFollowStatus().removeObservers(getViewLifecycleOwner()); // Xóa observer cũ
            followViewModel.checkFollowStatus(currentUsername, username, isLoggedIn);
            followViewModel.getFollowStatus().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                @Override
                public void onChanged(Event<Boolean> event) {
                    Boolean status = event.getContent();
                    if (status != null) {
                        isFollowing = status;
                        updateFollowButton();
                        Log.d("AnyProfileUserFragment", "Check follow status for " + currentUsername + " and " + username + ": " + isFollowing);
                    } else {
                        Log.e("AnyProfileUserFragment", "Failed to get follow status for " + username);
                        btn_follow.setImageResource(R.drawable.baseline_add_24);
                    }
                }
            });
        } else {
            btn_follow.setVisibility(View.GONE);
        }
    }
    private void updateFollowButton() {
        btn_follow.setImageResource(isFollowing ? R.drawable.baseline_check_24 : R.drawable.baseline_add_24);
    }

    private void topicFragmentConfig(String username) {
        TopicFragment topicFragment;
        topicFragment = TopicFragment.newInstance(username);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, topicFragment)
                .commit();
    }
    private boolean isUserLoggedIn() {
        return isLoggedIn; // Sử dụng giá trị từ Bundle
    }


}