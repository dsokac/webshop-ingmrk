package hr.danisoka.webshopingmrk.utils.models;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public final class ProductUtilsTests {

	@MockBean
	private ProductRepository mockedProductRepository;
	
	@Test
	public void isCodeCorrect_CorrectCode_returnsTrue() {
		String code = "0000000000";
		
		assertTrue(ProductUtils.isCodeCorrect(code));
	}
	
	@Test
	public void isCodeCorrect_NullValue_returnsFalse() {
		assertFalse(ProductUtils.isCodeCorrect(null));
	}
	
	@Test
	public void isCodeCorrect_MoreCharacters_returnsFalse() {
		String code = "000000000000";
		
		assertFalse(ProductUtils.isCodeCorrect(code));
	}
	
	@Test
	public void isCodeCorrect_LessCharacters_returnsFalse() {
		String code = "00000000";
		
		assertFalse(ProductUtils.isCodeCorrect(code));
	}
	
	@Test
	public void doesIdExist_IdIsStoredInDb_returnsTrue() {
		int id = 1;
		Product p = new Product();
		p.setId(1);	
		
		when(mockedProductRepository.findById(id)).thenReturn(Optional.of(p));
		
		assertTrue(ProductUtils.doesIdExist(id, mockedProductRepository));
	}
	
	@Test
	public void doesIdExist_IdNotStoredInDb_throwsException() {
		int id = 99;
		
		when(mockedProductRepository.findById(id)).thenReturn(null);
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.doesIdExist(id, mockedProductRepository), "ID '" + id + "' does not exist!");
	}
	
	@Test
	public void isCodeUnique_TwoProductsWithSameCode_returnsTrue() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		Product p2 = new Product();
		p2.setCode("0000000001");
		p2.setAvailable(true);
		p2.setName("Product 1");
		p2.setPriceHrk(new BigDecimal(12.76));
		p2.setDescription("Description.");
		List<Product> products = new ArrayList<>();
		products.add(p2);
		
		when(mockedProductRepository.findByCode("0000000001")).thenReturn(products);
		
		assertTrue(ProductUtils.isCodeUnique(p, p2, mockedProductRepository));
	}
	
	@Test
	public void isCodeUnique_TwoProductsWithDifferentCodeAndCodeUnique_returnsTrue() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		Product p2 = new Product();
		p2.setCode("0000000011");
		p2.setAvailable(true);
		p2.setName("Product 1");
		p2.setPriceHrk(new BigDecimal(12.76));
		p2.setDescription("Description.");
		
		
		when(mockedProductRepository.findByCode("0000000011")).thenReturn(new ArrayList<Product>());
		
		assertTrue(ProductUtils.isCodeUnique(p, p2, mockedProductRepository));
	}
	
	@Test
	public void isCodeUnique_TwoProductsWithDifferentCodeAndCodeNotUnique_throwsException() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		Product p2 = new Product();
		p2.setCode("0000000011");
		p2.setAvailable(true);
		p2.setName("Product 1");
		p2.setPriceHrk(new BigDecimal(12.76));
		p2.setDescription("Description.");
		List<Product> products = new ArrayList<>();
		products.add(p2);
		
		when(mockedProductRepository.findByCode("0000000011")).thenReturn(products);
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.isCodeUnique(p, p2, mockedProductRepository), "The code must be unique for an item. The code '" + p2.getCode() + "' already exist.");
	}
	
	@Test
	public void validate_ProductContainsIdWhenCreating_throwsException() {
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		boolean isUpdating = false;
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, isUpdating), "The entity must not contain ID.");
	}
	
	@Test
	public void validate_ProductDoesNotContainIdWhenUpdating_throwsException() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		boolean isUpdating = true;
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, isUpdating), "The entity must contain ID.");
	}
	
	@Test
	public void validate_ProductDoesNotContainCode_throwsException() {
		Product p = new Product();
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, false), "The code is required and must be 10 characters long.");
	}
	
	@Test
	public void validate_ProductContainsIncorrectCode_throwsException() {
		Product p = new Product();
		p.setCode("00000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		boolean isUpdating = true;
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, isUpdating), "The code must be exactly 10 characters long.");
	}
	
	@Test
	public void validate_ProductContainsCodeWhichNotUnique_throwsException() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setName("Product 1");
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		Product p2 = new Product();
		p2.setCode("0000000001");
		p2.setAvailable(true);
		p2.setName("Product 1");
		p2.setPriceHrk(new BigDecimal(12.76));
		p2.setDescription("Description.");
		List<Product> products = new ArrayList<>();
		products.add(p2);
		
		when(mockedProductRepository.findByCode("0000000001")).thenReturn(products);
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, false), "The code must be unique for an item. The code '" + p.getCode() + "' already exist.");
	}
	
	@Test
	public void validate_ProductDoesNotContainName_throwsException() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setAvailable(true);
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, false), "The name is required.");
	}
	
	@Test
	public void validate_ProductPriceIsIncorrect_throwsException() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setName("Product 1");
		p.setAvailable(true);
		p.setPriceHrk(new BigDecimal(-12.76));
		p.setDescription("Description.");
		
		assertThrows(IllegalArgumentException.class, () -> ProductUtils.validate(p, mockedProductRepository, false), "The price must no be negative value.");
	}
		
	@Test
	public void validate_ProductIsCorrectWhenUpdating_returnsTrue() {
		Product p = new Product();
		p.setId(1);
		p.setCode("0000000001");
		p.setName("Product 1");
		p.setAvailable(true);
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		boolean isUpdating = true;
		
		when(mockedProductRepository.findByCode("0000000001")).thenReturn(new ArrayList<Product>());
		
		assertTrue(ProductUtils.validate(p, mockedProductRepository, isUpdating));
	}
	
	@Test
	public void validate_ProductIsCorrectWhenCreating_returnsTrue() {
		Product p = new Product();
		p.setCode("0000000001");
		p.setName("Product 1");
		p.setAvailable(true);
		p.setPriceHrk(new BigDecimal(12.76));
		p.setDescription("Description.");
		boolean isUpdating = false;
		
		when(mockedProductRepository.findByCode("0000000001")).thenReturn(new ArrayList<Product>());
		
		assertTrue(ProductUtils.validate(p, mockedProductRepository, isUpdating));
	}
	
}
