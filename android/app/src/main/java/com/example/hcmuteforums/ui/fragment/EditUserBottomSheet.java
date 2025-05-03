package com.example.hcmuteforums.ui.fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.EditUserAdapter;
import com.example.hcmuteforums.decoration.DividerItemDecoration;
import com.example.hcmuteforums.decoration.RecyclerViewBorderDecoration;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.request.UserUpdateRequest;
import com.example.hcmuteforums.model.dto.response.ProfileResponse;
import com.example.hcmuteforums.model.dto.response.UserResponse;
import com.example.hcmuteforums.ui.activity.user.EditNameActivity;
import com.example.hcmuteforums.ui.activity.user.VerifyOTPActivity;
import com.example.hcmuteforums.viewmodel.AuthenticationViewModel;
import com.example.hcmuteforums.viewmodel.ProfileViewModel;
import com.example.hcmuteforums.viewmodel.UserViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditUserBottomSheet extends BottomSheetDialogFragment {
    private ViewGroup containerView;
    UserViewModel userViewModel;
    AuthenticationViewModel authenticationViewModel;
    private final Calendar calendar = Calendar.getInstance();
    UserUpdateRequest userUpdateRequest;
    UserUpdateRequest user;
    UserResponse userCurrent;
    String fullname, dob, gender, address, phone, dobEdit;
    ImageView imgClose;

    TextView tv_fullname, tv_dob;
    TextInputEditText tIEDT_dob, tIEDT_address;

    EditText edt_ho, edt_tendem, edt_ten;
    Button btn_editDob;
    String ho = "" , ten="", tendem = "";
    RadioButton rbMale, rbFmale, rbOther;
    RadioGroup rgGender;

    boolean isLoadingData = false;
    void anhxa(View view)
    {
        imgClose = view.findViewById(R.id.imgClose);
        tv_fullname = view.findViewById(R.id.tv_Fullname);
    }
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
                TextView tv_fullname = view.findViewById(R.id.tv_Fullname);
                tv_fullname.setText(userCurrent.getFullName());
                getProfile(view);
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
        anhxa(view);
        if(getArguments()!=null){
            userCurrent = (UserResponse) getArguments().getSerializable("user");
        }
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        fullname = userCurrent.getFullName() != null ? userCurrent.getFullName() : "";
        dob = userCurrent.getDob() != null ? userCurrent.getDob() : "";
        gender = userCurrent.getGender() != null ? userCurrent.getGender() : "";
        address = userCurrent.getAddress() != null ? userCurrent.getAddress() : "Chưa có địa chỉ";
        phone = userCurrent.getPhone() != null ? userCurrent.getPhone() : "Chưa có số điện thoại";
        setupRecyclerView(view);
        observeUpdateData();
    }


    private void switchToMainLayout() {

        View view = getView();
        if (view == null) return;

        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        if (parent == null) return;

        parent.removeAllViews();

        View mainView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_edit_user_dialog, parent, false);
        imgClose = mainView.findViewById(R.id.imgClose);

        getProfile(mainView);
        TextView tv_fullname = mainView.findViewById(R.id.tv_Fullname);
        tv_fullname.setText(userCurrent.getFullName());
        parent.addView(mainView);
        setupRecyclerView(mainView); // Set lại RecyclerView
        imgClose.setOnClickListener(v -> {
            dismiss();
        });
    }
    void loadImage(View view){
        CircleImageView imgAvatar = view.findViewById(R.id.img_User);
        Glide.with(requireContext()).load("http://10.0.2.2:8080/ute/" + avatarProfile)
                .placeholder(R.drawable.avatar_boy)
                .error(R.drawable.anhhoixua)
                .into(imgAvatar);
    }

    private void setupRecyclerView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<String> items = Arrays.asList("Tên", "Ngày Sinh", "Địa Chỉ", "Giới Tính");

        EditUserAdapter adapter = new EditUserAdapter(items, position -> {
            if (position == 0) {
                switchToEditNameLayout();
            }
            else if(position == 1){
                switchToViewDobLayout();
            }else if(position==2){
                switchToEditAddressLayout();
            } else if (position ==3) {
                switchToEditGenderLayout();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), getResources().getColor(android.R.color.darker_gray), 4));
    }

    private void getProfileInfo(View editNameView)
    {
        userUpdateRequest = new UserUpdateRequest(
                userCurrent.getFullName(),
                userCurrent.getPhone(),
                userCurrent.getDob(),
                userCurrent.getAddress(),
                userCurrent.getGender()
        );
    }
    private void switchToEditAddressLayout(){
        View view = getView();
        if(view==null) return;
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        parent.removeAllViews();
        View editAddress = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_address, parent, true);
        getProfileInfo(editAddress);
        tIEDT_address = (TextInputEditText) editAddress.findViewById(R.id.edt_Address);
        tIEDT_address.setText(address);
        Button btn_save = (Button) editAddress.findViewById(R.id.btnSaveAddress);
        btn_save.setOnClickListener(v->{
            updateAddress(editAddress, tIEDT_address);
        });
        ImageView backMain = (ImageView) editAddress.findViewById(R.id.btnBack);
        backMain.setOnClickListener(v->{
            switchToMainLayout();
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
        SetEdtName(editNameView, fullname);
        Log.d("UserFullNam", fullname);
        //Event
        updateName(editNameView);
        parent.addView(editNameView);

        ImageView backMain = (ImageView) editNameView.findViewById(R.id.btnBack);
        backMain.setOnClickListener(v->{
            switchToMainLayout();
        });

    }
    private void switchToViewDobLayout(){
        View view = getView();
        if(view == null) return;
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        parent.removeAllViews();

        View viewDob = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet_edit_dob, parent, true);
        getProfileInfo(viewDob);
        SetTextDob(viewDob, userCurrent.getDob());
        btn_editDob = (Button) viewDob.findViewById(R.id.btnEditDob);
        btn_editDob.setOnClickListener(v -> {
            switchToEditDobLayout(dob);
        });
        ImageView backMain = (ImageView) viewDob.findViewById(R.id.btnBack);
        backMain.setOnClickListener(v->{
            switchToMainLayout();
        });
    }
    private void switchToEditGenderLayout(){
        View view = getView();
        if(view == null)   return;
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.bottomSheetContainer);
        parent.removeAllViews();
        View viewGender = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_edit_gender, parent, true);
        getProfileInfo(viewGender);
        rbMale = (RadioButton) viewGender.findViewById(R.id.rb_male);
        rbFmale = (RadioButton) viewGender.findViewById(R.id.rb_female);
        rbOther =(RadioButton) viewGender.findViewById(R.id.rb_other);
        rgGender =(RadioGroup) view.findViewById(R.id.rg_gender);

        setGenderChecked(gender);

        Button btn_save = viewGender.findViewById(R.id.btnSaveGender);
        btn_save.setOnClickListener(v->{
            updateGender(viewGender);
        });
        ImageView backMain = (ImageView) viewGender.findViewById(R.id.btnBack);
        backMain.setOnClickListener(v->{
            switchToMainLayout();
        });

    }
    private void switchToEditDobLayout(String dob){
        View view = getView();
        if(view == null) return;
        ViewGroup parent = (ViewGroup) view.findViewById(R.id.dobContainer);
        parent.removeAllViews();
        View editDob = LayoutInflater.from(getContext()).inflate(R.layout.fragment_bottom_sheet_edit_dob, parent, true);
        tIEDT_dob = (TextInputEditText) editDob.findViewById(R.id.edtDob);
        ImageView back= (ImageView) editDob.findViewById(R.id.btnBack);
        tIEDT_dob.setText(dob);
        tIEDT_dob.setOnClickListener(v->{
            showDatePickerDialog();
        });
        Button btn_save = (Button) editDob.findViewById(R.id.btnSaveDob);
        btn_save.setOnClickListener(v->{
            updateDob(editDob, tIEDT_dob);
        });
        back.setOnClickListener(v->{
            switchToViewDobLayout();
        });
    }

    private void showDatePickerDialog() {
        String dobText = tIEDT_dob.getText().toString().trim();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        if (!dobText.isEmpty()) {
            try {
                Date parsedDate = sdf.parse(dobText);
                calendar.setTime(parsedDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        new DatePickerDialog(getContext(), (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            tIEDT_dob.setText(sdf.format(calendar.getTime()));
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
    private String convertDate(String inputDate){
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Định dạng đầu ra: Ngày 21, tháng 4, năm 2000
        SimpleDateFormat outputFormat = new SimpleDateFormat("d 'tháng' M, yyyy", new Locale("vi"));
        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "Không rõ ngày";
        }
    }

    private void SetTextDob(View viewDob, String dob){
        tv_dob = (TextView) viewDob.findViewById(R.id.textDob);
        String formattedDate = convertDate(dob);
        tv_dob.setText(formattedDate);
    }
    private void SetEdtName(View editNameView, String fullname) {
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

    private void setGenderChecked(String gender){
        if(gender == null)  return;
        if(gender.equalsIgnoreCase("Nam")){
            rbMale.setChecked(true);
        }else if(gender.equalsIgnoreCase("Nữ")){
            rbFmale.setChecked(true);
        }else if(gender.equalsIgnoreCase("Không muốn tiết lộ")){
            rbOther.setChecked(true);
        }
    }

    private void updateDob(View editDob, TextInputEditText edt_dob){
        Button btnSave = editDob.findViewById(R.id.btnSaveDob);

        btnSave.setOnClickListener(view -> {
            String NewDob = edt_dob.getText().toString().trim();
            if(NewDob!=null){
                userUpdateRequest.setDob(NewDob);
                userViewModel.updateUser(userUpdateRequest);
                userCurrent.setDob(NewDob);
                // Quan sát lỗi cập nhật thông tin người dùng

            }
        });

    }
    private void updateAddress(View editAddress, TextInputEditText edt_Address){
        Button btnSave = editAddress.findViewById(R.id.btnSaveAddress);
        btnSave.setOnClickListener(view -> {
            String newAddress = edt_Address.getText().toString();
            if(newAddress!=null){
                userUpdateRequest.setAddress(newAddress);
                userViewModel.updateUser(userUpdateRequest);
                userCurrent.setAddress(newAddress);
            }
        });
    }


    private void updateName(View editNameView){
        Button btnSave = editNameView.findViewById(R.id.btnSaveName);
        edt_ho = editNameView.findViewById(R.id.edtHo);
        edt_tendem = editNameView.findViewById(R.id.edtTenDem);
        edt_ten = editNameView.findViewById(R.id.edtTen);

        String mFullName;
        btnSave.setOnClickListener(v -> {
            String NewFullName = edt_ho.getText().toString() + " " + edt_tendem.getText().toString() + " "+
                    edt_ten.getText().toString();
            Log.d("Error Fullname", NewFullName);
            if(NewFullName!=null)
            {
                userUpdateRequest.setFullName(NewFullName);
                userViewModel.updateUser(userUpdateRequest);  //Lay ra ten duoc cap nhat dem ve cho MainSheet
            }

        });
    }
    private void updateGender(View viewGender){
        Button btn_save = (Button) viewGender.findViewById(R.id.btnSaveGender);

        btn_save.setOnClickListener(v->{
            int selectedId = rgGender.getCheckedRadioButtonId();

            String gender = "";
            if (selectedId == R.id.rb_male) {
                gender = "Nam";
            } else if (selectedId == R.id.rb_female) {
                gender = "Nữ";
            } else if (selectedId == R.id.rb_other) {
                gender = "Không muốn tiết lộ";
            }
            if(gender!=null){
                userUpdateRequest.setGender(gender);
                userViewModel.updateUser(userUpdateRequest);

            }
        });
    }
    ProfileViewModel profileViewModel;
    String avatarProfile;
    private void getProfile(View view){
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getProfile();
        profileViewModel.getProfileInfo().observe(getViewLifecycleOwner(), new Observer<ProfileResponse>() {
            @Override
            public void onChanged(ProfileResponse profileResponse) {
                avatarProfile = profileResponse.getAvatarUrl();
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

    private void observeUpdateData(){
        userViewModel.getUserUpdateError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent();
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi trong quá trình cập nhật thông tin", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Quan sát thông báo cập nhật thành công
        userViewModel.getUserUpdate().observe(getViewLifecycleOwner(), new Observer<Event<UserResponse>>() {
            @Override
            public void onChanged(Event<UserResponse> event) {
                UserResponse userResponse = event.getContent();
                if (userResponse != null) {
                    Toast.makeText(getContext(), "Cập nhật thông tin thành công", Toast.LENGTH_SHORT).show();
                    fullname = userResponse.getFullName() != null ? userResponse.getFullName() : "Chưa có tên";
                    dob = userResponse.getDob() != null ? userResponse.getDob() : "Chưa có ngày sinh";
                    dobEdit = dob;
                    address = userResponse.getAddress() != null ? userResponse.getAddress() : "Chưa có địa chỉ";
                    gender = userResponse.getGender() != null ? userResponse.getGender() : "Chưa có giới tính";

                    switchToMainLayout();   //Chuyen ve mainSheet
                }
            }
        });

        // Quan sát thông báo lỗi
        userViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String errorMessage = event.getContent();
                if (errorMessage != null) {
                    Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
