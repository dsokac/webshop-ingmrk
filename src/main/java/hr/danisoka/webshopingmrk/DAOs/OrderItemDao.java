package hr.danisoka.webshopingmrk.DAOs;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.repositories.OrderItemRepository;
import hr.danisoka.webshopingmrk.utils.models.OrderItemUtils;

@Service
public class OrderItemDao implements Dao<OrderItem, Integer> {

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Override
	public List<OrderItem> getAll() {
		return orderItemRepository.findAll();
	}

	@Override
	public Optional<OrderItem> getById(Integer id) throws Exception {
		Optional<OrderItem> item = null;
		if(OrderItemUtils.doesIdExists(id, orderItemRepository)) {
			item = orderItemRepository.findById(id);
		}
		return item;
	}

	@Override
	public OrderItem save(OrderItem entity) throws Exception {
		OrderItem stored = null;
		if(OrderItemUtils.validate(entity, orderItemRepository, false)) {
			stored = orderItemRepository.save(entity);
		}
		return stored;
	}

	@Override
	public OrderItem update(OrderItem entity, Integer id) throws Exception {
		OrderItem updated = null;
		if(OrderItemUtils.doesIdExists(id, orderItemRepository)) {
			Optional<OrderItem> old = orderItemRepository.findById(id);
			if(OrderItemUtils.validate(entity, orderItemRepository, true)) {
				old.get().updateWith(entity.getQuantity());
				updated = orderItemRepository.save(old.get());
			}
		}
		return updated;
	}

	@Override
	public OrderItem deleteById(Integer id) throws Exception {
		OrderItem deleted = null;
		if(OrderItemUtils.doesIdExists(id, orderItemRepository)) {
			Optional<OrderItem> existing = orderItemRepository.findById(id);
			orderItemRepository.delete(existing.get());
			deleted = existing.get();
		}
		return deleted;
	}

	@Override
	public void deleteAll() {
		orderItemRepository.deleteAll();		
	}
	
	public List<OrderItem> deleteAllItems(List<OrderItem> items) {
		if(items != null) {
			orderItemRepository.deleteAll(items);
			return items;		
		} else {
			return new ArrayList<OrderItem>();
		}
	}
	
	public List<OrderItem> getAllByOrder(Order order) {
		if(order != null) {
			return orderItemRepository.findByOrder(order);
		} else {
			throw new IllegalArgumentException("Cannot get items for the order of value NULL.");
		}
	}
	
	public List<OrderItem> saveAll(List<OrderItem> items) {
		for(OrderItem item : items) {
			OrderItemUtils.validate(item, orderItemRepository, item.getId() != null);
		}
		return orderItemRepository.saveAll(items);
	}
	
	public List<OrderItem> deleteAllByOrder(Order order) {
		List<OrderItem> oi = getAllByOrder(order);
		orderItemRepository.deleteAll(oi);
		return oi;		
	}
		
}
