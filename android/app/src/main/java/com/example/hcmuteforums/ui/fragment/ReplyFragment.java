package com.example.hcmuteforums.ui.fragment;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.ReplyAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnMenuActionListener;
import com.example.hcmuteforums.listeners.OnReplyClickListener;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.viewmodel.ReplyViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ReplyFragment extends Fragment implements OnReplyClickListener, OnMenuActionListener {
    private final String TAG = "ReplyFragment";
    //element from layout
    private RecyclerView rcvReplies;
    private EditText edtComment;
    private ImageButton btnSend;
    private ImageView btnCancel;
    private LinearLayout layoutText;

    //adapter config
    private ReplyAdapter replyAdapter;
    private List<ReplyResponse> replyList = new ArrayList<>();
    //viewmodel
    ReplyViewModel replyViewModel;

    //attribute
    private String topicId;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private boolean isFirstLoad = true;
    private int pageSize = 10;
    private int currentPage = 0;

    //listener interface

    //hash map for storage page size of each reply child
    HashMap<String, Integer> currentPageReplyChildMap;
    HashMap<String, Boolean> isLastPageReplyChildMap;
    HashMap<String, Integer> positionDelete;
    HashMap<String, Integer> positionUpdate;

    //reply information
    private String parentReplyId = null;
    private String replyingToUser = null;
    //fields
    private String replyIdFromNotification = null;
    //support
    LoadingDialogFragment loadingDialog;


    public static ReplyFragment newInstance(String topicId) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("topicId", topicId);
        fragment.setArguments(args);
        return fragment;
    }

    public static ReplyFragment newInstance(String topicId, String replyId) {
        ReplyFragment fragment = new ReplyFragment();
        Bundle args = new Bundle();
        args.putString("topicId", topicId);
        args.putString("replyId", replyId);
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reply, container, false);
        //mapping data
        rcvReplies = view.findViewById(R.id.recyclerReplies);
        edtComment = view.findViewById(R.id.edtComment);
        btnSend = view.findViewById(R.id.btnSend);
        btnCancel = view.findViewById(R.id.btnCancel);
        layoutText = view.findViewById(R.id.layoutText);
        replyViewModel = new ReplyViewModel();
        topicId = getArguments().getString("topicId");
        replyIdFromNotification = getArguments().getString("replyId");
        currentPageReplyChildMap = new HashMap<>();
        isLastPageReplyChildMap = new HashMap<>();
        positionDelete = new HashMap<>();
        positionUpdate = new HashMap<>();
        replyAdapterConfig();

        loadMoreReplies();

        sendReply();

        observeData();

        cancelReply();

        return view;
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Ẩn dialog nếu đang hiển thị
        if (loadingDialog != null && loadingDialog.isAdded()) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }
    private void cancelReply() {
        btnCancel.setOnClickListener(v -> {
            btnCancel.setVisibility(View.GONE);
            edtComment.setText("");
            edtComment.setHint("Viết bình luận...");
            replyingToUser = null;
            parentReplyId = null;
        });
    }

    //mapping for data
    private void replyAdapterConfig() {
        rcvReplies.setLayoutManager(new LinearLayoutManager(getContext()));
        replyAdapter = new ReplyAdapter(getContext(), replyList, this, this);
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
        //reply detail response
        replyViewModel.getDetailReplyLiveData().observe(getViewLifecycleOwner(), new Observer<Event<ReplyResponse>>() {
            @Override
            public void onChanged(Event<ReplyResponse> replyResponseEvent) {
                var replyResponse = replyResponseEvent.getContent();
                if (replyResponse != null){
                    replyAdapter.addNewReply(replyResponse);
                }
            }
        });
        replyViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean isLoading) {
                if (isLoading) {
                    // Chỉ hiển thị dialog nếu chưa được thêm
                    if (loadingDialog == null || !loadingDialog.isAdded()) {
                        loadingDialog = new LoadingDialogFragment();
                        loadingDialog.show(getParentFragmentManager(), "LoadingDialog");
                    }
                } else {
                    // Ẩn dialog nếu đang hiển thị
                    if (loadingDialog != null && loadingDialog.isAdded()) {
                        loadingDialog.dismiss();
                    }
                }
            }
        });
        //list reply response success
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
        //reply message error
        replyViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                }
            }
        });
        //reply post success
        replyViewModel.getReplyPostLiveData().observe(getViewLifecycleOwner(), new Observer<Event<ReplyResponse>>() {
            @Override
            public void onChanged(Event<ReplyResponse> replyResponseEvent) {
                ReplyResponse replyResponse = replyResponseEvent.getContent();
                if (replyResponse != null){
                    layoutText.setVisibility(View.GONE);
                    if (replyResponse.getParentReplyId() == null || replyResponse.getParentReplyId() == ""){
                        replyAdapter.addNewReply(replyResponse);
                        rcvReplies.smoothScrollToPosition(0);
                    }
                    else{
                        replyAdapter.addNewReplyChild(replyResponse);
                    }
                }
            }
        });


        //reply child
        replyViewModel.getReplyChildLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<ReplyResponse>>() {
            @Override
            public void onChanged(PageResponse<ReplyResponse> replyResponsePageResponse) {
                var replyChildList = replyResponsePageResponse.getContent();
                replyAdapter.addNewReplyChildList(replyChildList, replyResponsePageResponse.isLast());
                if (!replyChildList.isEmpty()){
                    String parentReplyId = replyChildList.get(0).getParentReplyId();
                    isLastPageReplyChildMap.put(parentReplyId, replyResponsePageResponse.isLast());
                }
            }
        });
        replyViewModel.getMessageChildError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //TODO: Delete reply
        replyViewModel.getReplyDeleteLiveData().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> result) {
                String replyId = result.getContent();
                if (replyId != null && positionDelete.containsKey(replyId)){
                    Integer pos = positionDelete.get(replyId);
                    replyAdapter.deleteReply(pos);
                    positionDelete.remove(replyId);
                }
            }
        });

        //TODO: Update reply
        replyViewModel.getReplyUpdateLiveData().observe(getViewLifecycleOwner(), new Observer<Event<ReplyResponse>>() {
            @Override
            public void onChanged(Event<ReplyResponse> replyResponseEvent) {
                ReplyResponse reply = replyResponseEvent.getContent();
                if (reply != null && positionUpdate.containsKey(reply.getId())){
                    int pos = positionUpdate.get(reply.getId());
                    replyAdapter.updateReply(pos, reply.getContent());
                    positionUpdate.remove(reply.getId());
                }
            }
        });
    }

    private void loadMoreReplies() {
        if (replyIdFromNotification != null){
            replyViewModel.getDetailReply(replyIdFromNotification);
        }

        replyViewModel.getAllRepliesByTopicId(topicId, currentPage);
        currentPage++;
    }

    private void sendReply() {
        btnSend.setOnClickListener(v -> {
            String comment = edtComment.getText().toString().trim();
            if (!comment.isEmpty()) {
                replyViewModel.postReply(comment, parentReplyId, replyingToUser, topicId);
                edtComment.setText("");
                edtComment.setHint("Viết bình luận...");
                replyingToUser = null;
            }
        });
    }

    @Override
    public void onReplyClick(ReplyResponse reply) {
        replyingToUser = reply.getUserGeneral().getUsername();
        parentReplyId = reply.getParentReplyId() == null ? reply.getId() : reply.getParentReplyId();
        edtComment.setHint("Reply @" + replyingToUser);
        btnCancel.setVisibility(View.VISIBLE);
    }

    @Override
    public void onShowChildReply(ReplyResponse reply) {
        int currPageChild = currentPageReplyChildMap.getOrDefault(reply.getId(), -1);
        currPageChild++;
        currentPageReplyChildMap.put(reply.getId(), currPageChild);
        replyViewModel.getAllRepliesByParentReplyId(reply.getId(), currPageChild);

        Log.d(TAG, "Go on show child");
    }

    @Override
    public void onHideChildReply(ReplyResponse reply) {
        currentPageReplyChildMap.remove(reply.getId());
        isLastPageReplyChildMap.remove(reply.getId());
    }

    @Override
    public void onUpdate(String replyId, String content, int pos) {
        // Create and show AlertDialog for editing
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Chỉnh sửa câu trả lời");

        // Create EditText for input
        final EditText input = new EditText(getContext());
        input.setText(content);
        builder.setView(input);

        // Set up buttons
        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String newContent = input.getText().toString().trim();
            if (!newContent.isEmpty()) {
                // Store position and call API
                positionUpdate.put(replyId, pos);
                replyViewModel.updateReply(replyId, newContent);
            } else {
                Toast.makeText(getContext(), "Nội dung không được để trống", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDelete(String replyId, int pos) {
        positionDelete.put(replyId, pos);
        replyViewModel.deleteReply(replyId);
    }

    @Override
    public void onCopy(String content) {
        // Sao chép văn bản vào clipboard
        ClipboardManager clipboard = (ClipboardManager) requireContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", content);
        clipboard.setPrimaryClip(clip);
        // Thông báo cho người dùng
    }
}
