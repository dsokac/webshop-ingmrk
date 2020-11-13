package hr.danisoka.webshopingmrk.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.danisoka.webshopingmrk.models.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{

}
