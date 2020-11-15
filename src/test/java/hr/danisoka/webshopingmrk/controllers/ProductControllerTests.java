package hr.danisoka.webshopingmrk.controllers;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import hr.danisoka.webshopingmrk.WebshopIngmrkApplication;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;

@TestInstance(Lifecycle.PER_CLASS)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK, classes = { WebshopIngmrkApplication.class })
public class ProductControllerTests {

	
	private MockMvc mockMvc;
	private Gson gson = new GsonBuilder().serializeNulls().create();
	
	@Autowired
	private WebApplicationContext webApplicationContext;
	
	@MockBean
	private ProductRepository mockedProductRepo;
	
	private final String PRODUCTS_API = "/api/v1/products";
	
	@BeforeAll
	private  void setUp() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
	}
	
	@Test
	public void should_getAccounts_when_validRequest() throws Exception {
		
		Product p1 = new Product();
		p1.setId(1);
		
		Product p2 = new Product();
		p2.setId(2);
		
		List<Product> products = new ArrayList<>();
		products.add(p1);
		products.add(p2);
		
		when(mockedProductRepo.findAll()).thenReturn(products);
		
		String target = "$.data";
		mockMvc.perform(get(PRODUCTS_API))
				.andExpect(jsonPath(target).exists())
				.andExpect(jsonPath(target).isArray())
				.andExpect(jsonPath(target).isNotEmpty())
				.andExpect(jsonPath("$.length()").value(products.size()));
	}
	
	@Test
	public void should_getAccountById_when_validRequest() throws Exception {
		
		Product p1 = new Product();
		p1.setId(1);
		
		Optional<Product> opt = Optional.of(p1);
		
		when(mockedProductRepo.findById(1)).thenReturn(opt);
		
		String target = "$.data.id";
		mockMvc.perform(get(PRODUCTS_API + "/1"))
				.andExpect(jsonPath(target).exists())
				.andExpect(jsonPath(target).isNumber())
				.andExpect(jsonPath(target).value(1));
	}
	
	@Test
	public void should_createAccount_when_validRequest() throws Exception {
		Product p = new Product();
		p.setCode("0000000011");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		when(mockedProductRepo.save(p)).thenReturn(p);
		
		String target = "$.data";
		mockMvc.perform(post(PRODUCTS_API + "/create")
				.content(gson.toJson(p))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath(target + ".code").value("0000000011"))
			.andExpect(jsonPath(target + ".available").value(true))
			.andExpect(jsonPath(target + ".name").value("Product 1"))
			.andExpect(jsonPath(target + ".priceHrk").value(12.76))
			.andExpect(jsonPath(target + ".description").value("Description."));
	}
	
	@Test
	public void should_updateAccount_when_validRequest() throws Exception {
		Product pOld = new Product();
		pOld.setId(1);
		pOld.setCode("0000000001");
		pOld.setAvailable(true);
		pOld.setName("Product 1");
		pOld.setPriceHrk(new BigDecimal(12.76));
		pOld.setDescription("Description.");
		
		Product pNew = new Product();
		pNew.setId(1);
		pNew.setCode("0000000011");
		pNew.setAvailable(false);
		pNew.setName("Product 11");
		pNew.setPriceHrk(new BigDecimal(12.00));
		pNew.setDescription("Description new.");
		
		when(mockedProductRepo.save(pNew)).thenReturn(pNew);
		when(mockedProductRepo.getOne(pOld.getId())).thenReturn(pOld);
		
		String target = "$.data";
		mockMvc.perform(put(PRODUCTS_API + "/1/update")
				.content(gson.toJson(pNew))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath(target + ".id").exists())
			.andExpect(jsonPath(target + ".id").value(1))
			.andExpect(jsonPath(target + ".code").value("0000000011"))
			.andExpect(jsonPath(target + ".available").value(false))
			.andExpect(jsonPath(target + ".name").value("Product 11"))
			.andExpect(jsonPath(target + ".priceHrk").value(12.00))
			.andExpect(jsonPath(target + ".description").value("Description new."));
	}
	
	@Test
	public void should_deleteAccountById_when_validRequest() throws Exception {
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		when(mockedProductRepo.getOne(p.getId())).thenReturn(p);
		
		String target = "$.data";
		mockMvc.perform(delete(PRODUCTS_API + "/1")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath(target + ".id").exists())
			.andExpect(jsonPath(target + ".id").value(1))
			.andExpect(jsonPath(target + ".code").value("0000000001"))
			.andExpect(jsonPath(target + ".available").value(true))
			.andExpect(jsonPath(target + ".name").value("Product 1"))
			.andExpect(jsonPath(target + ".priceHrk").value(12.76))
			.andExpect(jsonPath(target + ".description").value("Description."));
	}
	
	@Test
	public void should_deleteAllAccounts_when_validRequest() throws Exception {
		String target = "$.data";
		mockMvc.perform(delete(PRODUCTS_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
			)
			.andExpect(jsonPath(target).value("All products are deleted"));
	}
	
	
}
