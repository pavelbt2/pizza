package com.pizza.service;

import java.util.List;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.general.OrderNotOpenError;
import com.pizza.general.UnauthorizedUserError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;

public interface OrderService {

	public List<HOrder> findAllOrders();

	public HOrder findOrder(long orderId);
	
	// returns dummy order if no order for today yet
	public HOrder getCurrentOrder();

	// creates new order for today.
	// if order already exists - returns it
	public HOrder createNewOrder();
	
	public void addItemToOrder(long orderId, HOrderedItem orderedItem);

	public HOrder submitOrder(long orderId) throws OrderDoesntExistError, OrderNotOpenError, UnauthorizedUserError;		
	
}
