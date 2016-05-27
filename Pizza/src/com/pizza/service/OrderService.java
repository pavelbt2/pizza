package com.pizza.service;

import java.util.List;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.model.HOrder;

public interface OrderService {

	public List<HOrder> findAllOrders();

	public void updateOrder(HOrder order) throws OrderDoesntExistError;

	public void createOrder(HOrder order);

	public HOrder findOrder(long orderId);
	
	// returns order with no id if no order placed this day yet
	public HOrder findCurrentOrder();	
	
}
