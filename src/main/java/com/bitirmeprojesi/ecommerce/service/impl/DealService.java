package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Deal;
import com.bitirmeprojesi.ecommerce.model.HomeCategory;
import com.bitirmeprojesi.ecommerce.repository.IDealRepository;
import com.bitirmeprojesi.ecommerce.repository.IHomeCategoryRepository;
import com.bitirmeprojesi.ecommerce.service.IDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class DealService implements IDealService {

    private final IDealRepository dealRepository;
    private final IHomeCategoryRepository homeCategoryRepository;

    @Override
    public List<Deal> getDeals() {
        return dealRepository.findAll();
    }

    @Override
    public Deal createDeal(Deal deal) {
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(() -> new RuntimeException
                        ("Home category not found with id: " + deal.getCategory().getId()));
        Deal newDeal = dealRepository.save(deal);
        newDeal.setCategory(homeCategory);
        newDeal.setDiscount(deal.getDiscount());

        return dealRepository.save(newDeal);
    }

    @Override
    public Deal updateDeal(Long id, Deal deal) {
        Deal dealToUpdate = dealRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Deal not found with id: " + id));
        HomeCategory homeCategory = homeCategoryRepository.findById(deal.getCategory().getId())
                .orElseThrow(()-> new RuntimeException
                        ("Home category not found with id: " + deal.getCategory().getId()));

        if (dealToUpdate != null) {
            if (deal.getDiscount() != null) {
                dealToUpdate.setDiscount(deal.getDiscount());
            }
            if (homeCategory != null) {
                dealToUpdate.setCategory(homeCategory);
            }
            return dealRepository.save(dealToUpdate);
        }
        throw new RuntimeException("Deal is null with id: " + id);
    }

    @Override
    public void deleteDeal(Long id) {
        Deal dealToDelete = dealRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Deal not found with id: " + id));
        dealRepository.delete(dealToDelete);
    }
}
