package hr.danisoka.webshopingmrk.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Item {

	private Integer id;
	private Product product;
	private Integer quantity;
	
	public Item() {}
	
	public Item(Product product, int quantity) {
		this.product = product;
		this.quantity = quantity;
	}
	
	public Item(int id, Product product, int quantity) {
		this.id = id;
		this.product = product;
		this.quantity = quantity;
	}
	
	public static List<OrderItem> convertFrom(List<Item> items, Order o) {
		List<OrderItem> outcomeList = new ArrayList<OrderItem>();
		for(Item item : items) {
			outcomeList.add(new OrderItem(o, item.getProduct(), item.getQuantity()));
		}
		return outcomeList;
	}
}
