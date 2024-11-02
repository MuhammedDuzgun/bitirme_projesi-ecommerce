package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Deal;

import java.util.List;

public interface IDealService {
    List<Deal> getDeals();
    Deal createDeal(Deal deal);
    Deal updateDeal(Long id, Deal deal);
    void deleteDeal(Long id);
}
