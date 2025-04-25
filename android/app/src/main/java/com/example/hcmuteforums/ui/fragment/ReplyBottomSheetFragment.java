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
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ReplyAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnReplyAddedListener;
import com.example.hcmuteforums.model.dto.PageResponse;
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
    //element from layout
    private RecyclerView rcvReplies;
    private EditText edtComment;
    private ImageButton btnSend;
    private LinearLayout layoutText;

    //adapter config
    private ReplyAdapter replyAdapter;
    private List<ReplyResponse> replyList = new ArrayList<>();
    //viewmodel
    ReplyViewModel replyViewModel;

    //attribute
    private String replyingToUser = null; // Username người đang được reply
    private String topicId;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isFirstLoad = true;
    private int pageSize = 10;
    private int currentPage = 0;

    //listiner interface
    OnReplyAddedListener onReplyAddedListener;
    public static ReplyBottomSheetFragment newInstance(String topicId) {
        ReplyBottomSheetFragment fragment = new ReplyBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString("topicId", topicId);
        fragment.setArguments(args);
        return fragment;
    }
    public void setOnReplyAddedListener(OnReplyAddedListener onReplyAddedListener){
        this.onReplyAddedListener = onReplyAddedListener;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply_bottom_sheet, container, false);
        //mapping data
        rcvReplies = view.findViewById(R.id.recyclerReplies);
        edtComment = view.findViewById(R.id.edtComment);
        btnSend = view.findViewById(R.id.btnSend);
        layoutText = view.findViewById(R.id.layoutText);
        replyViewModel = new ReplyViewModel();
        topicId = getArguments().getString("topicId");

        replyAdapterConfig();

        loadMoreReplies();

        sendReply();

        observeData();

        return view;
    }

    //mapping for data
    private void replyAdapterConfig() {
        rcvReplies.setLayoutManager(new LinearLayoutManager(getContext()));
        replyAdapter = new ReplyAdapter(getContext(), replyList, reply -> {
            replyingToUser = reply.getUserGeneral().getUsername();
            edtComment.setHint("Reply @" + replyingToUser);
        });
        rcvReplies.setAdapter(replyAdapter);


        rcvReplies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (layoutManager == null) return;

                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading && !isLastPage) {
                    if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                            && firstVisibleItemPosition >= 0
                            && totalItemCount >= pageSize) {
                        loadMoreReplies();
                    }
                }
            }
        });
    }

    private void observeData(){
        replyViewModel.getReplyLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<ReplyResponse>>() {
            @Override
            public void onChanged(PageResponse<ReplyResponse> replyResponses) {
                if (isFirstLoad && (replyResponses == null || replyResponses.getContent().isEmpty())){
                    layoutText.setVisibility(View.VISIBLE);
                }
                else {
                    replyAdapter.addData(replyResponses.getContent());
                    isLastPage = replyResponses.isLast();
                    isFirstLoad = false;
                }
            }
        });

        replyViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                }
            }
        });
        replyViewModel.getReplyPostSuccess().observe(getViewLifecycleOwner(), new Observer<Event<ReplyResponse>>() {
            @Override
            public void onChanged(Event<ReplyResponse> replyResponseEvent) {
                ReplyResponse replyResponse = replyResponseEvent.getContent();
                if (replyResponse != null){
                    layoutText.setVisibility(View.GONE);
                    replyAdapter.addNewReply(replyResponse);
                    rcvReplies.smoothScrollToPosition(0);
                    if (onReplyAddedListener != null){
                        onReplyAddedListener.onReplyAdded(replyResponse);
                    }
                }
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
                layoutParams.height = (int)(getResources().getDisplayMetrics().heightPixels * 0.8);
                bottomSheet.setLayoutParams(layoutParams);

                // Cho mở rộng luôn khi hiển thị
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setSkipCollapsed(true);
            }
        }
    }

    private void loadMoreReplies() {
        // TODO: Gọi API hoặc lấy từ ViewModel
        // Ví dụ:
        replyViewModel.getAllRepliesByTopicId(topicId, currentPage);
        currentPage++;
    }


    private void sendReply() {
//        // TODO: Gửi reply về backend hoặc ViewModel
        btnSend.setOnClickListener(v -> {
            String comment = edtComment.getText().toString().trim();
            if (!comment.isEmpty()) {
                replyViewModel.postReply(comment, "", topicId);
                edtComment.setText("");
                edtComment.setHint("Viết bình luận...");
                replyingToUser = null;
            }
        });
    }

    public ReplyResponse getDataFromFragment(){
        ReplyResponse response = new ReplyResponse();
//        response.
        return response;
    }
}
