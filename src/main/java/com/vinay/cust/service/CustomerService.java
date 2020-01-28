package com.vinay.cust.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.vinay.cust.dto.CustomerDto;
import com.vinay.cust.model.Customer;
import com.vinay.cust.repository.CustomerRepository;

@Service
public class CustomerService {

	@Autowired
	private CustomerRepository customerRepository;

	public void saveCustomer(CustomerDto custdto) {
		Customer cust = new Customer();
		cust.setName(custdto.getName());
		cust.setPhonenumber(custdto.getPhonenumber());
		cust.setPincode(custdto.getPincode());
		cust.setPassword(custdto.getPassword());
		cust.setUsername(custdto.getUsername());
		customerRepository.save(cust);
	}

	public void updateCustomer(CustomerDto custdto) {
		Customer cust = new Customer();
		cust.setCustomerId(custdto.getCustomerId());
		cust.setName(custdto.getName());
		cust.setPhonenumber(custdto.getPhonenumber());
		cust.setPincode(custdto.getPincode());
		cust.setUsername(custdto.getUsername());
		customerRepository.save(cust);
	}

	public void deleteCustomer(String id) {
		Customer cust = new Customer();
		customerRepository.delete(cust);
	}

	public Optional<Customer> getCustomer(Integer id) {
		return customerRepository.findById(id);
	}

	public List<Customer> getAllCustomers() {
//		System.out.println("check" + customerRepository.findCustomerInfo());
		return (List<Customer>) customerRepository.findAll();
	}

	public void updateRefToken(String refToken, String userName) {
		Customer customer = customerRepository.findByUsername(userName);
		customer.setRefToken(refToken);
		customerRepository.save(customer);
	}

	public String getRefToken(String username) {
		Customer customer = customerRepository.findByUsername(username);// check
																		// for
																		// empty
		return customer.getRefToken();
	}
}
