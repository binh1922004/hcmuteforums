package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.TopicRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.response.TopicDetailResponse;

import java.util.List;

public class TopicViewModel extends ViewModel {
    private TopicRepository topicRepository;

    public TopicViewModel() {
        topicRepository = TopicRepository.getInstance();
    }

    public LiveData<List<TopicDetailResponse>> getTopicsLiveData() {
        return topicRepository.getTopicsLiveData();
    }

    public LiveData<Event<Boolean>> getTopicError() {
        return topicRepository.getTopicError();
    }

    public LiveData<Event<String>> getMessageError() {
        return topicRepository.getMessageError();
    }

    public void fetchAllTopics() {
        topicRepository.getAllTopics();
    }
}
