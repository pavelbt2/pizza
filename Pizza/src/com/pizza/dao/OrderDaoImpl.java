package com.pizza.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.error.OrderDoesntExistError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;

@Repository("orderDao")
@Transactional(propagation=Propagation.REQUIRED)
public class OrderDaoImpl extends AbstractDao implements OrderDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<HOrder> findAllOrders() {
		Criteria criteria = getSession().createCriteria(HOrder.class)
				.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY); // otherwise  -returns ordersXoreder-items
		criteria.addOrder(Order.desc(HOrder.NATIVE_ORDER_FIELD));
		List<HOrder> orders =  (List<HOrder>) criteria.list();
		return orders;
	}

	@Override
	public HOrder findById(long orderId, boolean lockForUpdate) {
        Criteria criteria = getSession().createCriteria(HOrder.class);
        criteria.add(Restrictions.eq("id",orderId));
        if (lockForUpdate) {
        	criteria.setLockMode(LockMode.PESSIMISTIC_WRITE);
        }
        HOrder order = (HOrder) criteria.uniqueResult();
        
        return order;
	}	
	
	@Override
	public HOrder findByDate(Date currentDate) {
        Criteria criteria = getSession().createCriteria(HOrder.class);
        criteria.add(Restrictions.eq("date",currentDate));
        return (HOrder) criteria.uniqueResult();
	}

	
	@Override
	public void updateOrder(HOrder order) {		
		getSession().update(order);
	}

	@Override
	public void createOrder(HOrder order) {
		getSession().save(order);		
	}

	@Override
	public void saveOrderedItem(HOrderedItem orderedItem) {
		getSession().save(orderedItem);
	}



}
