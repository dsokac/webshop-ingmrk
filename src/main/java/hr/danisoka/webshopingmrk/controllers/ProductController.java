package hr.danisoka.webshopingmrk.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.danisoka.webshopingmrk.WebshopErrorResponse;
import hr.danisoka.webshopingmrk.WebshopResponse;
import hr.danisoka.webshopingmrk.DAOs.ProductDao;
import hr.danisoka.webshopingmrk.models.Product;

@RestController @RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductDao productDao;
	
	@GetMapping
	public WebshopResponse getAllProducts() {
		return new WebshopResponse(productDao.getAll());
	}
	
	@GetMapping("/{id}")
	public WebshopResponse getProductById(@PathVariable int id) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(productDao.getById(id));
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/create")
	public WebshopResponse saveNewProduct(@RequestBody Product product) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(productDao.save(product));
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@PutMapping("/{id}/update")
	public WebshopResponse updateProduct(@PathVariable int id, @RequestBody Product product) {		
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(productDao.update(product, id));
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@DeleteMapping("/{id}")
	public WebshopResponse deleteProduct(@PathVariable Integer id) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(productDao.deleteById(id));
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@DeleteMapping
	public WebshopResponse deleteAllProducts() {
		productDao.deleteAll();
		return new WebshopResponse("All products are deleted");
	}
	
}
