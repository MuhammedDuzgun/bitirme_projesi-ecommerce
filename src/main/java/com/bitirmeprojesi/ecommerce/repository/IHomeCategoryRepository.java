package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.HomeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IHomeCategoryRepository extends JpaRepository<HomeCategory, Long> {
}
