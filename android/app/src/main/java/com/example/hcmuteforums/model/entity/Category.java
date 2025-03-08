package com.example.hcmuteforums.model.entity;

import java.util.List;

public class Category {
    private String id;
    private String name;
    private List<SubCategory> subCategories;

    public Category(String id, String name, List<SubCategory> subCategories) {
        this.id = id;
        this.name = name;
        this.subCategories = subCategories;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<SubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<SubCategory> subCategories) {
        this.subCategories = subCategories;
    }
}
