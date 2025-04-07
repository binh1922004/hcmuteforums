package com.example.hcmuteforums.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hcmuteforums.data.repository.CategoryRepository;
import com.example.hcmuteforums.event.Event;
import com.example.hcmuteforums.model.entity.Category;

import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final CategoryRepository categoryRepository;

    public CategoryViewModel() {
        categoryRepository = CategoryRepository.getInstance();
    }

    public LiveData<List<Category>> getCategoryList() {
        return categoryRepository.getCategoryList();
    }

    public LiveData<Event<String>> getMessageError() {
        return categoryRepository.getMessageError();
    }

    public LiveData<Event<Boolean>> getGetError() {
        return categoryRepository.getGetError();
    }

    public void fetchCategories() {
        categoryRepository.getAllCategory();
    }
}
