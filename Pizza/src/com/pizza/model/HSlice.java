package com.pizza.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name="T_ORDERED_ITEM")
public class HSlice extends HOrderedEntity {

	// TODO correct?
	public static final String NATIVE_ORDER_FIELD = "order";

}
