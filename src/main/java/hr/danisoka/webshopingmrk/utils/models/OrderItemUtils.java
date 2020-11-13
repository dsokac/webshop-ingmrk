package hr.danisoka.webshopingmrk.utils.models;

import java.util.List;

import hr.danisoka.webshopingmrk.models.OrderItem;

public final class OrderItemUtils {

	public static Boolean isQuantityCorrect(OrderItem oi) {
		return oi.getQuantity() >= 0;
	}
	
	public static Boolean isQuantityCorrect(int quantity) {
		return quantity >= 0;
	}
	
	public static Boolean isUnique(OrderItem item, List<OrderItem> items) {
		boolean outcome =  true;
		for(OrderItem elem : items) {
			if(item.getOrder().equals(elem.getOrder()) && item.getProduct().equals(elem.getProduct())) {
				outcome = false;
				break;
			}
		}
		return outcome;
	}
	
}
