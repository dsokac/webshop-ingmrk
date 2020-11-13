package hr.danisoka.webshopingmrk.utils.models;

import java.util.List;

import hr.danisoka.webshopingmrk.models.Customer;

public final class CustomerUtils {

	public Boolean isEmailUnique(List<Customer> customers, String email) {
		boolean outcome = true;
		for(Customer customer : customers) {
			if(customer.getEmail().equals(email)) {
				outcome = false;
				break;
			}
		}
		return outcome;
	}
	
}
