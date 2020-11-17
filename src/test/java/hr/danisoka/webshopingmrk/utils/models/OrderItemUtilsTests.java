package hr.danisoka.webshopingmrk.utils.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.OrderItemRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderItemUtilsTests {

	@MockBean
	private OrderItemRepository mockedOrderItemRepository;
	
	private Customer c;
	private Order o;
	private Product p, p2;
	
	@BeforeAll
	private void setUp() {
		c = new Customer("first", "last", "mail@ex.com");
		c.setId(1);
		
		o = new Order(c);
		o.setId(1);
		
		p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		p2 = new Product();
		p2.setId(2);
		p2.setCode("0000000002");
		p2.setAvailable(true);
		p2.setName("Product 2");
		p2.setPriceHrk(new BigDecimal(12.76));
		p2.setDescription("Description.");
	}
	
	@Test
	public void isQuantityCorrect_QuantityNegative_ReturnsFalse() {
		int q = -1;
		assertFalse(OrderItemUtils.isQuantityCorrect(q));
	}
	
	@Test
	public void isQuantityCorrect_QuantityZero_ReturnsFalse() {
		int q = 0;
		assertFalse(OrderItemUtils.isQuantityCorrect(q));
	}
	
	@Test
	public void isQuantityCorrect_QuantityGreaterThanZero_ReturnsTrue() {
		int q = 4;
		assertTrue(OrderItemUtils.isQuantityCorrect(q));
	}
	
	@Test
	public void isUnique_ItemNotUnique_ThrowsException() {
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		List<OrderItem> oiList = new ArrayList<>();
		oiList.add(oi1);
		
		OrderItem oiNew = new OrderItem(o, p, 2);
		
		when(mockedOrderItemRepository.findByOrderAndProduct(o, p)).thenReturn(oiList);
		
		assertThrows(IllegalArgumentException.class, () -> OrderItemUtils.isUnique(oiNew, mockedOrderItemRepository), "One product can be present only once in an order.");	
	}
	
	@Test
	public void isUnique_ItemUnique_ReturnsTrue() {	
		OrderItem oiNew = new OrderItem(o, p2, 2);
		
		when(mockedOrderItemRepository.findByOrderAndProduct(o, p2)).thenReturn(new ArrayList<OrderItem>());
		
		assertTrue(OrderItemUtils.isUnique(oiNew, mockedOrderItemRepository));

	}
	
	@Test
	public void validate_IncorrectQuantity_ThrowsException() {
		OrderItem oiNew = new OrderItem(o, p2, -2);
		
		when(mockedOrderItemRepository.findByOrderAndProduct(o, p2)).thenReturn(new ArrayList<OrderItem>());

		assertThrows(IllegalArgumentException.class, () -> OrderItemUtils.validate(oiNew, mockedOrderItemRepository, true), "The item's quantity must be greater than 0.");	
	}
	
	@Test
	public void validate_CorrectItem_ReturnsTrue() {
		OrderItem oiNew = new OrderItem(o, p2, 2);
		
		when(mockedOrderItemRepository.findByOrderAndProduct(o, p2)).thenReturn(new ArrayList<OrderItem>());

		assertTrue(OrderItemUtils.validate(oiNew, mockedOrderItemRepository, false));
	}
	
	@Test
	public void doesIdExists_IdIsNull_ThrowsException() {
		assertThrows(IllegalArgumentException.class, () -> OrderItemUtils.doesIdExists(null, mockedOrderItemRepository), "The ID is required.");
	}
	
	@Test
	public void doesIdExists_IdIsNotStored_ThrowsException() {
		int id = 99;
		
		when(mockedOrderItemRepository.existsById(id)).thenReturn(false);
		
		assertThrows(IllegalArgumentException.class, () -> OrderItemUtils.doesIdExists(99, mockedOrderItemRepository), "The order item's ID '" + id + "' does not exist.");
	}
	
	@Test
	public void doesIdExists_IdIsStored_ReturnsTrue() {
		int id = 1;
		
		when(mockedOrderItemRepository.existsById(1)).thenReturn(true);
		
		assertTrue(OrderItemUtils.doesIdExists(id, mockedOrderItemRepository));
	}
}
