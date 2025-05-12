package com.example.hcmuteforums.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.TopicDetailAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnReplyAddedListener;
import com.example.hcmuteforums.listeners.OnReplyShowListener;
import com.example.hcmuteforums.listeners.OnSwitchActivityActionListener;
import com.example.hcmuteforums.listeners.TopicLikeListener;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.ui.activity.topic.TopicDetailActivity;
import com.example.hcmuteforums.viewmodel.TopicDetailViewModel;
import com.example.hcmuteforums.viewmodel.TopicViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TopicFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TopicFragment extends Fragment implements
        TopicLikeListener, OnReplyShowListener, OnSwitchActivityActionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    // TODO: Rename and change types of parameters

    private String username = null;

    //elements
    private RecyclerView rcvTopic;
    //view model
    private TopicViewModel topicViewModel;
    private TopicDetailViewModel topicDetailViewModel;
    //adapter
    private TopicDetailAdapter topicDetailAdapter;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 0;
    private final int pageSize = 6;
    public TopicFragment() {
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TopicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TopicFragment newInstance(String username) {
        TopicFragment fragment = new TopicFragment();
        Bundle args = new Bundle();
        args.putString("username", username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString("username");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_topic, container, false);
        //mapping data
        rcvTopic = view.findViewById(R.id.rcvTopic);
        topicViewModel = new TopicViewModel();
        topicDetailViewModel = new TopicDetailViewModel();
        //adapter config
        adapterConfig();
        //observe data
        observeData();
        //show first time
        showMoreTopic();
        return view;
    }

    private void adapterConfig() {
        topicDetailAdapter = new TopicDetailAdapter(getContext(), this, this, this);
        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvTopic.setLayoutManager(linearLayout);
        rcvTopic.setAdapter(topicDetailAdapter);
        rcvTopic.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                        showMoreTopic();
                    }
                }
            }
        });
    }

    private void showMoreTopic(){
        //get data from viewmodel
        topicViewModel.getAllTopicsByUsername(username, currentPage);
        currentPage++;
    }
    private void observeData() {
        //observe
        topicViewModel.getTopicsByUserLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<TopicDetailResponse>>() {
            @Override
            public void onChanged(PageResponse<TopicDetailResponse> topicDetailResponses) {
                topicDetailAdapter.addData(topicDetailResponses.getContent());
                isLastPage = topicDetailResponses.isLast();
            }
        });

        topicViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> event) {
                String message = event.getContent(); // Lấy thông báo lỗi chưa được xử lý
                if (message != null) {
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        topicViewModel.getTopicByUserError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

//        topicViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
//            @Override
//            public void onChanged(Boolean loading) {
//                isLoading = loading;
//                if (loading){
//                    topicDetailAdapter.addLoadingFooter();
//                }
//                else{
//                    topicDetailAdapter.removeLoadingFooter();
//                }
//            }
//        });
    }

    @Override
    public void onClickProfile(String username) {
        SharedPreferences preferences = getContext().getSharedPreferences("User", MODE_PRIVATE); //Set danh dau dang nhap
        if (preferences.getBoolean("isLoggedIn", false)){
            String currentUserName = preferences.getString("username", null);
            if (!Objects.equals(currentUserName, username)){
                AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();
                Bundle bundle = new Bundle();
                bundle.putString("username", username);
                bundle.putString("currentUsername", currentUserName);
                anyProfileUserFragment.setArguments(bundle);
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.flFragment, anyProfileUserFragment)
                        .addToBackStack(null)
                        .commit();
            }
        }
        else{
            AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();
            Bundle bundle = new Bundle();
            bundle.putString("username", username);
            anyProfileUserFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, anyProfileUserFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }

    @Override
    public void onClickTopicDetail(String topicId, boolean isOwner) {
        Intent topicIntent = new Intent(getContext(), TopicDetailActivity.class);
        topicIntent.putExtra("topicId", topicId);
        topicIntent.putExtra("isOwner", isOwner);
        startActivity(topicIntent);
    }
    @Override
    public void likeTopic(String topicId) {
        topicDetailViewModel.likeTopic(topicId);
    }

    @Override
    public void onReply(String topicId, int position) {
        var replyBottomSheetFragment = ReplyBottomSheetFragment.newInstance(topicId);
        replyBottomSheetFragment.setOnReplyAddedListener(new OnReplyAddedListener() {
            @Override
            public void onReplyAdded(ReplyResponse replyResponse) {
                // ✅ Cập nhật số lượng reply cho topic tương ứng
                TopicDetailResponse topic = topicDetailAdapter.getData().get(position);
                topic.setReplyCount(topic.getReplyCount() + 1);
                topicDetailAdapter.notifyItemChanged(position);
            }
        });
        replyBottomSheetFragment.show(getParentFragmentManager(), "ReplyBottomSheet");
    }
}