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

import hr.danisoka.webshopingmrk.WebshopResponse;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;

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
		return new WebshopResponse(productRepository.findById(id));
	}
	
	@PostMapping("/create")
	public WebshopResponse saveNewPorduct(@RequestBody Product product) {
		WebshopResponse response = new WebshopResponse(productRepository.save(product));
		return response;
	}
	
	@PutMapping("/{id}/update")
	public WebshopResponse updateProduct(@PathVariable int id, @RequestBody Product product) {
		Product old = productRepository.getOne(id);
		old.updateWith(product);
		return new WebshopResponse(productRepository.save(old));
	}
	
	@DeleteMapping("/{id}")
	public WebshopResponse deleteProduct(@PathVariable Integer id) {
		Product product = productRepository.getOne(id);
		productRepository.delete(product);
		return new WebshopResponse(product);
	}
	
	@DeleteMapping
	public WebshopResponse deleteAllProducts() {
		productRepository.deleteAll();
		return new WebshopResponse("All products are deleted");
	}
	
}
