package com.example.hcmuteforums.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.EditUserAdapter;
import com.example.hcmuteforums.decoration.DividerItemDecoration;
import com.example.hcmuteforums.decoration.RecyclerViewBorderDecoration;
import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.ui.activity.user.EditNameActivity;
import com.example.hcmuteforums.ui.activity.user.VerifyOTPActivity;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EditUserBottomSheet extends BottomSheetDialogFragment {
    private ViewGroup containerView;

   UserUpdateRequest userUpdateRequest;
    public static final String ARG_USER = "user";
    public static EditUserBottomSheet newInstance(UserResponse user)
    {
        EditUserBottomSheet fragment = new EditUserBottomSheet();
        Bundle args = new Bundle();
        args.putSerializable(ARG_USER, user);
        fragment.setArguments(args);
        return fragment;
    }


    public EditUserBottomSheet() {
        // Required empty public constructor
    }
    @Override
    public void onStart() {
        super.onStart();

        View view = getView();
        if (view != null) {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(parent);

                // Đặt chiều cao tối đa là 80% màn hình
                parent.getLayoutParams().height = (int) (getResources().getDisplayMetrics().heightPixels * 0.8);
                parent.requestLayout();

                // Đặt BottomSheetDialog ở trạng thái mở rộng (EXPANDED)
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bottom_sheet_edit_user_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupRecyclerView(view);
    }


    private void switchToMainLayout() {
        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        if (parent == null) return;

        parent.removeAllViews();

        View mainView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_edit_user_dialog, parent, false);
        parent.addView(mainView);

        setupRecyclerView(mainView); // Set lại RecyclerView
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<String> items = Arrays.asList("Tên", "Số điện thoại", "Ngày Sinh", "Địa Chỉ", "Giới Tính");

        EditUserAdapter adapter = new EditUserAdapter(items, position -> {
            if (position == 0) {
                switchToEditNameLayout();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), getResources().getColor(android.R.color.darker_gray), 4));
    }

    UserViewModel userViewModel;
    AuthenticationViewModel authenticationViewModel;
    EditText edt_ho, edt_tendem, edt_ten;
    String ho = "" , ten="", tendem = "";

    private void getProfileInfo(View editNameView)
    {
        userViewModel = new ViewModelProvider(this).get(UserViewModel.class);
        userViewModel.getInfo();
        userViewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<UserResponse>() {
            @Override
            public void onChanged(UserResponse userResponse) {
                if(userResponse!=null){
                    userUpdateRequest = new UserUpdateRequest(
                            userResponse.getFullName(),
                            userResponse.getPhone(),
                            userResponse.getDob(),
                            userResponse.getAddress(),
                            userResponse.getGender()
                    );
                }
                SetEdtName(editNameView, userResponse.getFullName());

            }
        });
        userViewModel.getUserInfoError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
        userViewModel.getUserInfoError().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void NameSplitter(String fullname)
    {
        String[] parts = fullname.trim().split("\\s+");
        if(parts.length ==1){
            ten = parts[0];
        }
        else if(parts.length == 2){
            ho = parts[0];
            ten = parts[1];
        }
        else{
            ho = parts[0];
            ten = parts[parts.length-1];
            tendem = String.join(" ", java.util.Arrays.copyOfRange(parts,1,parts.length-1));
        }
    }

    private void switchToEditNameLayout() {
        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        if (parent == null) return;

        parent.removeAllViews(); // Xóa tất cả view cũ

        View editNameView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_name, parent, false);
        getProfileInfo(editNameView);
        //Event
        updateName(editNameView);
        parent.addView(editNameView);

    }
    private void SetEdtName(View editNameView, String fullname)
    {
        //Lay thong tin ra

        edt_ho = editNameView.findViewById(R.id.edtHo);
        edt_tendem = editNameView.findViewById(R.id.edtTenDem);
        edt_ten = editNameView.findViewById(R.id.edtTen);

        //Tach ho, tendem, ten
        NameSplitter(fullname);

        edt_ho.setText(ho);
        edt_tendem.setText(tendem);
        edt_ten.setText(ten);
    }


    private void updateName(View editNameView){
        Button btnSave = editNameView.findViewById(R.id.btnSave);
        edt_ho = editNameView.findViewById(R.id.edtHo);
        edt_tendem = editNameView.findViewById(R.id.edtTenDem);
        edt_ten = editNameView.findViewById(R.id.edtTen);
        btnSave.setOnClickListener(v -> {
            String NewFullName = edt_ho.getText().toString() + " " + edt_tendem.getText().toString() + " "+
                    edt_ten.getText().toString();
            Log.d("Error Fullname", NewFullName);
            if(NewFullName!=null)
            {
                userUpdateRequest.setFullName(NewFullName);
                userViewModel.updateUser(userUpdateRequest);
            }

        });
        userViewModel.getMessageError().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(requireContext(), s,Toast.LENGTH_SHORT).show();
            }
        });
        userViewModel.getUserUpdateError().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(requireContext(), "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
            }
        });
        userViewModel.getUserUpdate().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                Toast.makeText(requireContext(), "Cập nhật tên thành công", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
