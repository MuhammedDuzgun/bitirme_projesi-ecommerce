package com.bitirmeprojesi.ecommerce.controller;

import com.bitirmeprojesi.ecommerce.model.Deal;
import com.bitirmeprojesi.ecommerce.response.ApiResponse;
import com.bitirmeprojesi.ecommerce.service.IDealService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/admin/deals")
public class DealController {

    private final IDealService dealService;

    @GetMapping("/get-all-deals")
    public ResponseEntity<List<Deal>> getAllDeals() {
        List<Deal> deals = dealService.getDeals();
        return ResponseEntity.ok(deals);
    }

    @PostMapping("/create")
    public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {
        Deal createdDeal = dealService.createDeal(deal);
        return ResponseEntity.ok(createdDeal);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<Deal> updateDeal(@PathVariable("id") Long id,
                                           @RequestBody Deal deal) {
        Deal updatedDeal = dealService.updateDeal(id, deal);
        return ResponseEntity.ok(updatedDeal);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse> deleteDeal(@PathVariable("id") Long id) {
        dealService.deleteDeal(id);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("Deal deleted with id: " + id);

        return ResponseEntity.ok(apiResponse);
    }

}
