package hr.danisoka.webshopingmrk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.danisoka.webshopingmrk.models.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {

	List<Product> findByCode(String code);
	
}
