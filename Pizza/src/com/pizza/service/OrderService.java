package com.pizza.service;

import java.util.List;

import com.pizza.error.OrderAlreadyExistError;
import com.pizza.error.OrderNotOpenError;
import com.pizza.error.UnauthorizedUserError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;

public interface OrderService {

	public List<HOrder> findAllOrders();

	/*
 	 * The order contains ordered items.
	 */
	public HOrder findOrder(long orderId);
	
	/* 
	 * Returns dummy order if no order for today yet.
	 * The order contains ordered items.
	 */
	public HOrder getCurrentOrder();

	// creates new order for today.
	// if order already exists - throws exception
	public HOrder createNewOrder() throws OrderAlreadyExistError;
	
	public void addItemToOrder(long orderId, HOrderedItem orderedItem) throws OrderNotOpenError;

	public HOrder submitOrder(long orderId) throws OrderNotOpenError, UnauthorizedUserError;		
	
}
