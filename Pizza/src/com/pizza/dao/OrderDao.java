package com.pizza.dao;

import java.util.List;
import com.pizza.model.HOrder;


public interface OrderDao {
	
    List<HOrder> findAllOrders();
       
}
