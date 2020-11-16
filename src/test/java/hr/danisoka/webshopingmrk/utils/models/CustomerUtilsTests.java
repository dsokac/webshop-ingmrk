package hr.danisoka.webshopingmrk.utils.models;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.repositories.CustomerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerUtilsTests {

	@MockBean
	private CustomerRepository mockedCustomerRepository;
	
	@Test
	public void isEmailUnique_EmailUnique_ReturnsTrue() {
		String email = "example@ex.com";
		
		when(mockedCustomerRepository.findByEmail(email)).thenReturn(new ArrayList<Customer>());
		
		assertTrue(CustomerUtils.isEmailUnique(email, mockedCustomerRepository));
	}
	
	@Test
	public void isEmailUnique_EmailNotUnique_ThrowsException() {
		String email = "example@ex.com";
		Customer customer = new Customer("first", "last", email);
		List<Customer> customers = new ArrayList<>();
		customers.add(customer);
		
		when(mockedCustomerRepository.findByEmail(email)).thenReturn(customers);
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.isEmailUnique(email, mockedCustomerRepository), "The email '" + email + "' is already taken!");
	}
	
	@Test
	public void isEmailUnique_EmailNull_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.isEmailUnique(null, mockedCustomerRepository), "The e-mail is required.");
	}
	
	@Test
	public void doesIdExists_IdNotStored_ThrowsException() {
		int id = 99;
		
		when(mockedCustomerRepository.findById(id)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.doesIdExists(id, mockedCustomerRepository), "The customer with ID '" + id + "' does not exist.");
	}
	
	@Test
	public void doesIdExists_IdStored_ReturnsCustomer() {
		int id = 1;
		Customer customer = new Customer("first", "last", "mail@ex.com");
		customer.setId(1);
		
		when(mockedCustomerRepository.findById(id)).thenReturn(Optional.of(customer));

		assertTrue(CustomerUtils.doesIdExists(id, mockedCustomerRepository));
	}
	
	@Test
	public void doesIdExists_IdIsNull_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.doesIdExists(null, mockedCustomerRepository), "The customer ID is required.");
	}

	@Test
	public void validate_IdExistsWhenCreating_ThrowsException() {
		Customer customer = new Customer("first", "last", "mail@ex.com");
		customer.setId(1);
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The customer must not have ID.");
	}
	
	@Test
	public void validate_IdNotExistsWhenUpdating_ThrowsException() {
		Customer customer = new Customer("first", "last", "mail@ex.com");
		boolean isUpdating = true;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The customer must have ID.");
	}
	
	@Test
	public void validate_FirstNameNotExists_ThrowsException() {
		Customer customer = new Customer(null, "last", "mail@ex.com");
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The first name is required.");
	}
	
	@Test
	public void validate_LastNameNotExists_ThrowsException() {
		Customer customer = new Customer("first", null, "mail@ex.com");
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The last name is required.");
	}
	
	@Test
	public void validate_EmailNotExists_ThrowsException() {
		Customer customer = new Customer("first", "last", null);
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The e-mail is required.");
		
	}
	
	@Test
	public void validate_EmailIncorrectFormat_ThrowsException() {
		Customer customer = new Customer("first", "last", "mail");
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> CustomerUtils.validate(customer, isUpdating), "The e-mail has invalid format.");
	}
	
	@Test
	public void validate_CorrectCustomer_ReturnsTrue() {
		Customer customer = new Customer("first", "last", "mail@ex.com");
		boolean isUpdating = false;
		
		assertTrue(CustomerUtils.validate(customer, isUpdating));
	}
}
