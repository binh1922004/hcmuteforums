package com.example.hcmuteforums.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.ui.activity.user.UserMainActivity;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;

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
    AuthenticationViewModel authenticationViewModel;
    private void getInfo(){
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);   //Map viewmodel
        userViewModel.getInfo();
        userViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                Toast.makeText(getContext(), userResponse.getUsername(), Toast.LENGTH_SHORT).show();
            }
        });
        userViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), s, Toast.LENGTH_SHORT).show();
            }
        });
        userViewModel.getUserInfoError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_user, container, false);
        TextView tv_username = view.findViewById(R.id.tv_Username);
        TextView tv_email = view.findViewById(R.id.tv_Email);

        SharedPreferences preferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);

        String username = preferences.getString("username" , "Chưa có tên người dùng");
        String email = preferences.getString("email", "Chưa có Email");

        String token = preferences.getString("jwtLocal", "Không có");
        Log.d("JWT ERROR", token);
        tv_email.setText(email);
        tv_username.setText(username);
        //Nut logout
        ConstraintLayout logOutButton = view.findViewById(R.id.logOut);
        logOutButton.setOnClickListener(v-> {
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



        getInfo();

        return  view;
    }

}