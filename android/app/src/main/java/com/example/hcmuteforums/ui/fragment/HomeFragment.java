package com.example.hcmuteforums.ui.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.example.hcmuteforums.adapter.TopicDetailAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.listeners.OnSwitchActivityActionListener;
import com.example.hcmuteforums.listeners.OnReplyAddedListener;
import com.example.hcmuteforums.listeners.OnReplyShowListener;
import com.example.hcmuteforums.listeners.TopicLikeListener;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;
import com.example.hcmuteforums.ui.activity.topic.TopicDetailActivity;
import com.example.hcmuteforums.ui.activity.topic.TopicPostActivity;
import com.example.hcmuteforums.viewmodel.TopicDetailViewModel;
import com.example.hcmuteforums.viewmodel.TopicViewModel;

import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements
        TopicLikeListener, OnReplyShowListener, OnSwitchActivityActionListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //view model
    private TopicViewModel topicViewModel;
    private TopicDetailViewModel topicDetailViewModel;
    //element
    private CardView cvPostTopic;
    RecyclerView rcvTopic;
    //adapter
    TopicDetailAdapter topicDetailAdapter;
    private boolean isLastPage = false;
    private boolean isLoading = false;
    private int currentPage = 0;
    private final int pageSize = 6;


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
        //mapping data
        mappingData(view);

        //recyclerView
        recyclerViewConfig();

        observeData();
        //go to post topic
        postTopic();
        return view;
    }

    private void mappingData(View view) {

        //init data
        topicViewModel = new TopicViewModel();
        topicDetailViewModel = new TopicDetailViewModel();
        cvPostTopic = view.findViewById(R.id.cvPostTopic);
        rcvTopic = view.findViewById(R.id.rcvTopic);
    }

    private void recyclerViewConfig(){
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

    private void showMoreTopic() {
        //get data from viewmodel
        topicViewModel.fetchAllTopics(currentPage);
        currentPage++;
    }

    private void postTopic() {
        cvPostTopic.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), TopicPostActivity.class);
            startActivity(myIntent);
        });
    }

    private void observeData() {
        //observe
        topicViewModel.getTopicsLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<TopicDetailResponse>>() {
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

        topicViewModel.getTopicError().observe(getViewLifecycleOwner(), new Observer<Event<Boolean>>() {
            @Override
            public void onChanged(Event<Boolean> event) {
                Boolean errorOccurred = event.getContent(); // Lấy lỗi chưa được xử lý
                if (errorOccurred != null && errorOccurred) {
                    Toast.makeText(getContext(), "Đã xảy ra lỗi", Toast.LENGTH_SHORT).show();
                }
            }
        });

        topicViewModel.getIsLoading().observe(getViewLifecycleOwner(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean loading) {
                isLoading = loading;
                if (loading){
                    topicDetailAdapter.addLoadingFooter();
                }
                else{
                    topicDetailAdapter.removeLoadingFooter();
                }
            }
        });
    }


    //override section
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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        resetData();
    }

    @Override
    public void onResume() {
        super.onResume();
        resetData();
    }

    private void resetData() {
        currentPage = 0;
        isLastPage = false;
        topicDetailAdapter.clearData(); // Xóa hết dữ liệu hiện có
        showMoreTopic();            // Gọi lại API trang đầu tiên
    }

    @Override
    public void onClickProfile(String username) {
        /*SharedPreferences preferences = getContext().getSharedPreferences("User", MODE_PRIVATE); //Set danh dau dang nhap
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
            bundle.putString("currentUsername", "guest");
            anyProfileUserFragment.setArguments(bundle);
            getParentFragmentManager().beginTransaction()
                    .replace(R.id.flFragment, anyProfileUserFragment)
                    .addToBackStack(null)
                    .commit();
        }*/
        SharedPreferences preferences = getContext().getSharedPreferences("User", MODE_PRIVATE);
        String currentUserName = preferences.getString("username", "guest"); // Giá trị mặc định "guest" nếu chưa đăng nhập
        boolean isLoggedIn = preferences.getBoolean("isLoggedIn", false);

        AnyProfileUserFragment anyProfileUserFragment = new AnyProfileUserFragment();
        Bundle bundle = new Bundle();
        bundle.putString("username", username);
        bundle.putString("currentUsername", currentUserName);
        bundle.putBoolean("isLoggedIn", isLoggedIn); // Truyền trạng thái đăng nhập
        bundle.putString("loginPrompt", isLoggedIn ? null : "Bạn cần đăng nhập để theo dõi người dùng này"); // Thông điệp tùy chỉnh
        anyProfileUserFragment.setArguments(bundle);

        getParentFragmentManager().beginTransaction()
                .replace(R.id.flFragment, anyProfileUserFragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onClickTopicDetail(String topicId, boolean isOwner) {
        Intent topicIntent = new Intent(getContext(), TopicDetailActivity.class);
        topicIntent.putExtra("topicId", topicId);
        topicIntent.putExtra("isOwner", isOwner);
        startActivity(topicIntent);
    }
}


