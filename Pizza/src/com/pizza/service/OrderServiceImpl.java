package com.pizza.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.dao.OrderDao;
import com.pizza.general.OrderDoesntExistError;
import com.pizza.general.PizzaError;
import com.pizza.model.HOrder;

@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={PizzaError.class})
public class OrderServiceImpl implements OrderService {

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

	@Override
	public void createOrder(HOrder order) {
		order.setDate(getCurrentDate());
		orderDao.createOrder(order);		
	}
	
	private String getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return currentDate.toString();
	}

	@Override
	public HOrder findOrder(long orderId) {
		return orderDao.findById(orderId);	
	}

	@Override
	public HOrder findCurrentOrder() {
		HOrder order =  orderDao.findByDate(getCurrentDate());
		if (order == null) {
			order = new HOrder();
			order.setDate(getCurrentDate());
		}
		return order;
	}	

}
