package com.example.hcmuteforums.ui.fragment;

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
    UserResponse currentUserResponse;   //Xac nhan co ton du lieu nguoi dung hay chua

    String avatarProfile, coverProfile, bioProfile;
    ImageView coverPhoto;
    CircleImageView imgAvatar;
    ImageButton btn_follow, btn_back;
    String username = "trieudz";
    String currentUsername = "javire";
    TextView tv_username, tv_email, tv_countFollower, tv_countFollowing, tv_fullname;
    LinearLayout followingLayout, followerLayout;
    private boolean isFollowing = false;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AnyProfileUserFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
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
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_any_user, container, false);
        anhxa(view);
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);
        Bundle bundle = getArguments();
        username = bundle.getString("username");
        currentUsername = bundle.getString("currentUsername");
        getInfo(tv_username, tv_email, tv_fullname);
        getPersonProfile(view);
        EventBackHome();
        setupFollowClickEvents();
        setupFollowButton();

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
    private void getInfo(TextView tv_username, TextView tv_email, TextView tv_fullname){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);   //Map viewmodel
        userViewModel.getInfoPerson(username);
        userViewModel.getPersonInfo().observe(getViewLifecycleOwner(), new Observer<Event<UserResponse>>() {
            @Override
            public void onChanged(Event<UserResponse> eUserResponse) {
                UserResponse userResponse = eUserResponse.getContent();
                if (userResponse == null)
                    return;
                currentUserResponse = userResponse;
                tv_username.setText(userResponse.getUsername());
                tv_email.setText(userResponse.getEmail());
                tv_fullname.setText(userResponse.getFullName());
                // Lấy số người theo dõi và đang theo dõi khi có username
                if (userResponse.getUsername() != null) {
                    fetchFollowCounts(userResponse.getUsername());
                }
            }
        });
        userViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String message = event.getContent(); // Lấy nội dung sự kiện chưa được xử lý
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        userViewModel.getPersonInfoError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void getPersonProfile(View view){
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
                Boolean errorOccurred = booleanEvent.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });
        profileViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String message = stringEvent.getContent(); // Lấy nội dung sự kiện chưa được xử lý
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private void loadImage(View viewProfile){
        CircleImageView avatar = (CircleImageView)viewProfile.findViewById(R.id.imgAvatar);
        Glide.with(requireContext()).load("http://10.0.2.2:8080/ute/" +avatarProfile)
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.user_2)
                .into(avatar);
        ImageView cover = (ImageView) viewProfile.findViewById(R.id.coverPhoto);
        Glide.with(requireContext()).load("http://10.0.2.2:8080/ute/"+coverProfile)
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.user_2)
                .centerCrop()
                .into(cover);

    }
    private void fetchFollowCounts(String username) {
        followViewModel = new ViewModelProvider(this).get(FollowViewModel.class);

        // Lấy danh sách Followers
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

        // Lấy danh sách Following
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
    private void EventBackHome(){
        btn_back.setOnClickListener(v -> {
            // Tạo instance của MenuFragment
            HomeFragment homeFragment = new HomeFragment();
            // Thay thế fragment hiện tại bằng MenuFragment
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, homeFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }
    private void setupFollowClickEvents() {
        // Khi nhấn vào "Đang theo dõi"
        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUserResponse == null) {
                    Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }
                FollowFragment followingFragment = new FollowFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUserResponse.getUsername());
                bundle.putInt("defaultTab", 1); // 1: Tab "Đang theo dõi"
                followingFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, followingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        // Khi nhấn vào "Người theo dõi"
        followerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUserResponse == null) {
                    Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
                    return;
                }
                FollowFragment followingFragment = new FollowFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", currentUserResponse.getUsername());
                bundle.putInt("defaultTab", 0); // 0: Tab "Người theo dõi"
                followingFragment.setArguments(bundle);

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, followingFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
    private void setupFollowButton() {
        //if (btn_follow == null || currentUserResponse == null || username == null) return;

        // Kiểm tra trạng thái follow ban đầu
        checkFollowStatus();

        btn_follow.setOnClickListener(v -> {
            if (isFollowing) {
                // Thực hiện unfollow
                followViewModel.unFollow(username);
                followViewModel.getUnFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = false;
                            btn_follow.setImageResource(R.drawable.baseline_add_24); // Quay lại dấu cộng
                            fetchFollowCounts(username); // Cập nhật lại số lượng follower
                            Toast.makeText(getContext(), "Đã hủy theo dõi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Hủy theo dõi thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                // Thực hiện follow
                followViewModel.toFollow(username);
                followViewModel.getToFollowSuccess().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                    @Override
                    public void onChanged(Event<Boolean> event) {
                        Boolean success = event.getContent();
                        if (success != null && success) {
                            isFollowing = true;
                            btn_follow.setImageResource(R.drawable.baseline_check_24); // Chuyển sang dấu tích
                            fetchFollowCounts(username); // Cập nhật lại số lượng follower
                            Toast.makeText(getContext(), "Đã theo dõi", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Theo dõi thất bại", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    private void checkFollowStatus() {
        if (currentUsername != null && username != null && !currentUsername.equals(username)) {
            followViewModel.checkFollowStatus(currentUsername, username);
            followViewModel.getFollowStatus().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
                @Override
                public void onChanged(Event<Boolean> event) {
                    Boolean status = event.getContent();
                    if (status != null) {
                        isFollowing = status;
                        btn_follow.setImageResource(isFollowing ? R.drawable.baseline_check_24 : R.drawable.baseline_add_24);
                    }
                }
            });
        } else {
            btn_follow.setVisibility(View.GONE); // Ẩn nút nếu xem profile của chính mình
        }
    }
}