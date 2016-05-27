package com.pizza.dao;

import java.util.List;

import com.pizza.general.OrderDoesntExistError;
import com.pizza.model.HOrder;


public interface OrderDao {
	
	public List<HOrder> findAllOrders();

    public void updateOrder(HOrder order) throws OrderDoesntExistError ;

	public void createOrder(HOrder order);
	
	public HOrder findById(long orderId);

	public HOrder findByDate(String currentDate);
       
}
