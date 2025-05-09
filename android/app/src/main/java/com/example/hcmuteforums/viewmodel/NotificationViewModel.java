package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.NotificationRepository;
import com.example.hcmuteforums.data.repository.ReplyRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.dto.NotificationDTO;
import com.example.hcmuteforums.model.dto.PageResponse;
import com.example.hcmuteforums.model.dto.response.ReplyResponse;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationViewModel extends ViewModel {
    private NotificationRepository notificationRepository;

    private MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    private MutableLiveData<PageResponse<NotificationDTO>> notificationLiveData = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> notificationError = new MutableLiveData<>();

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<PageResponse<NotificationDTO>> getNotificationLiveData() {
        return notificationLiveData;
    }

    public MutableLiveData<Event<Boolean>> getNotificationError() {
        return notificationError;
    }

    public NotificationViewModel() {
        notificationRepository = NotificationRepository.getInstance();
    }

    public void getAllNotifications(int page) {
        notificationRepository.getAllNotifications(page, new Callback<ApiResponse<PageResponse<NotificationDTO>>>() {
            @Override
            public void onResponse(Call<ApiResponse<PageResponse<NotificationDTO>>> call, Response<ApiResponse<PageResponse<NotificationDTO>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<PageResponse<NotificationDTO>> apiRes = response.body();
                    if (apiRes.getResult() != null) {
                        notificationLiveData.setValue(apiRes.getResult());  // ✅ Không dùng Event
                    } else {
                        notificationError.setValue(new Event<>(true));       // ✅ Dùng Event
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage()));  // ✅ Dùng Event
                    } else {
                        notificationError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<PageResponse<NotificationDTO>>> call, Throwable throwable) {
                Log.d("Error Noti", throwable.getMessage());
                notificationError.setValue(new Event<>(true));
            }

        });
    }
}
