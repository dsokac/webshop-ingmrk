package hr.danisoka.webshopingmrk.utils.models;

import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.repositories.OrderRepository;

public final class OrderUtils {

	public static boolean isUpdateable(Order order) {
		return order.getStatus().equals(Order.Status.DRAFT);
	}
	
	public static boolean doesIdExists(Integer id, OrderRepository orderRepository) throws IllegalArgumentException {
		if(id == null) {
			throw new IllegalArgumentException("The order ID is required.");
		} else if(id != null && orderRepository.findById(id) == null) {
			throw new IllegalArgumentException("The order with ID '" + id + "' does not exist.");
		}
		return true;
	}
	
	public static boolean validate(Order order, boolean isUpdating) throws IllegalArgumentException{
		if(isUpdating && order.getId() == null) {
			throw new IllegalArgumentException("The order ID is required.");
		} else if(isUpdating && order.getId() != null && !isUpdateable(order)) {
			throw new IllegalArgumentException("The submitted order cannot be updated.");
		} else if(!isUpdating && order.getId() != null) {
			throw new IllegalArgumentException("The order ID must not be present.");
		} else if(order.getCustomer() == null) {
			throw new IllegalArgumentException("The order must be assigned to a customer.");
		}
		return true;
	}
	
}
