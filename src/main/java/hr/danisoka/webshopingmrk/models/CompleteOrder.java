package hr.danisoka.webshopingmrk.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CompleteOrder {

	private Order order;
	private List<Item> items = new ArrayList<>();
	
	public CompleteOrder() {}
	
	public CompleteOrder(Order order) {
		this.order = order;
	}
	
	public void addItem(Product product, int quantity) {
		this.items.add(new Item(product, quantity));
	}

	public void addItem(Item item) {
		this.items.add(item);
	}
	
	public void convertToLst(List<OrderItem> oiList) {
		this.items.clear();
		for(OrderItem el : oiList) {
			Item item = new Item(el.getProduct(), el.getQuantity());
			if(el.getId() != null) {
				item.setId(el.getId());
			}
			addItem(item);
		}
	}
	
	public static List<CompleteOrder> generateFrom(List<Order> orders) {
		List<CompleteOrder> cOrders = new ArrayList<>();
		for(Order order : orders) {
			cOrders.add(new CompleteOrder(order));
		}
		return cOrders;
	}
}
