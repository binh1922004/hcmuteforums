package com.example.hcmuteforums.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.EditUserAdapter;
import com.example.hcmuteforums.decoration.DividerItemDecoration;
import com.example.hcmuteforums.decoration.RecyclerViewBorderDecoration;
import com.example.hcmuteforums.ui.activity.user.EditNameActivity;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Arrays;
import java.util.List;

public class EditUserBottomSheet extends BottomSheetDialogFragment {

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

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        List<String> items = Arrays.asList("Tên", "Số điện thoại", "Ngày Sinh", "Địa Chỉ", "Giới Tính");

        // Kiểm tra Log để biết danh sách có dữ liệu không
        Log.d("EditUserBottomSheet", "Danh sách item: " + items);

        EditUserAdapter adapter = new EditUserAdapter(items, new EditUserAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(getContext(), "Clicked: " + items.get(position), Toast.LENGTH_SHORT).show();
                if (position == 0) { // Nếu click vào "Tên"
                    Intent intent = new Intent(getActivity(), EditNameActivity.class);
                    intent.putExtra("firstName", "Nghĩa");
                    intent.putExtra("middleName", "");
                    intent.putExtra("lastName", "Trần");
                    startActivity(intent);
                }
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        int dividerColor = getResources().getColor(android.R.color.darker_gray);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), dividerColor, 4));
    }


}
