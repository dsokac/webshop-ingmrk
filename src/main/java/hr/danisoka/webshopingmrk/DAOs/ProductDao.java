package hr.danisoka.webshopingmrk.DAOs;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;
import hr.danisoka.webshopingmrk.utils.models.ProductUtils;

@Service
public class ProductDao implements Dao<Product, Integer> {

	@Autowired 
	private ProductRepository productRepository;
	 
	public List<Product> getAll() {
		return productRepository.findAll();
	}
	
	
	@Override
	public Optional<Product> getById(Integer id) {
		Optional<Product> product = null;
		if(ProductUtils.doesIdExist(id, productRepository)) {
			product = productRepository.findById(id);
		}
		return product;
	}

	@Override
	public Product save(Product entity) throws IllegalArgumentException{
		Product stored = null;
		if(ProductUtils.validate(entity, productRepository, false)) {		
			stored = productRepository.save(entity);
		}
		return stored;
	}

	@Override
	public Product update(Product entity, Integer id) {
		Product updated = null;
		Optional<Product> old = null;
		if(ProductUtils.doesIdExist(id, productRepository)) {
			old = productRepository.findById(id);
			if(old != null && ProductUtils.isCodeUnique(old.get(), entity, productRepository)) {
				if(ProductUtils.validate(entity, productRepository, true)) {		
					old.get().updateWith(entity);
					updated = productRepository.save(old.get());
				}
			}
		}
		return updated;
	}

	@Override
	public Product deleteById(Integer id) {
		Optional<Product> product = null;
		if(ProductUtils.doesIdExist(id, productRepository)) {
			product = productRepository.findById(id);
			productRepository.delete(product.get());
		}
		return product.get();
	}

	@Override
	public void deleteAll() {
		productRepository.deleteAll();
	}
	
}
