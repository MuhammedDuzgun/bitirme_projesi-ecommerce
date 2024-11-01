package com.bitirmeprojesi.ecommerce.service;

import com.bitirmeprojesi.ecommerce.model.Order;
import com.bitirmeprojesi.ecommerce.model.Seller;
import com.bitirmeprojesi.ecommerce.model.Transaction;

import java.util.List;

public interface ITransactionService {

    Transaction createTransaction(Order order);
    List<Transaction> getTransactionsBySeller(Seller seller);
    List<Transaction> getAllTransactions();

}
