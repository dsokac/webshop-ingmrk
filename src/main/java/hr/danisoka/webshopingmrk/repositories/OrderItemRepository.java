package hr.danisoka.webshopingmrk.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer>{
	List<OrderItem> findByOrder(Order order);
	List<OrderItem> findByProduct(Product product);
	List<OrderItem> findByOrderAndProduct(Order order, Product product);
}
