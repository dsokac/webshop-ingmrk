package hr.danisoka.webshopingmrk.DAOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
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

import hr.danisoka.webshopingmrk.models.CompleteOrder;
import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.models.Item;
import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.Order.Status;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.OrderRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest
public class OrderDaoTests {

	@MockBean
	private OrderRepository mockedOrderRepository;
	
	@MockBean
	private OrderItemDao mockedOrderItemDao;
	
	@MockBean
	private ProductDao mockedProductDao;
	
	@Autowired
	private OrderDao orderDao;
	
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
	public void getAll_NonEmptyList_ReturnsItems() {
		Order o1 = new Order(c);
		o1.setId(1);
		Order o2 = new Order(c);
		o2.setId(1);
		List<Order> orders = new ArrayList<Order>();
		orders.add(o1);
		orders.add(o2);
		
		when(mockedOrderRepository.findAll()).thenReturn(orders);
		
		assertEquals(orders, orderDao.getAll());
	}
	
	@Test
	public void save_OrderMissingCustomer_ThrowsException() {
		Order o1 = new Order();
				
		assertThrows(IllegalArgumentException.class, () -> orderDao.save(o1));
	}
	
	@Test
	public void save_CorrectOrder_ReturnsStoredOrder() throws Exception {
		Order o1 = new Order(c);
		
		when(mockedOrderRepository.save(o1)).thenReturn(o1);
		
		assertEquals(o1, orderDao.save(o1));
	}
	
	@Test
	public void update_IncorrectOrderId_ThrowsException() {
		List<Item> items = new ArrayList<Item>();
		Item i1 = new Item(p, 1);
		Item i2 = new Item(p2, 2);
		items.add(i1);
		items.add(i2);
		
		when(mockedOrderRepository.findById(99)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> orderDao.update(items, 99));
	}
	
	@Test
	public void update_CorrectOrderItems_ReturnUpdatedCompleteOrder() throws Exception {
		List<Item> items = new ArrayList<Item>();
		Item i1 = new Item(p, 1);
		Item i2 = new Item(p2, 2);
		items.add(i1);
		items.add(i2);
		
		CompleteOrder co = new CompleteOrder(o);
		List<OrderItem> oiList = Item.convertFrom(items, o);
		co.setItems(items);
		
		when(mockedOrderRepository.findById(1)).thenReturn(Optional.of(o));
		when(mockedOrderItemDao.saveAll(oiList)).thenReturn(oiList);
		
		CompleteOrder coUpdated = orderDao.update(items, 1);
		assertEquals(co.getOrder(), coUpdated.getOrder());
		assertEquals(co.getItems().size(), coUpdated.getItems().size());
		assertEquals(co.getItems().get(0).getProduct(), coUpdated.getItems().get(0).getProduct());
	}
	
	@Test
	public void deleteById_IncorrectId_ThrowsException() {
		when(mockedOrderRepository.findById(99)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> orderDao.deleteById(99));
	}
	
	@Test
	public void deleteById_CorrectOrder_ReturnsDeletedCompleteOrder() throws Exception {
		List<Item> items = new ArrayList<Item>();
		Item i1 = new Item(p, 1);
		Item i2 = new Item(p2, 2);
		items.add(i1);
		items.add(i2);
		
		CompleteOrder co = new CompleteOrder(o);
		List<OrderItem> oiList = Item.convertFrom(items, o);
		co.setItems(items);
		
		when(mockedOrderRepository.findById(1)).thenReturn(Optional.of(o));
		when(mockedOrderItemDao.deleteAllByOrder(o)).thenReturn(oiList);
		
		CompleteOrder deletedCo = orderDao.deleteById(1);
		assertEquals(co.getOrder(), deletedCo.getOrder());
		assertEquals(co.getItems().size(), deletedCo.getItems().size());
		assertEquals(co.getItems().get(0).getProduct(), deletedCo.getItems().get(0).getProduct());
	}
	
	@Test
	public void finalizeOrderById_FinalizingSubmittedOrder_ThrowsException() {
		Order submittedO = new Order(c);
		submittedO.setId(1);
		submittedO.setStatus(Status.SUBMITTED);
				
		when(mockedOrderRepository.findById(1)).thenReturn(Optional.of(submittedO));
		
		assertThrows(IllegalArgumentException.class, () -> orderDao.finalizeOrderById(1));
	}
	
	@Test
	public void finalizeOrderById_FinalizingDraftOrder_ReturnsSubmittedOrder() throws Exception {
		List<Item> items = new ArrayList<Item>();
		Item i1 = new Item(p, 1);
		Item i2 = new Item(p2, 2);
		items.add(i1);
		items.add(i2);
		
		CompleteOrder co = new CompleteOrder(o);
		List<OrderItem> oiList = Item.convertFrom(items, o);
		co.setItems(items);
		
		when(mockedOrderRepository.findById(1)).thenReturn(Optional.of(o));
		when(mockedOrderRepository.save(o)).thenReturn(o);
		when(mockedOrderItemDao.getAllByOrder(o)).thenReturn(oiList);
		
		CompleteOrder finalizedOrder = orderDao.finalizeOrderById(1);
		
		assertEquals(Status.SUBMITTED, finalizedOrder.getOrder().getStatus());
		assertNotEquals(0, finalizedOrder.getOrder().getTotalPriceEur());
		assertNotEquals(0, finalizedOrder.getOrder().getTotalPriceEur());
	}
	
}
