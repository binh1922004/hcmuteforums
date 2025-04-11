package com.example.hcmuteforums.ui.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.ui.activity.user.UserMainActivity;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;
import com.example.hcmuteforums.viewmodel.ProfileViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileUserFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ProfileUserFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileUserFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileUserFragment newInstance(String param1, String param2) {
        ProfileUserFragment fragment = new ProfileUserFragment();
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
    UserViewModel userViewModel;
    ProfileViewModel profileViewModel;
    AuthenticationViewModel authenticationViewModel;
    ProfileResponse currentProfileResponse;
    String username, email;

    UserResponse currentUserResponse;   //Xac nhan co ton du lieu nguoi dung hay chua

    private void getInfo(TextView tv_username, TextView tv_email){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);   //Map viewmodel
        userViewModel.getInfo();
        userViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                currentUserResponse = userResponse;
                tv_username.setText(userResponse.getFullName());
                tv_email.setText(userResponse.getEmail());
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

        userViewModel.getUserInfoError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    String avatarProfile, coverProfile, bioProfile;
    private void getProfile(View view){
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile();
        profileViewModel.getProfileInfo().observe(getViewLifecycleOwner(), new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                avatarProfile = profileResponse.getAvatarUrl();
                Log.d("DUong dan anh", avatarProfile);
                coverProfile = profileResponse.getCoverUrl();
                bioProfile = profileResponse.getBio();
                loadImage(view);
            }
        });

        profileViewModel.getProfileInfoError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        TextView tv_username = view.findViewById(R.id.tvName);
        TextView tv_email = view.findViewById(R.id.tvUsername);
        Button btn_edit = view.findViewById(R.id.btnEdit);
        ImageButton btn_logout = view.findViewById(R.id.btnSetting);  //logout
        SharedPreferences preferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String token = preferences.getString("jwtLocal", "Không có");
        Log.d("JWT ERROR", token);
        //Nut logout
        btn_logout.setOnClickListener(v-> {
            //xoa du lieu trong viewmodel
            authenticationViewModel = new ViewModelProvider(this).get(AuthenticationViewModel.class);
            authenticationViewModel.logout();
            //Xoá thông tin đăng nhập ở sharepreferences
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
            sharedPreferences.edit().clear().apply();
            //Chuyen ve trang chu
            Intent intent = new Intent(requireActivity(), UserMainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa tất cả activity trước đó
            startActivity(intent);
        });
        getInfo(tv_username, tv_email);
        getProfile(view);

        //Goi Form EditProfile
        OpenEditProfile(btn_edit);

        //Upload anh
        upLoadImage(view);
        return view;
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
    private void OpenEditProfile(Button btn_edit){
        btn_edit.setOnClickListener(view -> {
            showBottomDialog();
        });
    }

    private void showBottomDialog()
    {
        if (currentUserResponse != null) {
            EditUserBottomSheet bottomSheet = EditUserBottomSheet.newInstance(currentUserResponse);
            bottomSheet.show(getParentFragmentManager(), bottomSheet.getTag());
        } else {
            Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
        }
    }

    //Xu li up anh len
    ImageView coverPhoto;
    ImageButton uploadAvatar, uploadCover;
    private void upLoadImage(View view)
    {
        coverPhoto = (ImageView)view.findViewById(R.id.coverPhoto);
        uploadAvatar = (ImageButton) view.findViewById(R.id.uploadAvatar);
        uploadCover = (ImageButton) view.findViewById(R.id.coverCameraButton);

        uploadCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImagePicker.Companion.with(requireActivity())
                        .start();
            }
        });
    }


}