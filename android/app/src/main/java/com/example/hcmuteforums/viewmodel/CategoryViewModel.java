package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.CategoryRepository;
import com.example.hcmuteforums.model.entity.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;
    private final MutableLiveData<List<Category>> categoryList;
    private final MutableLiveData<String> messageError;
    private final MutableLiveData<Boolean> getError;

    public CategoryViewModel() {
        categoryRepository = CategoryRepository.getInstance();
        categoryList = categoryRepository.getCategoryList();
        messageError = categoryRepository.getMessageError();
        getError = categoryRepository.getGetError();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryList;
    }

    public LiveData<String> getMessageError() {
        return messageError;
    }

    public LiveData<Boolean> getGetError() {
        return getError;
    }

    public void fetchCategories() {
        categoryRepository.getAllCategory();
    }
}
