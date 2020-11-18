package hr.danisoka.webshopingmrk.utils.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.repositories.OrderRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderUtilsTests {

	@MockBean
	private OrderRepository mockedOrderRepository;
	
	private Customer c;
	
	@BeforeAll
	private void setUp() {
		c = new Customer("first", "last", "mail@ex.com");
		c.setId(1);		
	}
	
	@Test
	public void isUpdateable_SubmittedOrder_ReturnsFalse() {
		Order o = new Order(c);
		o.setId(1);
		o.setStatus(Order.Status.SUBMITTED);
		
		assertFalse(OrderUtils.isUpdateable(o));
	}
	
	@Test
	public void isUpdateable_DraftOrder_ReturnsTrue() {
		Order o = new Order(c);
		o.setId(1);
		
		assertTrue(OrderUtils.isUpdateable(o));
	}
	
	@Test
	public void doesIdExists_IdIsNull_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.doesIdExists(null, mockedOrderRepository), "The order ID is required.");
	}
	
	@Test
	public void doesIdExists_IsNotStored_ThrowsException() {
		int id = 99;
		
		when(mockedOrderRepository.findById(99)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.doesIdExists(null, mockedOrderRepository), "The order with ID '" + id + "' does not exist.");
	}
	
	@Test
	public void doesIdExists_IdIsStored_ReturnsTrue() {
		int id = 1;
		Order o = new Order(c);
		o.setId(1);
		
		when(mockedOrderRepository.findById(1)).thenReturn(Optional.of(o));
		
		assertTrue(OrderUtils.doesIdExists(id, mockedOrderRepository));
	}
	
	@Test
	public void validate_IdIsNullWhenUpdating_ThrowsException() {
		Order o = new Order(c);
		boolean isUpdating = true;
		
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.validate(o, isUpdating), "The order ID is required.");
	}
	
	@Test
	public void validate_UpdatingSubmittedOrder_ThrowsException() {
		Order o = new Order(c);
		o.setId(1);
		o.setStatus(Order.Status.SUBMITTED);
		boolean isUpdating = true;
		
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.validate(o, isUpdating), "The submitted order cannot be updated.");
	}
	
	@Test
	public void validate_IdIsPresentWhenCreating_ThrowsException() {
		Order o = new Order(c);
		o.setId(1);
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.validate(o, isUpdating), "The order ID must not be present.");
	}
	
	@Test
	public void validate_CustomerIsMissing_ThrowsException() {
		Order o = new Order();
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> OrderUtils.validate(o, isUpdating), "The order must be assigned to a customer.");
	}
	
	@Test
	public void validate_CorrectOrder_ReturnsTrue() {
		Order o = new Order(c);
		boolean isUpdating = false;
		
		assertTrue(OrderUtils.validate(o, isUpdating));
	}
	
}
