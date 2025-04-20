package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ReplyAdapter;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.viewmodel.ReplyViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReplyBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReplyBottomSheetFragment extends BottomSheetDialogFragment {

    private RecyclerView recyclerView;
    private EditText edtComment;
    private ImageButton btnSend;

    //adapter config
    private ReplyAdapter replyAdapter;
    private List<ReplyResponse> replyList = new ArrayList<>();
    //viewmodel
    ReplyViewModel replyViewModel;

    //attribute
    private String replyingToUser = null; // Username người đang được reply
    private String topicId;
    public static ReplyBottomSheetFragment newInstance(String topicId) {
        ReplyBottomSheetFragment fragment = new ReplyBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("topicId", topicId);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_bottom_sheet, container, false);
        recyclerView = view.findViewById(R.id.recyclerReplies);
        edtComment = view.findViewById(R.id.edtComment);
        btnSend = view.findViewById(R.id.btnSend);
        replyViewModel = new ReplyViewModel();
        topicId = getArguments().getString("topicId");

        replyAdapterConfig();

        loadReplies();

        return view;
    }

    private void replyAdapterConfig() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        replyAdapter = new ReplyAdapter(replyList, reply -> {
            replyingToUser = reply.getUserGeneral().getUsername();
            edtComment.setHint("Reply @" + replyingToUser);
        });
        recyclerView.setAdapter(replyAdapter);

        btnSend.setOnClickListener(v -> {
            String comment = edtComment.getText().toString().trim();
            if (!comment.isEmpty()) {
                String finalComment = replyingToUser != null ? "@" + replyingToUser + " " + comment : comment;
                sendReply(finalComment);
                edtComment.setText("");
                edtComment.setHint("Viết bình luận...");
                replyingToUser = null;
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Lấy BottomSheet ra khỏi dialog
        BottomSheetDialog dialog = (BottomSheetDialog) getDialog();
        if (dialog != null) {
            FrameLayout bottomSheet = dialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                // Đặt chiều cao mong muốn (90% chiều cao màn hình)
                ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();
                layoutParams.height = (int)(getResources().getDisplayMetrics().heightPixels * 0.9);
                bottomSheet.setLayoutParams(layoutParams);

                // Cho mở rộng luôn khi hiển thị
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        }
    }

    private void loadReplies() {
        // TODO: Gọi API hoặc lấy từ ViewModel
        // Ví dụ:
        replyViewModel.getAllRepliesByTopicId(topicId);
        replyViewModel.getReplyLiveData().observe(getViewLifecycleOwner(), new Observer<List<ReplyResponse>>() {
            @Override
            public void onChanged(List<ReplyResponse> replyResponses) {
                replyAdapter.setData(replyResponses);
            }
        });
    }

    private void sendReply(String content) {
//        // TODO: Gửi reply về backend hoặc ViewModel
//        replyList.add();
//        replyAdapter.notifyItemInserted(replyList.size() - 1);
//        recyclerView.scrollToPosition(replyList.size() - 1);
    }

    public ReplyResponse getDataFromFragment(){
        ReplyResponse response = new ReplyResponse();
//        response.
        return response;
    }
}
