package com.example.hcmuteforums.ui.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.CategoryAdapter;
import com.example.hcmuteforums.adapter.TopicDetailAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnReplyClickListener;
import com.example.hcmuteforums.listeners.TopicLikeListener;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.model.entity.Category;
import com.example.hcmuteforums.ui.activity.topic.TopicPostActivity;
import com.example.hcmuteforums.viewmodel.TopicDetailViewModel;
import com.example.hcmuteforums.viewmodel.TopicViewModel;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements TopicLikeListener, OnReplyClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private TopicViewModel topicViewModel;
    private TopicDetailViewModel topicDetailViewModel;
    private CardView cvPostTopic;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //init data
        topicViewModel = new TopicViewModel();
        topicDetailViewModel = new TopicDetailViewModel();
        cvPostTopic = view.findViewById(R.id.cvPostTopic);
        //show category
        showAllTopic(view);
        //go to post topic
        postTopic();
        return view;
    }

    private void postTopic() {
        cvPostTopic.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), TopicPostActivity.class);
            startActivity(myIntent);
        });
    }

    private void showAllTopic(View view) {
        RecyclerView rcvTopic = view.findViewById(R.id.rcvTopic);
        TopicDetailAdapter topicDetailAdapter = new TopicDetailAdapter(getContext(), this, this);

        //get data from viewmodel
        topicViewModel.fetchAllTopics();
        //observe
        topicViewModel.getTopicsLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<TopicDetailResponse>>() {
            @Override
            public void onChanged(PageResponse<TopicDetailResponse> topicDetailResponses) {
                topicDetailAdapter.setData(topicDetailResponses.getContent());
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

        topicViewModel.getTopicError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });


        RecyclerView.LayoutManager linearLayout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        rcvTopic.setLayoutManager(linearLayout);
        rcvTopic.setAdapter(topicDetailAdapter);
    }

    @Override
    public void likeTopic(String topicId) {
        topicDetailViewModel.likeTopic(topicId);
    }

    @Override
    public void onReply(String topicId) {
        ReplyBottomSheetFragment.newInstance(topicId).show(getParentFragmentManager(), "ReplyBottomSheet");
    }
}