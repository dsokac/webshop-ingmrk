package hr.danisoka.webshopingmrk.utils.models;

import java.util.List;

import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.repositories.OrderItemRepository;

public final class OrderItemUtils {

	public static boolean isQuantityCorrect(int quantity) {
		return quantity > 0;
	}
	
	public static boolean isUnique(OrderItem item, OrderItemRepository orderItemRepository) {
		List<OrderItem> oi = orderItemRepository.findByOrderAndProduct(item.getOrder(), item.getProduct());
		if(!oi.isEmpty()) {
			throw new IllegalArgumentException("One product can be present only once in an order.");
		}
		return true;
	}
	
	public static boolean validate(OrderItem item, OrderItemRepository orderItemRepository, boolean isUpdating) {
		if((!isUpdating && !isUnique(item, orderItemRepository)) || isUpdating) {
			if(!isQuantityCorrect(item.getQuantity())) {
				throw new IllegalArgumentException("The item's quantity must be greater than 0.");
			}
		}
		return true;
	}
	
	public static boolean doesIdExists(Integer id, OrderItemRepository orderItemRepository) {
		if(id == null) {
			throw new IllegalArgumentException("The ID is required.");
		} else if(id != null && !orderItemRepository.existsById(id)) {
			throw new IllegalArgumentException("The order item's ID '" + id + "' does not exist.");
		}
		
		return true;
	}
	
}
