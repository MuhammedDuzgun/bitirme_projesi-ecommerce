package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Deal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDealRepository extends JpaRepository<Deal, Long> {
}
