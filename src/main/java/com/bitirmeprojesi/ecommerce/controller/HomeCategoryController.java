package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.model.Home;
import com.bitirmeprojesi.ecommerce.model.HomeCategory;
import com.bitirmeprojesi.ecommerce.service.IHomeCategoryService;
import com.bitirmeprojesi.ecommerce.service.IHomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class HomeCategoryController {

    private final IHomeCategoryService homeCategoryService;
    private final IHomeService homeService;

    @PostMapping("/home/categories")
    public ResponseEntity<Home> createHomeCategories(@RequestBody List<HomeCategory> homeCategories) {
        List<HomeCategory> categories = homeCategoryService.createCategories(homeCategories);
        Home home = homeService.createHomePageData(categories);
        return new ResponseEntity<>(home, HttpStatus.CREATED);
    }

    @GetMapping("/admin/home-category")
    public ResponseEntity<List<HomeCategory>> getHomeCategories() {
        List<HomeCategory> categories = homeCategoryService.getAllHomeCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    @PatchMapping("/admin/home-category/{id}")
    public ResponseEntity<HomeCategory> updateHomeCategory(@PathVariable("id") Long id,
                                                           @RequestBody HomeCategory homeCategory) {
        HomeCategory updatedHomeCategory = homeCategoryService.updateHomeCategory(id, homeCategory);
        return ResponseEntity.ok(updatedHomeCategory);
    }

}
