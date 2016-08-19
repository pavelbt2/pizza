package com.pizza.controller;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;

import com.pizza.auth.JwtUtil;
import com.pizza.error.EmailError;
import com.pizza.error.ItemAlreadyOrderedByUser;
import com.pizza.error.OrderAlreadyExistError;
import com.pizza.error.OrderNotOpenError;
import com.pizza.error.PizzaError;
import com.pizza.error.UnauthorizedUserError;
import com.pizza.model.HOrder;
import com.pizza.model.HOrderedItem;
import com.pizza.model.Item;
import com.pizza.service.OrderService;

@CrossOrigin // TODO OK?? - no!! at least limit to specific client origin.
// allowedHeaders=
@RestController
// maps incoming requests to methods and responses in json format
public class AngularRestController {

	static Logger log = Logger.getLogger(AngularRestController.class.getName());

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtUtil jwtUtil; // TODO replace by JwtAuthenticationProvider?

	@Autowired
	OrderService orderService;

	// ------------------- Login --------------------------------------------------------
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest) {

		try {
		// Perform the security
		final Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(),
						authenticationRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication); // TODO needed??
		
		} catch (BadCredentialsException e) {
			return new ResponseEntity<Void>(HttpStatus.UNAUTHORIZED);
		}

		final String token = jwtUtil.generateToken(authenticationRequest.getUsername());

		// Return the token
		return ResponseEntity.ok(new JwtAuthenticationResponse(token));
	}

	// ------------------- Orders --------------------------------------------------------
	@RequestMapping(value = "/api/order/fetchall", method = RequestMethod.GET)
	public ResponseEntity<List<HOrder>> listAllOrders() {
		log.info("listAllOrders()");
		List<HOrder> orders = orderService.findAllOrders();

		return new ResponseEntity<List<HOrder>>(orders, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/order/get/{id}", method = RequestMethod.GET)
	public ResponseEntity<HOrder> getOrder(@PathVariable("id") long orderId) {
		log.info("getOrder() id=" + orderId);

		HOrder order = orderService.findOrder(orderId);

		return new ResponseEntity<HOrder>(order, HttpStatus.OK);
	}

	// get current order
	@RequestMapping(value = "/api/order/getcurrent", method = RequestMethod.GET)
	public ResponseEntity<HOrder> getCurrentOrder() {
		log.info("getCurrentOrder()");

		HOrder order = orderService.getCurrentOrder();

		return new ResponseEntity<HOrder>(order, HttpStatus.OK);
	}

	@RequestMapping(value = "/api/order/additem/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<Void> addItemToOrder(@PathVariable("orderId") long orderId,
			@RequestBody HOrderedItem orderedItem, UriComponentsBuilder ucBuilder) {
		log.info("Adding item :" + orderedItem.toString() + " to order: " + orderId);

		HttpStatus status = HttpStatus.OK;
		
		try {
			orderService.addItemToOrder(orderId, orderedItem);
		} catch (OrderNotOpenError e) {
			status = HttpStatus.FORBIDDEN;
		} catch (ItemAlreadyOrderedByUser e) {
			status = HttpStatus.CONFLICT;
		}

		return new ResponseEntity<Void>(status);
	}

	@RequestMapping(value = "/api/order/create", method = RequestMethod.POST)
	public ResponseEntity<HOrder> createNewOrder(UriComponentsBuilder ucBuilder) {
		log.info("Creating new order order");

		try {
			HOrder order = orderService.createNewOrder();
			return new ResponseEntity<HOrder>(order, HttpStatus.OK);
		} catch (OrderAlreadyExistError e) {
			return new ResponseEntity<HOrder>(HttpStatus.CONFLICT);
		} catch (EmailError e) {
			return new ResponseEntity<HOrder>(HttpStatus.INTERNAL_SERVER_ERROR);
		}		
	}

	@RequestMapping(value = "/api/order/submit/{orderId}", method = RequestMethod.POST)
	public ResponseEntity<HOrder> submitOrder(@PathVariable("orderId") long orderId,
			UriComponentsBuilder ucBuilder)  {
		log.info("Submit order: " + orderId);

		HttpStatus status = HttpStatus.OK;
		
		HOrder updatedOrder = null;
		try {
			updatedOrder = orderService.submitOrder(orderId);
		} catch (OrderNotOpenError e) {
			// the user might have tried to trick the system
			status = HttpStatus.FORBIDDEN;			
		} catch (UnauthorizedUserError e) {
			// the user might have tried to trick the system
			status = HttpStatus.UNAUTHORIZED;
		} catch (EmailError e) {
			status = HttpStatus.INTERNAL_SERVER_ERROR;
		}
		
		return new ResponseEntity<HOrder>(updatedOrder, status);
	}

	// ------------------- Items --------------------------------------------------------

	@RequestMapping(value = "/api/item/fetchall", method = RequestMethod.GET)
	public ResponseEntity<List<Item>> listAllItems() {
		log.info("listAllItems()");
		List<Item> orders = Item.getAllItemsAsList();

		return new ResponseEntity<List<Item>>(orders, HttpStatus.OK);
	}

}
