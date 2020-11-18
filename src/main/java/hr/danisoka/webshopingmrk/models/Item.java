package hr.danisoka.webshopingmrk.models;

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
}
