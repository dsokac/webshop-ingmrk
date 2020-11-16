package hr.danisoka.webshopingmrk.DAOs;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class ProductDaoTests {

	@MockBean
	private ProductRepository mockedProductRepository;
	
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void getAll_NonEmptyListIsReturned_ListNotEmpy() {
		Product p1 = new Product();
		p1.setId(1);	
		Product p2 = new Product();
		p2.setId(2);	
		List<Product> products = new ArrayList<>();
		products.add(p1);
		products.add(p2);
		
		when(mockedProductRepository.findAll()).thenReturn(products);
		
		assertEquals(products, productDao.getAll());
	}
	
	@Test
	public void getAll_NoProductsIsPresent_ListEmpty() {		
		when(mockedProductRepository.findAll()).thenReturn(new ArrayList<Product>());
		
		assertEquals(new ArrayList<Product>(), productDao.getAll());
	}
	
	@Test
	public void getById_IdNotStoredInDb_ThrowsException() {
		int id = 99;
		
		when(mockedProductRepository.findById(id)).thenReturn(null);

		assertThrows(IllegalArgumentException.class, () -> productDao.getById(id));
	}
	
	@Test
	public void getById_IdIsStoredInDb_ReturnsProduct() {
		Product p = new Product();
		p.setId(1);	
		int id = 1;
		
		when(mockedProductRepository.findById(1)).thenReturn(Optional.of(p));
		
		assertEquals(Optional.of(p), productDao.getById(id));
	}
	
	@Test
	public void save_ProductIsIncorrect_ThrowsException() {
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		when(mockedProductRepository.save(p)).thenReturn(p);
		
		assertThrows(IllegalArgumentException.class, () -> productDao.save(p));
	}
	
	@Test
	public void save_ProductCorrect_storesProduct() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(10.12));
		p.setDescription("Description.");
		
		when(mockedProductRepository.save(p)).thenReturn(p);
		
		assertEquals(p, productDao.save(p));
	}
	
	@Test
	public void update_ProductIsIncorrect_throwsException() {
		int id = 1;
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(10.12));
		p.setDescription("Description.");
		
		Product pNew = new Product();
		pNew.setId(1);
		pNew.setCode("0000000001");
		pNew.setAvailable(true);
		pNew.setPriceHrk(new BigDecimal(10.12));
		pNew.setDescription("Description.");
		
		when(mockedProductRepository.findById(id)).thenReturn(Optional.of(p));
		
		assertThrows(IllegalArgumentException.class, () -> productDao.update(pNew, id));
	}
	
	@Test
	public void update_ProductIsCorrect_returnsUpdatedProduct() {
		int id = 1;
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(10.12));
		p.setDescription("Description.");
		
		Product pNew = new Product();
		pNew.setId(1);
		pNew.setCode("0000000001");
		pNew.setName("Product 11");
		pNew.setAvailable(true);
		pNew.setPriceHrk(new BigDecimal(10.12));
		pNew.setDescription("Description.");
		
		when(mockedProductRepository.findById(id)).thenReturn(Optional.of(p));
		when(mockedProductRepository.save(pNew)).thenReturn(pNew);
		
		assertEquals(pNew, productDao.update(pNew, id));
	}
	
	@Test
	public void deleteById_IdNotStoredInDb_throwsException() {
		int id = 99;
		
		when(mockedProductRepository.findById(id)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> productDao.deleteById(id));
	}
	
	@Test
	public void deleteById_IdCorrect_returnsDeletedItem() {
		int id = 1;
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(10.12));
		p.setDescription("Description.");
		
		when(mockedProductRepository.findById(id)).thenReturn(Optional.of(p));
		
		assertEquals(p, productDao.deleteById(id));
	}
	
}
