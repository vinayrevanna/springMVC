package com.vinay.cust.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinay.cust.dto.CustomerDto;
import com.vinay.cust.model.Customer;
import com.vinay.cust.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;
	
	public void saveCustomer(CustomerDto custdto){
		Customer cust = new Customer();
		cust.setName(custdto.getName());
		cust.setCustomerId(custdto.getCustomerId());
		cust.setAddress(custdto.getAddress());
		cust.setPhonenumber(custdto.getPhonenumber());
		cust.setPincode(custdto.getPincode());
		customerRepository.save(cust);
	}
	
	public void updateCustomer(CustomerDto custdto){
		Customer cust = new Customer();
		cust.setId(custdto.getId());
		cust.setCustomerId(custdto.getCustomerId());
		cust.setName(custdto.getName());
		cust.setAddress(custdto.getAddress());
		cust.setPhonenumber(custdto.getPhonenumber());
		cust.setPincode(custdto.getPincode());
		customerRepository.save(cust);
	}
	
	public void deleteCustomer(String id) {
		Customer cust = new Customer();
		cust.setId(id);
		customerRepository.delete(cust);
	}
	
	public Customer getCustomer(String id){
		return customerRepository.findByid(id);
	}
	
	public Customer findByCustomerId(long id){
		return customerRepository.findByCustomerId(id);
	}
}
