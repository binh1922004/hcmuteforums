package com.example.hcmuteforums.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.provider.OpenableColumns;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

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

    UserViewModel userViewModel;
    ProfileViewModel profileViewModel;
    AuthenticationViewModel authenticationViewModel;

    UserResponse currentUserResponse;   //Xac nhan co ton du lieu nguoi dung hay chua
    String avatarProfile, coverProfile, bioProfile;

    ImageView coverPhoto;
    CircleImageView imgAvatar;
    ImageButton uploadAvatar, uploadCover, btn_logout;
    TextView tv_username, tv_email;
    Button btn_edit;

    private ActivityResultLauncher<String> permissionLauncher;
    private ActivityResultLauncher<Intent> avatarPickerLauncher;
    private ActivityResultLauncher<Intent> coverPickerLauncher;
    private boolean isPickingAvatar = true;



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
        //Ánh xạ
        anhxa(view);
        SharedPreferences preferences = requireActivity().getSharedPreferences("User", Context.MODE_PRIVATE);
        String token = preferences.getString("jwtLocal", "Không có");
        Log.d("JWT ERROR", token);
        //Nut logout
        btn_logout.setOnClickListener(v-> {
           logoutEvent();
        });
        //Lay thong tin
        getInfo(tv_username, tv_email);

        //Load Image
        getProfile(view);

        //UploadImage
        initLaunchers();
        EventUploadAvatar(uploadAvatar);
        EventUploadCover(uploadCover);
        //Goi Form EditProfile
        OpenEditProfile(btn_edit);

        return view;
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
    void anhxa(View view) {
        tv_username = view.findViewById(R.id.tvName);
        tv_email = view.findViewById(R.id.tvUsername);
        uploadAvatar = view.findViewById(R.id.uploadAvatar);
        uploadCover = view.findViewById(R.id.coverCameraButton);
        btn_edit = view.findViewById(R.id.btnEdit);
        coverPhoto = view.findViewById(R.id.coverPhoto);
        imgAvatar = view.findViewById(R.id.imgAvatar);
        btn_logout = view.findViewById(R.id.btnSetting);  //logout
    }


    private void OpenEditProfile(Button btn_edit){
        btn_edit.setOnClickListener(view -> {
            //showBottomDialog();
            showBottomDiaglogForgotPassword();
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

    //Xu li up anh len
    public String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) cursor.close();
            }
        }

        if (result == null) {
            result = uri.getLastPathSegment();
        }

        return result;
    }

    public File getFileFromUri(Context context, Uri uri) {
        File file = null;
        try {
            String fileName = getFileName(context, uri);
            File tempFile = new File(context.getCacheDir(), fileName);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            OutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            outputStream.close();
            inputStream.close();
            file = tempFile;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


    private void initLaunchers() {
        // Permission launcher
        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                launchImagePicker();
            } else {
                Toast.makeText(requireContext(), "Cần cấp quyền để chọn ảnh!", Toast.LENGTH_SHORT).show();
            }
        });

        // Avatar picker
        avatarPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                File file = getFileFromUri(getActivity(), uri);
                imgAvatar.setImageURI(uri);
                uploadAvatarToServer(file);
                Glide.with(requireActivity()).load(uri).into(imgAvatar);
            }
        });

        // Cover picker
        coverPickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                Uri uri = result.getData().getData();
                File file = getFileFromUri(getActivity(), uri);

                uploadCoverToServer(file);
                Glide.with(requireActivity()).load(uri).into(coverPhoto);
            }
        });
    }

    private void checkAndRequestPermission(boolean forAvatar) {
        isPickingAvatar = forAvatar;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
        } else {
            permissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }
    private void EventUploadAvatar(ImageButton uploadAvatar){
        uploadAvatar.setOnClickListener(view -> {
            checkAndRequestPermission(true);

        });
    }
    private void EventUploadCover(ImageButton uploadCover){
        uploadCover.setOnClickListener(view -> {
            checkAndRequestPermission(false);
        });
    }


    private void uploadAvatarToServer(File AvatarImageFile) {
        profileViewModel.uploadAvatarImage(AvatarImageFile);
    }
    private void uploadCoverToServer(File CoverImageFile){
         profileViewModel.uploadCoverImage(CoverImageFile);
    }
    private void launchImagePicker() {
        ImagePicker.Builder builder = ImagePicker.with(requireActivity())
                .compress(1024)
                .maxResultSize(1080, 1080);

        // Cắt ảnh theo kiểu tương ứng
        if (isPickingAvatar) {
            builder.cropSquare();
        } else {
            builder.crop();
        }

        // Tạo intent và launch đúng launcher
        builder.createIntent(intent -> {
            ActivityResultLauncher<Intent> launcher = isPickingAvatar ? avatarPickerLauncher : coverPickerLauncher;
            launcher.launch(intent);
            return null;
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


}