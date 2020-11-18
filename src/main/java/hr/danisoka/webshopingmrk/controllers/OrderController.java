package hr.danisoka.webshopingmrk.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hr.danisoka.webshopingmrk.WebshopErrorResponse;
import hr.danisoka.webshopingmrk.WebshopResponse;
import hr.danisoka.webshopingmrk.DAOs.OrderDao;
import hr.danisoka.webshopingmrk.models.Item;
import hr.danisoka.webshopingmrk.models.Order;

@RestController @RequestMapping("/api/v1/orders")
public class OrderController {

	@Autowired
	private OrderDao orderDao;
	
	@GetMapping("/read-order")
	public WebshopResponse fetchAll() {
		return new WebshopResponse(orderDao.getAll());
	}
	
	@GetMapping("/read-order/{id}")
	public WebshopResponse fetchById(@PathVariable int id) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(orderDao.getById(id));
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
	@PostMapping("/create-order")
	public WebshopResponse createNewOrder(@RequestBody Order order) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(orderDao.save(order));
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
	@PutMapping("/update-order/{id}")
	public WebshopResponse updateOrder(@PathVariable int id, @RequestBody List<Item> items) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(orderDao.update(items, id));
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
	@DeleteMapping("/delete-order/{id}")
	public WebshopResponse deleteOrderById(@PathVariable int id) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(orderDao.deleteById(id));
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
	@DeleteMapping("/delete-order")
	public WebshopResponse deleteAllOrders() {
		WebshopResponse response = null;
		try {
			orderDao.deleteAll();
			response = new WebshopResponse("All orders and their items are deleted.");
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
	@GetMapping("/finalize-order/{id}")
	public WebshopResponse finalizeOrderById(@PathVariable int id) {
		WebshopResponse response = null;
		try {
			response = new WebshopResponse(orderDao.finalizeOrderById(id));
		} catch(Exception ex) {
			response = new WebshopErrorResponse(ex.getMessage());
		}
		return response;
	}
	
}
