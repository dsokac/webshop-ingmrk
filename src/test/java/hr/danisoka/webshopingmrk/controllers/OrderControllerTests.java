package hr.danisoka.webshopingmrk.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hr.danisoka.webshopingmrk.WebshopIngmrkApplication;
import hr.danisoka.webshopingmrk.DAOs.OrderDao;
import hr.danisoka.webshopingmrk.DAOs.OrderItemDao;
import hr.danisoka.webshopingmrk.models.CompleteOrder;
import hr.danisoka.webshopingmrk.models.Customer;
import hr.danisoka.webshopingmrk.models.Item;
import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.OrderRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { WebshopIngmrkApplication.class })
public class OrderControllerTests {

	private MockMvc mockMvc;
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private OrderDao mockedOrderDao;
	
	@MockBean
	private OrderItemDao mockedOrderItemDao;
	
	@MockBean
	private OrderRepository mockedOrderRepository;
	
	private final String ORDERS_API = "/api/v1/orders";
	private Gson gson = new GsonBuilder().serializeNulls().create();
	private Customer customer;
	private Product p, p2;
	
	@BeforeAll
	private  void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
		customer = new Customer("FirstName", "LastName", "fullName@mail.com");		
		customer.setId(1);
		
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
	public void fetchAll_RequestValid_ReturnsAll() throws Exception {
		List<Order> orders = new ArrayList<>();		
		Order o1 = new Order(customer);
		Order o2 = new Order(customer);
		orders.addAll(Arrays.asList(o1, o2));
		
		when(mockedOrderDao.getAll()).thenReturn(orders);
		
		String target = "$.data";
		mockMvc.perform(get(ORDERS_API + "/read-order")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(target).exists())
		.andExpect(jsonPath(target).isArray())
		.andExpect(jsonPath(target).isNotEmpty())
		.andExpect(jsonPath("$.length()").value(orders.size()));
		
	}
	
	@Test
	public void fetchById_RequestedIdNotStored_ThrowsException() throws Exception {
		when(mockedOrderDao.getById(99)).thenThrow(new IllegalArgumentException());
		
		String targetType = "$.type";
		mockMvc.perform(get(ORDERS_API + "/read-order/99")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(targetType).value("error"));
	}
	
	@Test
	public void fetchById_RequestValid_ReturnsFullOrder() throws Exception {
		Order o = new Order(customer);
		o.setId(1);
		CompleteOrder co = new CompleteOrder(o);
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 3);
		oi2.setId(2);
		List<OrderItem> oiList = new  ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
		co.convertToLst(oiList);
				
		when(mockedOrderDao.getById(1)).thenReturn(Optional.of(co));
		
		String target = "$.data";
		mockMvc.perform(get(ORDERS_API + "/read-order/1")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(target).exists())
		.andExpect(jsonPath(target).isNotEmpty())
		.andExpect(jsonPath(target + ".order.id").value(1))
		.andExpect(jsonPath(target + ".items.length()").value(co.getItems().size()));
	}
	
	@Test
	public void createNewOrder_IncorrectOrder_ThrowsException() throws Exception  {
		Order o = new Order();
		o.setId(1);
		
		when(mockedOrderDao.save(o)).thenThrow(new IllegalArgumentException());
		
		String target = "$.type";
		mockMvc.perform(post(ORDERS_API + "/create-order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(o))
		)
		.andExpect(jsonPath(target).value("error"));
	}
	
	@Test
	public void createNewOrder_CorrectOrder_ReturnsCreatedOrder() throws Exception {
		Order o = new Order(customer);
		
		when(mockedOrderDao.save(o)).thenReturn(o);
		
		String target = "$.data";
		mockMvc.perform(post(ORDERS_API + "/create-order")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(o))
		)
		.andExpect(jsonPath(target).exists())
		.andExpect(jsonPath(target + ".status").value("DRAFT"));
	}
	
	//@Test
	public void updateOrder_IncorrectOneOrderItem_ThrowsException() throws Exception {
		List<Item> items = new ArrayList<>();
		Item i1 = new Item(1, p, -1);
		Item i2 = new Item(p, 3);
		items.addAll(Arrays.asList(i1, i2));
		
		when(mockedOrderDao.update(items, 1)).thenThrow(new IllegalArgumentException());
		
		String target = "$.type";
		ResultActions ra = mockMvc.perform(put(ORDERS_API + "/update-order/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(gson.toJson(items))
		);
		ra
		.andExpect(jsonPath(target).value("error"));
	}
	
	//@Test
	public void updateOrder_CorrectOneOrderItem_ReturnsCompleteOrder() throws Exception {
		Order o = new Order(customer);
		o.setId(1);
		CompleteOrder co = new CompleteOrder(o);
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 3);
		oi2.setId(2);
		List<OrderItem> oiList = new  ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
		co.convertToLst(oiList);
		
		when(mockedOrderDao.update(co.getItems(), 1)).thenReturn(co);
		
		String target = "$.data";
		mockMvc.perform(put(ORDERS_API + "/update-order/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(gson.toJson(co.getItems()))
		)
		.andExpect(jsonPath(target + ".order").exists())
		.andExpect(jsonPath(target + ".items").exists())
		.andExpect(jsonPath(target + ".items.length()").value(co.getItems().size()));
	}
	
	@Test
	public void deleteOrderById_CorrectId_ReturnsDeletedOrder() throws Exception {
		Order o = new Order(customer);
		o.setId(1);
		CompleteOrder co = new CompleteOrder(o);
		OrderItem oi1 = new OrderItem(o, p, 1);
		oi1.setId(1);
		OrderItem oi2 = new OrderItem(o, p2, 3);
		oi2.setId(2);
		List<OrderItem> oiList = new  ArrayList<>();
		oiList.addAll(Arrays.asList(oi1, oi2));
		co.convertToLst(oiList);
		
		when(mockedOrderDao.deleteById(1)).thenReturn(co);
		
		String target = "$.data";
		mockMvc.perform(delete(ORDERS_API + "/delete-order/1")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(target + ".order").exists())
		.andExpect(jsonPath(target + ".items").exists())
		.andExpect(jsonPath(target + ".items.length()").value(co.getItems().size()));
	}
	
	@Test
	public void deleteOrderById_IncorrectId_ReturnsDeletedOrder() throws Exception {		
		when(mockedOrderDao.deleteById(99)).thenThrow(new IllegalArgumentException());
		
		String target = "$.type";
		mockMvc.perform(delete(ORDERS_API + "/delete-order/99")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(target).value("error"));
	}
	
	@Test
	public void deleteAllOrders_ValidRequest_ReturnsMessage() throws Exception {		
		
		String target = "$.data";
		mockMvc.perform(delete(ORDERS_API + "/delete-order/")
				.contentType(MediaType.APPLICATION_JSON)
		)
		.andExpect(jsonPath(target).value("All orders and their items are deleted."));
	}
}
