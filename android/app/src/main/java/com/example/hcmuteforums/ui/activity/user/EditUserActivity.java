package com.example.hcmuteforums.ui.activity.user;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.EditUserAdapter;
import com.example.hcmuteforums.decoration.DividerItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_user);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        List<String> items = Arrays.asList("Tên", "Số điện thoại", "Ngày Sinh", "Địa Chỉ", "Giới Tính");
        EditUserAdapter adapter = new EditUserAdapter(items);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        int dividerColor = getResources().getColor(android.R.color.darker_gray);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, dividerColor, 4));
    }
}