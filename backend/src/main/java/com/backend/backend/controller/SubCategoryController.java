package com.backend.backend.controller;

import com.backend.backend.dto.ApiResponse;
import com.backend.backend.entity.Category;
import com.backend.backend.entity.SubCategory;
import com.backend.backend.service.CategoryService;
import com.backend.backend.service.SubCategoryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/subcategory")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SubCategoryController {
    SubCategoryService subCategoryService;

    @GetMapping("/{category_id}")
    public ApiResponse<List<SubCategory>> getSubCategoryByCategoryId(@PathVariable("category_id") String categoryId) {
        return ApiResponse.<List<SubCategory>>builder()
                .result(subCategoryService.getSubcategoriesByCategoryId(categoryId))
                .build();
    }

}
