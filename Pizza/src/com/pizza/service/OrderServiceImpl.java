package com.pizza.service;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.dao.OrderDao;
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

}
