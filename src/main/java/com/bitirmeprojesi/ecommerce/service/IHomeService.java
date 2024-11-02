package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Home;
import com.bitirmeprojesi.ecommerce.model.HomeCategory;

import java.util.List;

public interface IHomeService {
    Home createHomePageData(List<HomeCategory> allCategories);
}
