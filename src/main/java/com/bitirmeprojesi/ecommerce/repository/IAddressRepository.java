package com.bitirmeprojesi.ecommerce.repository;

import com.bitirmeprojesi.ecommerce.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IAddressRepository extends JpaRepository<Address, Long> {
}
