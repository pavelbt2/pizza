package com.pizza.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.pizza.auth.JwtUser;
import com.pizza.dao.OrderDao;
import com.pizza.error.EmailError;
import com.pizza.error.ItemAlreadyOrderedByUser;
import com.pizza.error.OrderAlreadyExistError;
import com.pizza.error.OrderNotOpenError;
import com.pizza.error.PizzaError;
import com.pizza.error.UnauthorizedUserError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;
import com.pizza.model.Item;
import com.pizza.model.OrderStatus;

@Service("orderService")
@Transactional(propagation=Propagation.REQUIRED, rollbackFor={PizzaError.class})
public class OrderServiceImpl implements OrderService {
	
	static Logger log = Logger.getLogger(OrderServiceImpl.class.getName());
	
	@Autowired
	private MailSender mailSender;	
	
	@Autowired
    private ObjectFactory<SimpleMailMessage> mailFactory;

	@Autowired
	private OrderDao orderDao;

	/****************************** READ **************************************************/
		
	@Override
	public List<HOrder> findAllOrders() {
		List<HOrder> orders = orderDao.findAllOrders();
		return orders;
	}

	@Override
	public HOrder findOrder(long orderId) {
		HOrder order =  orderDao.findById(orderId, false);
		if (order != null) {
			// populate items in hibernate object		
			Hibernate.initialize(order.getItems());
			completeSparePizzaSlices(order);
		}
		return order;
	}

	@Override
	public HOrder getCurrentOrder() {
		HOrder order =  orderDao.findByDate(getCurrentDate());
		if (order == null) {
			order = new HOrder();
			order.setValid(false);				
		} else {
			// populate items in hibernate object
			Hibernate.initialize(order.getItems());
			completeSparePizzaSlices(order);
			// TODO compine with findOrder()
		}
		return order;
	}
	
	
	/****************************** WRITE **************************************************/
	
	@Override
	public HOrder createNewOrder() throws OrderAlreadyExistError, EmailError {
		HOrder order = new HOrder();
		Date date = getCurrentDate();
		order.setDate(date);
		order.setStatus(OrderStatus.OPEN);
		String username = ((JwtUser)SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername(); 
		// TODO export to some user util
		order.setResponsible(username);
		
		try {
			orderDao.createOrder(order); // this also sets the id of the order to newly generated one
		} catch (ConstraintViolationException e) {
			log.error("Error creating new order", e);
			// probably 2 users tried to create the order at same time
			throw new OrderAlreadyExistError(date); 
		}
		
		// transaction will be aborted on error to send
		sendEmail(""+username+" ordering pizza in 15 minutes", "please fill your orders");	
		
		log.info("New order created for "+order.getDate());
		return order; // TODO if "already exist" exception - fetch and return existing order
	}


	@Override
	public void addItemToOrder(long orderId, HOrderedItem orderedItem) throws OrderNotOpenError, ItemAlreadyOrderedByUser {
		HOrder order =  orderDao.findById(orderId, true);
		
		if (!OrderStatus.OPEN.equals(order.getStatus() )) {
			throw new OrderNotOpenError(orderId);
		} 
		
		orderedItem.setOrder(order);		
		
		try {
			orderDao.saveOrderedItem(orderedItem);
		} catch (ConstraintViolationException e) {
			throw new ItemAlreadyOrderedByUser(orderedItem.getItem(), orderedItem.getUser());
		}
	}

	@Override
	public HOrder submitOrder(long orderId) throws OrderNotOpenError, UnauthorizedUserError, EmailError {
		HOrder order = orderDao.findById(orderId, true);		
		
		if (!OrderStatus.OPEN.equals(order.getStatus() )) {
			throw new OrderNotOpenError(orderId);
		} 
		
		JwtUser loggedInUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (!order.getResponsible().equals(loggedInUser.getUsername())) {
			throw new UnauthorizedUserError();
		}
		
		order.setStatus(OrderStatus.ORDERED);
		orderDao.updateOrder(order);
		// populate items in hibernate object		
		Hibernate.initialize(order.getItems()); // TODO move this to Dao??
		completeSparePizzaSlices(order);
		
		// transaction will be aborted on error to send
		sendEmail("order submitted", "Will arrive in 30-60 minutes");		
		
		return order;
	}	

	/****************************** HELPER METHODS **************************************************/
	
	private Date getCurrentDate() {
		LocalDate currentDate = LocalDate.now();
		return Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
	}
	
	// use with cautious
	private void completeSparePizzaSlices(HOrder order) {
		if (order.getItems() == null || order.getItems().isEmpty()) {
			// nothing to do
			return;
		}
		
		// count slices. Don't use streams cause ordered by item id
		int nSliceItems = 0;
		int nSlices = 0;		
		for (HOrderedItem oItem : order.getItems()) {
			if (!Item.PIZZA_SLICE.equals(oItem.getItem())) { 
				break; // can stop cause ordered by item id and pizza slice is first
			}
			nSliceItems ++;
			nSlices += oItem.getCount();
		}
				
		// Add slices as needed
		if ((nSlices % 8) == 0) {
			return; // no need to add slices
		}
		int nDummySlices = 8 - (nSlices % 8);
		String details = order.getItems().get(nSliceItems-1).getDetails();
		
		HOrderedItem spareSlices = new HOrderedItem();
		spareSlices.setItem(Item.PIZZA_SLICE);
		spareSlices.setDetails(details);
		spareSlices.setCount(nDummySlices);
		spareSlices.setUser("sparessssss");
		
		order.getItems().add(nSliceItems, spareSlices);
	}
	
	// TODO do it only on commit hook - before commit.
	// otherwise the actual commit might fail (especially if there are 2 servers? 
	// but didn't manage to reproduce. maybe depends on db lock mode? or is it hibernate?)
	private void sendEmail(String subject, String msgBody) throws EmailError {
		try {
			SimpleMailMessage mailMessage = mailFactory.getObject();
			mailMessage.setSubject("Pizza notification: "+subject);
			mailMessage.setText(msgBody);
			mailSender.send(mailMessage);
		} catch (Exception e) {
			log.error("Failed to send email", e);
			throw new EmailError();
		}
	}
	
}
