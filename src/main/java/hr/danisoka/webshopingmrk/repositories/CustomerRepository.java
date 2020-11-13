package hr.danisoka.webshopingmrk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.danisoka.webshopingmrk.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer>{

}
