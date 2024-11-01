package com.bitirmeprojesi.ecommerce.service.impl;

import com.bitirmeprojesi.ecommerce.model.Order;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.model.Transaction;
import com.bitirmeprojesi.ecommerce.repository.ISellerRepository;
import com.bitirmeprojesi.ecommerce.repository.ITransactionRepository;
import com.bitirmeprojesi.ecommerce.service.ITransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TransactionService implements ITransactionService {

    private final ITransactionRepository transactionRepository;
    private final ISellerRepository sellerRepository;

    @Override
    public Transaction createTransaction(Order order) {
        Seller seller = sellerRepository.findById(order.getSellerId()).get();

        Transaction transaction = new Transaction();
        transaction.setSeller(seller);
        transaction.setCustomer(order.getUser());
        transaction.setOrder(order);

        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsBySeller(Seller seller) {
        return transactionRepository.findBySellerId(seller.getId());
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
