package hr.danisoka.webshopingmrk.controllers;

import java.util.Optional;

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
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;
import hr.danisoka.webshopingmrk.utils.models.ProductUtils;

@RestController @RequestMapping("/api/v1/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	
	@GetMapping
	public WebshopResponse getAllProducts() {
		return new WebshopResponse(productRepository.findAll());
	}
	
	@GetMapping("/{id}")
	public WebshopResponse getProductById(@PathVariable int id) {
		WebshopResponse response = null;
		try {
			Optional<Product> product = null;
			if(ProductUtils.doesIdExist(id, productRepository)) {
				product = productRepository.findById(id);
				return new WebshopResponse(product.get());
			}
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@PostMapping("/create")
	public WebshopResponse saveNewPorduct(@RequestBody Product product) {
		WebshopResponse response = null;
		try {
			if(ProductUtils.validate(product, productRepository, false)) {		
				response = new WebshopResponse(productRepository.save(product));
			}
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@PutMapping("/{id}/update")
	public WebshopResponse updateProduct(@PathVariable int id, @RequestBody Product product) {		
		WebshopResponse response = null;
		try {
			Optional<Product> old = null;
			if(ProductUtils.doesIdExist(id, productRepository)) {
				old = productRepository.findById(id);
				if(old != null && ProductUtils.isCodeUnique(old.get(), product, productRepository)) {
					if(ProductUtils.validate(product, productRepository, true)) {		
						old.get().updateWith(product);
						return new WebshopResponse(productRepository.save(old.get()));
					}
				}
			}
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@DeleteMapping("/{id}")
	public WebshopResponse deleteProduct(@PathVariable Integer id) {
		WebshopResponse response = null;
		try {
			Optional<Product> product = null;
			if(ProductUtils.doesIdExist(id, productRepository)) {
				product = productRepository.findById(id);
				productRepository.delete(product.get());
				return new WebshopResponse(product.get());
			}
		} catch (Exception e) {
			response = new WebshopErrorResponse(e.getMessage());
		}
		return response;
	}
	
	@DeleteMapping
	public WebshopResponse deleteAllProducts() {
		productRepository.deleteAll();
		return new WebshopResponse("All products are deleted");
	}
	
}
