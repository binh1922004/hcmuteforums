package com.example.hcmuteforums.data.repository;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.hcmuteforums.data.remote.api.CategoryApi;
import com.example.hcmuteforums.data.remote.retrofit.LocalRetrofit;
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
    MutableLiveData<String> messageError = new MutableLiveData<>();
    MutableLiveData<Boolean> getError = new MutableLiveData<>();

    public CategoryRepository(){
        categoryApi = LocalRetrofit.getRetrofit().create(CategoryApi.class);
    }

    public static CategoryRepository getInstance(){
        if (instance == null)
            instance = new CategoryRepository();
        return instance;
    }

    public void getAllCategory(){
        categoryApi.getAllCategories().enqueue(new Callback<ApiResponse<List<Category>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Category>>> call, Response<ApiResponse<List<Category>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ApiResponse<List<Category>> apiRes = response.body();
                    if (apiRes.getCode() == 200){
                        categoryList.setValue(apiRes.getResult());
                    }
                    else{
                        getError.setValue(true);
                    }
                }
                else{
                    if (response.errorBody() != null) {
                        Gson gson = new Gson();
                        ApiErrorResponse apiError = gson.fromJson(response.errorBody().charStream(),
                                ApiErrorResponse.class);
                        messageError.setValue(apiError.getMessage());
                    }
                    else {
                        getError.setValue(true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Category>>> call, Throwable throwable) {
                Log.d("Error Get Category", throwable.getMessage());
                getError.setValue(true);
            }
        });
    }

    public MutableLiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public MutableLiveData<Boolean> getGetError() {
        return getError;
    }
}
