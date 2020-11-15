package hr.danisoka.webshopingmrk.utils.models;

import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.ProductRepository;
import hr.danisoka.webshopingmrk.utils.GenericUtils;

public final class ProductUtils {
	
	public static boolean isCodeCorrect(String code) {
		return code.length() == 10;
	}	
	
	public static boolean doesIdExist(Integer id, ProductRepository productRepository) {
		if(productRepository.findById(id) == null) {
			throw new IllegalArgumentException("ID '" + id + "' does not exist!");
		}
		return true;
	}
	
	public static boolean isCodeUnique(Product pOld, Product pNew, ProductRepository productRepository) {
		if(!pOld.getCode().equals(pNew.getCode()) && !productRepository.findByCode(pNew.getCode()).isEmpty()) {
			throw new IllegalArgumentException("The code must be unique for an item. The code '" + pNew.getCode() + "' already exist.");
		}
		return true;
	}
	
	public static boolean validate(Product product, ProductRepository productRepository, boolean isUpdating) throws IllegalArgumentException {
		boolean valid = true;
		
		if(product.getId() != null && !isUpdating) {
			throw new IllegalArgumentException("The entity must not contain ID.");
		} else if(product.getId() == null && isUpdating) {
			throw new IllegalArgumentException("The entity must contain ID.");
		} else if(product.getCode() == null || product.getCode().isBlank()) {
			throw new IllegalArgumentException("The code is required and must be 10 characters long.");
		} else if(!ProductUtils.isCodeCorrect(product.getCode())) {
			throw new IllegalArgumentException("The code must be exactly 10 characters long.");
		} else if(!isUpdating && !productRepository.findByCode(product.getCode()).isEmpty()) {
			throw new IllegalArgumentException("The code must be unique for an item. The code '" + product.getCode() + "' already exist.");
		}else if(product.getName() == null || product.getName().isBlank()) {
			throw new IllegalArgumentException("The name is required.");
		} else if(!GenericUtils.isPriceCorrect(product.getPriceHrk())) {
			throw new IllegalArgumentException("The price must no be negative value.");
		}
		
		return valid;
	}
}
