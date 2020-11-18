package hr.danisoka.webshopingmrk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hr.danisoka.webshopingmrk.models.Order;

public interface OrderRepository extends JpaRepository<Order, Integer>{

}
