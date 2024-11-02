package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.HomeCategory;

import java.util.List;

public interface IHomeCategoryService {
    HomeCategory createHomeCategory(HomeCategory homeCategory);
    List<HomeCategory> createCategories(List<HomeCategory> homeCategories);
    HomeCategory updateHomeCategory(Long id, HomeCategory homeCategory);
    List<HomeCategory> getAllHomeCategories();
}