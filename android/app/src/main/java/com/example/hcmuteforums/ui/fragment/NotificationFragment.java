package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.hcmuteforums.R;
import com.example.hcmuteforums.adapter.NotificationAdapter;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.viewmodel.NotificationViewModel;
import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotificationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private final String TAG = "NotificationTag";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    //param:
    private int currentPage = 0;

    //fields
    RecyclerView rcvNotification;
    MaterialButton btnMoreNotification;
    //viewmodel
    NotificationViewModel notificationViewModel;
    //adapter
    NotificationAdapter notificationAdapter;
    public NotificationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
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
        View view =  inflater.inflate(R.layout.fragment_notification, container, false);

        //mapping data
        rcvNotification = view.findViewById(R.id.rcvNotification);
        notificationViewModel = new NotificationViewModel();
        btnMoreNotification = view.findViewById(R.id.btnMoreNoti);
        //adapter config
        adapterConfig();
        //show more data
        showMoreDataEvent();
        //observe data
        observeData();
        return view;
    }

    private void observeData() {
        notificationViewModel.getNotificationLiveData().observe(getViewLifecycleOwner(), new Observer<PageResponse<NotificationDTO>>() {
            @Override
            public void onChanged(PageResponse<NotificationDTO> notificationDTOPageResponse) {
                Log.d(TAG, "Data Noti");
                notificationAdapter.addData(notificationDTOPageResponse.getContent());
            }
        });
        notificationViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<Event<String>>() {
            @Override
            public void onChanged(Event<String> stringEvent) {
                String mess = stringEvent.getContent();
                if (mess != null){
                    Toast.makeText(getContext(), mess, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void adapterConfig(){
        notificationAdapter = new NotificationAdapter(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rcvNotification.setLayoutManager(linearLayoutManager);
        rcvNotification.setAdapter(notificationAdapter);
    }

    private void showMoreData(){
        notificationViewModel.getAllNotifications(currentPage);
        currentPage++;
    }
    private void showMoreDataEvent(){
        btnMoreNotification.setOnClickListener(v -> {
            showMoreData();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        currentPage = 0;
        notificationAdapter.clearData();
        showMoreData();
    }
}