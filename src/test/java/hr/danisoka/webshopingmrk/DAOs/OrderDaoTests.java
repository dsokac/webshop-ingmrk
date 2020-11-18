package hr.danisoka.webshopingmrk.DAOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.models.Order;
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
	
}
