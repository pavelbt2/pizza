package com.pizza.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.pizza.general.UserAlreadyExistsError;
import com.pizza.model.HOrder;
import com.pizza.model.HUser;

@Repository("orderDao")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDaoImpl extends AbstractDao implements OrderDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<HOrder> findAllOrders() {
		Criteria criteria = getSession().createCriteria(HOrder.class);
		return (List<HOrder>) criteria.list();	
	}



}
