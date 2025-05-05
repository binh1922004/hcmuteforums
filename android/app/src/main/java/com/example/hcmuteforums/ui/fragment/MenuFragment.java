package com.example.hcmuteforums.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.MenuAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.model.modelAdapter.MenuItemModel;
import com.example.hcmuteforums.ui.activity.user.UserMainActivity;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;
import com.example.hcmuteforums.viewmodel.ProfileViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MenuFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MenuFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public MenuFragment() {
        // Required empty public constructor
    }

    public static MenuFragment newInstance(String param1, String param2) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    private RecyclerView recyclerView;
    private MenuAdapter menuAdapter;
    private List<MenuItemModel> menuList;
    UserResponse currentUserResponse;
    UserViewModel userViewModel;
    AuthenticationViewModel authenticationViewModel;
    ProfileViewModel profileViewModel;
    String avatarUrl, fullname;

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
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        getProfile(view);
        recyclerView = view.findViewById(R.id.recyclerViewMenu);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getInfo();

        // Tạo danh sách menu
        menuList = new ArrayList<>();
        menuList.add(new MenuItemModel(R.drawable.ic_edit, "Chỉnh sửa thông tin"));
        menuList.add(new MenuItemModel(R.drawable.baseline_lock_24, "Đổi mật khẩu"));
        menuList.add(new MenuItemModel(R.drawable.ic_logout, "Đăng xuất"));

        // Adapter
        menuAdapter = new MenuAdapter(getContext(), menuList, new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                switch (position) {
                    case 0:
                        // Mở màn chỉnh sửa thông tin
                        showBottomDialog();
                        break;
                    case 1:
                        // Mở màn đổi mật khẩu
                        showBottomDiaglogForgotPassword();
                        break;
                    case 2:
                        logoutEvent();
                        Toast.makeText(getContext(), "Đăng xuất", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onHeaderClick() {
                FragmentTransaction transaction = requireActivity()
                        .getSupportFragmentManager()
                        .beginTransaction();
                transaction.replace(R.id.flFragment, new ProfileUserFragment());
                transaction.addToBackStack(null); // Để người dùng có thể quay lại
                transaction.commit();
            }

        });

        recyclerView.setAdapter(menuAdapter);

        return view;
    }
    private void getInfo(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);   //Map viewmodel
        userViewModel.getInfo();
        userViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<Event<UserResponse>>() {
            @Override
            public void onChanged(Event<UserResponse> eUserResponse) {
                UserResponse userResponse = eUserResponse.getContent();
                if (userResponse == null)
                    return;
                currentUserResponse = userResponse;
                fullname = userResponse.getFullName();
                menuAdapter.setFullname(fullname);
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
    private void showBottomDiaglogForgotPassword(){
        if(currentUserResponse!=null){
            ForgotPasswordBottomSheetFragment bottomSheetFragment =
                    ForgotPasswordBottomSheetFragment.newInstance(currentUserResponse);;
            bottomSheetFragment.show(getParentFragmentManager(), bottomSheetFragment.getTag());
        }else{
            Toast.makeText(getContext(), "Chưa có dữ liệu người dùng", Toast.LENGTH_SHORT).show();
        }

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
    private void logoutEvent()
    {
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
    }

    private void getProfile(View view){
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile();
        profileViewModel.getProfileInfo().observe(getViewLifecycleOwner(), new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                avatarUrl = profileResponse.getAvatarUrl();
                menuAdapter.setAvatarProfile(avatarUrl);
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


}