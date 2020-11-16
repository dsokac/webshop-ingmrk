package hr.danisoka.webshopingmrk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.danisoka.webshopingmrk.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{
	List<Customer> findByEmail(String email);
}
