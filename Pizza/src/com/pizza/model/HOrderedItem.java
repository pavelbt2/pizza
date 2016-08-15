package com.pizza.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="T_ORDERED_ITEM")
public class HOrderedItem {

	public static final String NATIVE_ORDER_FIELD = "order";

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ITEM", nullable = false)
	@Enumerated(EnumType.ORDINAL)
    private Item item;
	
	@Column(name = "COUNT", nullable = false)
	private int count;
	
	@Column(name = "USER", nullable = false)
	private String user;
	
	@Column(name = "DETAILS", nullable = false)
	private String details;	
	
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="ORDER_ID", nullable=false)
    @JsonIgnore
	private HOrder order;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public HOrder getOrder() {
		return order;
	}

	public void setOrder(HOrder order) {
		this.order = order;
	}

	public String getDetails() {
		return details;
	}

	public void setDetails(String details) {
		this.details = details;
	}
	
	
}
