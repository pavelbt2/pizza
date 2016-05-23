package com.pizza.service;

import java.util.List;
import com.pizza.model.HOrder;

public interface OrderService {

	List<HOrder> findAllOrders();	
	
}
