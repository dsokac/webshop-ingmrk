package hr.danisoka.webshopingmrk.utils.models;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.repositories.CustomerRepository;

public final class CustomerUtils {

	public static boolean isEmailUnique(String email, CustomerRepository customerRepository) {
		if(email != null && !email.isBlank()) {
			for(Customer customer : customerRepository.findByEmail(email)) {
				if(customer.getEmail().equals(email)) {
					throw new IllegalArgumentException("The email '" + email + "' is already taken!");
				}
			}
			return true;
		} else {
			throw new IllegalArgumentException("The e-mail is required.");
		}
	}
	
	public static boolean doesIdExists(Integer id, CustomerRepository customerRepository) throws IllegalArgumentException {
		if(id == null) {
			throw new IllegalArgumentException("The customer ID is required.");
		} else if(id != null && customerRepository.findById(id) == null) {
			throw new IllegalArgumentException("The customer with ID '" + id + "' does not exist.");
		}
		return true;
	}
	
	
	public static boolean validate(Customer customer, boolean isUpdating) throws IllegalArgumentException {
		if(isUpdating && customer.getId() == null) {
			throw new IllegalArgumentException("The customer must have ID.");
		} else if(!isUpdating && customer.getId() != null) {
			throw new IllegalArgumentException("The customer must not have ID.");
		} else if(customer.getFirstName() == null || customer.getFirstName().isBlank()) {
			throw new IllegalArgumentException("The first name is required.");
		} else if(customer.getLastName() == null || customer.getLastName().isBlank()) {
			throw new IllegalArgumentException("The last name is required.");
		} else if(customer.getEmail() == null || customer.getEmail().isBlank()) {
			throw new IllegalArgumentException("The e-mail is required.");
		} else if(!customer.getEmail().contains("@") || !customer.getEmail().contains(".")) {
			throw new IllegalArgumentException("The e-mail has invalid format.");
		}
		return true;
	}
	
	
}
