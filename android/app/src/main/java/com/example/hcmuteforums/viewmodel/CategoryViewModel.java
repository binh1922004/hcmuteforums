package com.example.hcmuteforums.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.CategoryRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.entity.Category;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    MutableLiveData<Event<Boolean>> getError = new MutableLiveData<>();

    public CategoryViewModel() {
        categoryRepository = CategoryRepository.getInstance();
    }

    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public MutableLiveData<Event<String>> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Event<Boolean>> getGetError() {
        return getError;
    }

    public void fetchCategories() {
        categoryRepository.getAllCategory(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Category>> apiRes = response.body();
                    if (apiRes.getCode() == 200) {
                        categoryList.setValue(apiRes.getResult()); // ❗️Lỗi bạn quên từ khóa `Event<>`
                    } else {
                        getError.setValue(new Event<>(true));
                    }
                } else {
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(), ApiErrorResponse.class);
                        messageError.setValue(new Event<>(apiError.getMessage())); // ❗️Gói lỗi trong `Event<>`
                    } else {
                        getError.setValue(new Event<>(true));
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.d("Error Get Category", throwable.getMessage());
                getError.setValue(new Event<>(true));
            }
        });
    }
}
