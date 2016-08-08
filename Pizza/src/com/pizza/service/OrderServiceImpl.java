package com.pizza.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.configuration.JwtUser;
import com.pizza.controller.AngularRestController;
import com.pizza.dao.OrderDao;
import com.pizza.general.OrderDoesntExistError;
import com.pizza.general.OrderNotOpenError;
import com.pizza.general.PizzaError;
import com.pizza.general.UnauthorizedUserError;
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
		List<HOrder> orders = orderDao.findAllOrders();
		return orders;
	}
	
	private Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}

	@Override
	public HOrder findOrder(long orderId) {
		HOrder order =  orderDao.findById(orderId, false);
		if (order != null) {
			// populate items in hibernate object		
			Hibernate.initialize(order.getItems());
		}
		return order;
	}

	@Override
	public HOrder getCurrentOrder() {
		HOrder order =  orderDao.findByDate(getCurrentDate());
		if (order == null) {
			order = new HOrder();
			order.setValid(false);				
		} else {
			// populate items in hibernate object
			Hibernate.initialize(order.getItems());
		}
		return order;
	}
	
	@Override
	public HOrder createNewOrder() {
		HOrder order = new HOrder();
		order.setDate(getCurrentDate());
		order.setStatus(OrderStatus.OPEN);
		String username = ((JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(); 
		// TODO export to some user util
		order.setResponsible(username);
		
		orderDao.createOrder(order); // this also sets the id of the order to newly generated one 		
		log.info("New order created for "+order.getDate());
		return order; // TODO if "already exist" exception - fetch and return existing order
	}


	@Override
	public void addItemToOrder(long orderId, HOrderedItem orderedItem) {
		HOrder order =  orderDao.findById(orderId, false);
		orderedItem.setOrder(order);
		orderDao.saveOrderedItem(orderedItem);		
	}

	@Override
	public HOrder submitOrder(long orderId) throws OrderDoesntExistError, OrderNotOpenError, UnauthorizedUserError {
		HOrder order = orderDao.findById(orderId, true);		
		if (order == null) { 
			throw new OrderDoesntExistError(orderId);
		}
		
		if (!OrderStatus.OPEN.equals(order.getStatus() )) {
			throw new OrderNotOpenError(orderId);
		} 
		
		// TODO verify user is responsible
		JwtUser loggedInUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!order.getResponsible().equals(loggedInUser.getUsername())) {
			throw new UnauthorizedUserError();
		}
		
		order.setStatus(OrderStatus.ORDERED);
		orderDao.updateOrder(order);
		
					
		// TODO send mail. together with transaction??
		
		return order;
	}	

}
