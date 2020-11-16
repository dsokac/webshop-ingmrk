package hr.danisoka.webshopingmrk.DAOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.repositories.CustomerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CustomerDaoTests {

	@MockBean
	private CustomerRepository mockedCustomerRepository;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Test
	public void getAll_EmptyList_ReturnsEmptyList() {
		when(mockedCustomerRepository.findAll()).thenReturn(new ArrayList<Customer>());
		
		assertEquals(new ArrayList<Customer>(), customerDao.getAll());
	}
	
	@Test
	public void getAll_NonEmptyList_ReturnNonEmptyList() {
		Customer c1 = new Customer("first", "last", "email@ex.com");
		c1.setId(1);
		Customer c2 = new Customer("first", "last", "email@ex.com");
		c2.setId(2);
		List<Customer> customers = new ArrayList<>();
		customers.add(c1);
		customers.add(c2);
		
		when(mockedCustomerRepository.findAll()).thenReturn(customers);
		
		assertEquals(customers, customerDao.getAll());
	}
	
	@Test
	public void getById_IdNotStored_ThrowsException() {
		when(mockedCustomerRepository.findById(99)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> customerDao.getById(99));
	}
	
	@Test
	public void getById_IdStored_ReturnsCustomer() throws Exception {
		int id = 1;
		Customer c1 = new Customer("first", "last", "email@ex.com");
		c1.setId(id);
		
		when(mockedCustomerRepository.findById(id)).thenReturn(Optional.of(c1));
		
		assertEquals(Optional.of(c1), customerDao.getById(id));
	}
	
	@Test
	public void save_IncorrectCustomer_ThrowsException() {
		Customer c1 = new Customer(null, "last", "email@ex.com");
		
		when(mockedCustomerRepository.save(c1)).thenReturn(c1);
		
		assertThrows(IllegalArgumentException.class, () -> customerDao.save(c1));
	}
	
	@Test
	public void save_CorrectCustomer_ReturnsSavedCustomer() throws Exception {
		Customer c1 = new Customer("first", "last", "email@ex.com");
		
		when(mockedCustomerRepository.save(c1)).thenReturn(c1);
		
		assertEquals(c1, customerDao.save(c1));
		
	}
	
	@Test
	public void update_IncorrectCustomer_ThrowsException() {
		Customer cOld = new Customer("first", "last", "email@ex.com");
		cOld.setId(1);
		Customer cNew = new Customer("first", "last", "email@ex.com");
		
		when(mockedCustomerRepository.findById(1)).thenReturn(Optional.of(cOld));
		when(mockedCustomerRepository.save(cOld)).thenReturn(cNew);
		
		assertThrows(IllegalArgumentException.class, () -> customerDao.update(cNew, 1));
	}
	
	@Test
	public void update_CorrectCustomer_ReturnsUpdatedCustomer() throws Exception {
		Customer cOld = new Customer("first", "last", "email@ex.com");
		cOld.setId(1);
		Customer cNew = new Customer("first", "last", "newmail@ex.com");
		cNew.setId(1);
		
		when(mockedCustomerRepository.findById(1)).thenReturn(Optional.of(cOld));
		when(mockedCustomerRepository.save(cOld)).thenReturn(cNew);
		
		assertEquals(cNew, customerDao.update(cNew, 1));
	}
	
	@Test
	public void deleteById_IncorrectId_ThrowsException() {
		when(mockedCustomerRepository.findById(99)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> customerDao.deleteById(99));
	}
	
	@Test
	public void deleteById_CorrectId_ReturnsDeletedCustomer() throws Exception {
		Customer cOld = new Customer("first", "last", "email@ex.com");
		cOld.setId(1);
		
		when(mockedCustomerRepository.findById(1)).thenReturn(Optional.of(cOld));
		
		assertEquals(cOld, customerDao.deleteById(1));
	}
	
}
