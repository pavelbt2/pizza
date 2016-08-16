package com.pizza.dao;

import java.util.Date;
import java.util.List;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;

public interface OrderDao {
	
	public List<HOrder> findAllOrders();

    public void updateOrder(HOrder order);

	public void createOrder(HOrder order);
	
	public HOrder findById(long orderId, boolean lockForUpdate);
	
	public HOrder findByDate(Date currentDate);

	public void saveOrderedItem(HOrderedItem orderedItem);
       
}
