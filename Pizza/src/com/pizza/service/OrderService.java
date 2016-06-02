package com.pizza.service;

import java.util.List;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;

public interface OrderService {

	public List<HOrder> findAllOrders();

	public void updateOrder(HOrder order) throws OrderDoesntExistError;

	public HOrder findOrder(long orderId);
	
	// creates new order for current date if none exist yet
	public HOrder getCurrentOrder();

	public void addItemToOrder(HOrderedItem orderedItem);	
	
}
