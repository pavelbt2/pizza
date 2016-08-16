package com.pizza.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.configuration.JwtUser;
import com.pizza.controller.AngularRestController;
import com.pizza.dao.OrderDao;
import com.pizza.error.OrderAlreadyExistError;
import com.pizza.error.OrderDoesntExistError;
import com.pizza.error.OrderDoesntExistError;
import com.pizza.error.OrderNotOpenError;
import com.pizza.error.PizzaError;
import com.pizza.error.UnauthorizedUserError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;
import com.pizza.model.Item;
import com.pizza.model.OrderStatus;

@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={PizzaError.class})
public class OrderServiceImpl implements OrderService {
	
	static Logger log = Logger.getLogger(OrderServiceImpl.class.getName());

	@Autowired
	private OrderDao orderDao;

	/****************************** READ **************************************************/
		
	@Override
	public List<HOrder> findAllOrders() {
		List<HOrder> orders = orderDao.findAllOrders();
		return orders;
	}

	@Override
	public HOrder findOrder(long orderId) {
		HOrder order =  orderDao.findById(orderId, false);
		if (order != null) {
			// populate items in hibernate object		
			Hibernate.initialize(order.getItems());
			completeSparePizzaSlices(order);
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
			completeSparePizzaSlices(order);
			// TODO compine with findOrder()
		}
		return order;
	}
	
	
	/****************************** WRITE **************************************************/
	
	@Override
	public HOrder createNewOrder() throws OrderAlreadyExistError {
		HOrder order = new HOrder();
		Date date = getCurrentDate();
		order.setDate(date);
		order.setStatus(OrderStatus.OPEN);
		String username = ((JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(); 
		// TODO export to some user util
		order.setResponsible(username);
		
		try {
			orderDao.createOrder(order); // this also sets the id of the order to newly generated one
		} catch (ConstraintViolationException e) {
			log.error("Error creating new order", e);
			// probably 2 users tried to create the order at same time
			throw new OrderAlreadyExistError(date); 
		}
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

	/****************************** HELPER METHODS **************************************************/
	
	private Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	// warning - use in relevant places and with caution. 
	private void completeSparePizzaSlices(HOrder order) {
		if (order.getItems() == null || order.getItems().isEmpty()) {
			// nothing to do
			return;
		}
		
		// count slices. Don't use streams cause ordered by item id
		int nSliceItems = 0;
		int nSlices = 0;		
		for (HOrderedItem oItem : order.getItems()) {
			if (!Item.PIZZA_SLICE.equals(oItem.getItem())) { 
				break; // can stop cause ordered by item id and pizza slice is first
			}
			nSliceItems ++;
			nSlices += oItem.getCount();
		}
				
		// Add slices as needed
		if ((nSlices % 8) == 0) {
			return; // no need to add slices
		}
		int nDummySlices = 8 - (nSlices % 8);
		String details = order.getItems().get(nSliceItems-1).getDetails();
		
		HOrderedItem spareSlices = new HOrderedItem();
		spareSlices.setItem(Item.PIZZA_SLICE);
		spareSlices.setDetails(details);
		spareSlices.setCount(nDummySlices);
		spareSlices.setUser("sparessssss");
		
		order.getItems().add(nSliceItems, spareSlices);
	}
	
}
