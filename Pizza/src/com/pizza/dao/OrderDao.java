package com.pizza.dao;

import java.util.List;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.model.HOrder;


public interface OrderDao {
	
    List<HOrder> findAllOrders();

	void updateOrder(HOrder order) throws OrderDoesntExistError ;

	void createOrder(HOrder order);
       
}
