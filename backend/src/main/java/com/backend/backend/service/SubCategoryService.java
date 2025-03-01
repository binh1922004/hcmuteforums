package com.backend.backend.service;

import com.backend.backend.entity.Category;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.repository.CategoryRepository;
import com.backend.backend.repository.SubCategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SubCategoryService {
    SubCategoryRepository subCategoryRepository;

    public List<SubCategory> getSubcategoriesByCategoryId(String categoryId) {
        return subCategoryRepository.findSubCategoriesByCategory_Id(categoryId);
    }
}
