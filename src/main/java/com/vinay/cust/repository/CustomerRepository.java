package com.vinay.cust.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.vinay.cust.model.Customer;

@Repository
public interface CustomerRepository extends MongoRepository<Customer,Integer> {

	Customer findByid(String id);
	Customer findByCustomerId(long id);

}
