package com.example.hcmuteforums.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.example.hcmuteforums.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String keyword;


    private ImageView imgSearch;
    private EditText etSearch;
    private FrameLayout fragmentContainer;


    public SearchFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static SearchFragment newInstance(String keyword) {
        SearchFragment fragment = new SearchFragment();
        Bundle args = new Bundle();
        args.putString("keyword", keyword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            keyword = getArguments().getString("keyword");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        //mapping data
        etSearch = view.findViewById(R.id.etSearch);
        imgSearch = view.findViewById(R.id.imgSearch);
        fragmentContainer = view.findViewById(R.id.fragmentContainer);

        //init data
        initData();
        //set up fragment
        topicFragmentConfig(keyword);
        return view;
    }

    private void initData() {
        etSearch.setText(keyword);
        imgSearch.setOnClickListener(v -> {
            if (!etSearch.getText().toString().isEmpty())
                topicFragmentConfig(etSearch.getText().toString());
        });
    }

    private void topicFragmentConfig(String keyword) {
        TopicFragment topicFragment;
        topicFragment = TopicFragment.newInstance(null, keyword);
        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, topicFragment)
                .commit();
    }
}