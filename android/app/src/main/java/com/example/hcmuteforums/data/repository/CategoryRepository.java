package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.CategoryApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.dto.ApiErrorResponse;
import com.example.hcmuteforums.model.dto.ApiResponse;
import com.example.hcmuteforums.model.entity.Category;
import com.google.gson.Gson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    public static CategoryRepository instance;
    CategoryApi categoryApi;

    MutableLiveData<List<Category>> categoryList = new MutableLiveData<>();
    MutableLiveData<Event<String>> messageError = new MutableLiveData<>();
    MutableLiveData<Event<Boolean>> getError = new MutableLiveData<>();

    public CategoryRepository(){
        categoryApi = LocalRetrofit.getRetrofit().create(CategoryApi.class);
    }

    public static CategoryRepository getInstance(){
        if (instance == null)
            instance = new CategoryRepository();
        return instance;
    }

    public void getAllCategory(Callback<ApiResponse<List<Category>>> callback) {
        Call<ApiResponse<List<Category>>> call = categoryApi.getAllCategories();
        call.enqueue(callback);
    }

    public CategoryApi getCategoryApi() {
        return categoryApi;
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
}
