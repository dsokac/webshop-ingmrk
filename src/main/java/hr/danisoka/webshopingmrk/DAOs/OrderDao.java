package hr.danisoka.webshopingmrk.DAOs;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hr.danisoka.webshopingmrk.APIs.HnbRestApi;
import hr.danisoka.webshopingmrk.APIs.models.ExchangeRateResponse;
import hr.danisoka.webshopingmrk.models.CompleteOrder;
import hr.danisoka.webshopingmrk.models.Item;
import hr.danisoka.webshopingmrk.models.Order;
import hr.danisoka.webshopingmrk.models.OrderItem;
import hr.danisoka.webshopingmrk.models.Product;
import hr.danisoka.webshopingmrk.repositories.OrderRepository;
import hr.danisoka.webshopingmrk.utils.models.OrderUtils;

@Service
public class OrderDao {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private OrderItemDao orderItemDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Autowired
	private CustomerDao customerDao;
	
	@Autowired
	private HnbRestApi hnbApi;
	
	public List<Order> getAll() {
		return orderRepository.findAll();
	}

	@Transactional
	public Optional<CompleteOrder> getById(Integer id) throws Exception {
		Optional<Order> order = null;
		CompleteOrder co = new CompleteOrder();
		if(OrderUtils.doesIdExists(id, orderRepository)) {
			order = orderRepository.findById(id);
			co.setOrder(order.get());
			List<OrderItem> items = orderItemDao.getAllByOrder(order.get());
			co.convertToLst(items);
		}
		return Optional.of(co);
	}

	@Transactional
	public Order save(Order entity) throws Exception {
		Order saved = null;
		if(OrderUtils.validate(entity, false)) {
			if(entity.getCustomer().isEmpty()) {
				entity.setCustomer(customerDao.getById(entity.getCustomer().getId()).get());
			}
			saved = orderRepository.save(entity);
		}
		return saved;
	}

	@Transactional
	public CompleteOrder update(List<Item> items, Integer id) throws Exception {
		CompleteOrder storedOrder = getById(id).get();
		List<OrderItem> oiList = new ArrayList<OrderItem>();
		for(Item item : items) {
			Product p = item.getProduct();
			if(p.isEmpty()) {
				p = productDao.getById(p.getId()).get();
			}
			OrderItem oi = new OrderItem(storedOrder.getOrder(), p, item.getQuantity()); 
			if(item.getId() != null) {
				oi.setId(item.getId());
			}
			oiList.add(oi);
		}
		List<OrderItem> stored = orderItemDao.saveAll(oiList);
		storedOrder.convertToLst(stored);
		return storedOrder;
	}

	@Transactional
	public CompleteOrder deleteById(Integer id) throws Exception {
		CompleteOrder deleted = null;
		if(OrderUtils.doesIdExists(id, orderRepository)) {
			Optional<Order> order = orderRepository.findById(id);
			List<OrderItem> deletedItems = orderItemDao.deleteAllByOrder(order.get());
			orderRepository.delete(order.get());
			deleted = new CompleteOrder(order.get());
			deleted.convertToLst(deletedItems);
		}
		return deleted;
	}

	@Transactional
	public void deleteAll() {
		orderItemDao.deleteAll();
		orderRepository.deleteAll();
	}
	
	@Transactional
	public CompleteOrder finalizeOrderById(Integer id) throws Exception {
		ExchangeRateResponse res = hnbApi.getExchangeRateForCurrency(HnbRestApi.CURRENCY_EUR);
		CompleteOrder completeOrder = getById(id).get();
		if(OrderUtils.validate(completeOrder.getOrder(), true)) {
			BigDecimal totalHrk = new BigDecimal(0);
			totalHrk.setScale(2, RoundingMode.HALF_UP);
			for(Item item : completeOrder.getItems()) {
				BigDecimal v = item.getProduct().getPriceHrk().multiply(new BigDecimal(item.getQuantity()));
				totalHrk = totalHrk.add(v);
			}
			completeOrder.getOrder().setTotalPriceHrk(totalHrk);
			BigDecimal totalEur = totalHrk.divide(new BigDecimal(res.getSellingRate()), 2, RoundingMode.HALF_UP);
			completeOrder.getOrder().setTotalPriceEur(totalEur);
			completeOrder.getOrder().setStatus(Order.Status.SUBMITTED);
			Order stored = orderRepository.save(completeOrder.getOrder());
			completeOrder.setOrder(stored);
		} 
		return completeOrder;
	}
}
