package com.pizza.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.controller.AngularRestController;
import com.pizza.dao.OrderDao;
import com.pizza.general.OrderDoesntExistError;
import com.pizza.general.PizzaError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;
import com.pizza.model.OrderStatus;

@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={PizzaError.class})
public class OrderServiceImpl implements OrderService {
	
	static Logger log = Logger.getLogger(OrderServiceImpl.class.getName());

	@Autowired
	private OrderDao orderDao;	

	@Override
	public List<HOrder> findAllOrders() {
		return orderDao.findAllOrders();
	}

	@Override
	public void updateOrder(HOrder order) throws OrderDoesntExistError {
		orderDao.updateOrder(order);
	}
	
	private Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public HOrder findOrder(long orderId) {
		return orderDao.findById(orderId);	
	}

	@Override
	public HOrder getCurrentOrder() {
		HOrder order =  orderDao.findByDate(getCurrentDate());
		if (order == null) {
			order = new HOrder();
			order.setValid(false);
				
		}
		return order;
	}
	
	@Override
	public HOrder createNewOrder() {
		HOrder order = new HOrder();
		order.setDate(getCurrentDate());
		order.setStatus(OrderStatus.OPEN);
		orderDao.createOrder(order); // this also sets the id of the order to newly generated one		
		// TODO set user from JWT
		log.info("New order created for "+order.getDate());
		return order; // TODO if "already exist" exception - fetch and return existing order
	}


	@Override
	public void addItemToOrder(long orderId, HOrderedItem orderedItem) {
		HOrder order = findOrder(orderId);
		orderedItem.setOrder(order);
		orderDao.saveOrderedItem(orderedItem);		
	}	

}
