package hr.danisoka.webshopingmrk.DAOs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.repositories.CustomerRepository;
import hr.danisoka.webshopingmrk.utils.models.CustomerUtils;

@Service
public class CustomerDao implements Dao<Customer, Integer>{

	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	public List<Customer> getAll() {
		return customerRepository.findAll();
	}

	@Override
	public Optional<Customer> getById(Integer id) throws Exception {
		Optional<Customer> customer = null;
		if(CustomerUtils.doesIdExists(id, customerRepository)) {
			customer = customerRepository.findById(id);
		}
		return customer;
	}

	@Override
	public Customer save(Customer entity) throws Exception {
		Customer stored = null;
		if(CustomerUtils.validate(entity, false)) {
			if(CustomerUtils.isEmailUnique(entity.getEmail(), customerRepository)) {
				stored = customerRepository.save(entity);
			}
		}
		return stored;
	}

	@Override
	public Customer update(Customer entity, Integer id) throws Exception {
		Customer updated = null;
		if(CustomerUtils.doesIdExists(id, customerRepository)) {
			Optional<Customer> old = customerRepository.findById(id);
			if(CustomerUtils.validate(entity, true)) {
				if(CustomerUtils.isEmailUnique(entity.getEmail(), customerRepository)) {
					old.get().updateWith(entity);
					updated = customerRepository.save(old.get());
				}
			}
		}
		return updated;
	}

	@Override
	public Customer deleteById(Integer id) throws Exception {
		Customer deleted = null;
		if(CustomerUtils.doesIdExists(id, customerRepository)) {
			Optional<Customer> old = customerRepository.findById(id);
			customerRepository.delete(old.get());
			deleted = old.get();
		}
		return deleted;
	}

	@Override
	public void deleteAll() {
		customerRepository.deleteAll();
	}

}
