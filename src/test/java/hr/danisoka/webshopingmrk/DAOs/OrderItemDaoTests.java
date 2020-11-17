package hr.danisoka.webshopingmrk.DAOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.OrderItemRepository;
import hr.danisoka.webshopingmrk.utils.models.OrderItemUtils;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderItemDaoTests {

	@MockBean
	private OrderItemRepository mockedOrderItemRepository;
	
	@Autowired
	private OrderItemDao orderItemDao;
	
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
	public void getAll_NonEmptyList_ReturnsNonEmptyList() {
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 2);
		oi1.setId(2);
		List<OrderItem> oiList = new ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
		
		when(mockedOrderItemRepository.findAll()).thenReturn(oiList);
		
		assertEquals(oiList, orderItemDao.getAll());
	}
	
	@Test
	public void getAll_EmptyList_ReturnsEmptyList() {		
		when(mockedOrderItemRepository.findAll()).thenReturn(new ArrayList<OrderItem>());
		
		assertEquals(new ArrayList<OrderItem>(), orderItemDao.getAll());
	}
	
	@Test
	public void getById_IdNotStored_ThrowsException() {
		int id = 99;
		
		when(mockedOrderItemRepository.existsById(id)).thenReturn(false);
		
		assertThrows(IllegalArgumentException.class, () -> orderItemDao.getById(id));
	}
	
	@Test
	public void getById_IdStored_ReturnsItem() throws Exception {
		int id = 1;
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
	
		when(mockedOrderItemRepository.existsById(id)).thenReturn(true);
		when(mockedOrderItemRepository.findById(id)).thenReturn(Optional.of(oi1));
		
		assertEquals(Optional.of(oi1), orderItemDao.getById(id));
	}
	
	@Test
	public void save_IncorrectItem_ThrowsException() {
		OrderItem oi1 = new OrderItem(o, p, -1);
		
	    when(OrderItemUtils.validate(oi1, mockedOrderItemRepository, false)).thenThrow(new IllegalArgumentException());
		
		assertThrows(IllegalArgumentException.class, () -> orderItemDao.save(oi1));
	}
	
	@Test
	public void save_CorrectItem_ReturnsStoredItem() throws Exception {
		OrderItem oi1 = new OrderItem(o, p, 2);
		
		when(mockedOrderItemRepository.save(oi1)).thenReturn(oi1);
		
		assertEquals(oi1, orderItemDao.save(oi1));
	}
	
	@Test
	public void update_IncorrectItem_ThrowsException() {
		int id = 1;
		OrderItem oi1 = new OrderItem(o, p, -2);
		oi1.setId(1);
		
		when(mockedOrderItemRepository.findById(id)).thenReturn(Optional.of(oi1));
		
		assertThrows(IllegalArgumentException.class, () -> orderItemDao.update(oi1, id));
	}
	
	@Test
	public void update_CorrectItem_ReturnsUpdatedItem() throws Exception {
		int id = 1;
		OrderItem oi1 = new OrderItem(o, p, 2);
		oi1.setId(1);
		OrderItem oiNew = new OrderItem(o, p, 5);
		oiNew.setId(1);
		
		when(mockedOrderItemRepository.existsById(id)).thenReturn(true);
		when(mockedOrderItemRepository.findById(id)).thenReturn(Optional.of(oi1));
		when(mockedOrderItemRepository.save(oi1)).thenReturn(oiNew);
		
		assertEquals(oiNew, orderItemDao.update(oi1, id));
	}
	
	
	@Test
	public void deleteById_IdNotStored_ThrowsException() {
		int id = 99;
		
		when(mockedOrderItemRepository.existsById(id)).thenReturn(false);
		
		assertThrows(IllegalArgumentException.class, () -> orderItemDao.deleteById(id));
	}
	
	@Test
	public void deleteById_IdStored_ReturnsDeletedItem() throws Exception {
		int id = 1;
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
	
		when(mockedOrderItemRepository.findById(id)).thenReturn(Optional.of(oi1));
		when(mockedOrderItemRepository.existsById(id)).thenReturn(true);
		
		assertEquals(oi1, orderItemDao.deleteById(id));
	}
	
	@Test
	public void deleteAllItems_NonEmptyList_ReturnsDeletedList() {
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 2);
		oi1.setId(2);
		List<OrderItem> oiList = new ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
				
		assertEquals(oiList, orderItemDao.deleteAllItems(oiList));
	}
	
	@Test
	public void deleteAllItems_ListIsNull_ReturnsEmptyList() {				
		assertEquals(new ArrayList<>(), orderItemDao.deleteAllItems(null));
	}
	
	@Test
	public void getAllByOrder_ProvidedOrder_ReturnsFoundItems() {
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 2);
		oi1.setId(2);
		List<OrderItem> oiList = new ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
		
		when(mockedOrderItemRepository.findByOrder(o)).thenReturn(oiList);
		
		assertEquals(oiList, orderItemDao.getAllByOrder(o));
	}
	
	@Test
	public void getAllByOrder_OrderIsNull_ThrowsException() {	
		assertThrows(IllegalArgumentException.class, () -> orderItemDao.getAllByOrder(null), "Cannot get items for the order of value NULL.");
	}
}
