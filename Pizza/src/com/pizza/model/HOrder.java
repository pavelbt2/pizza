package com.pizza.model;

import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="T_ORDER")
public class HOrder {

	public static final String NATIVE_ORDER_FIELD = "date";
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ORDER_DATE", nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd", timezone="Israel") // TODO do it better way
	private Date date;
	
	@Column(name = "STATUS", nullable = false)
	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	@Column(name = "RESPONSIBLE", nullable = false)
	private String responsible;
	
	@Transient
	private boolean isValid = true;
	
	@OneToMany(fetch= FetchType.LAZY, mappedBy="order")
	@OrderBy(HOrderedItem.NATIVE_ORDER_FIELD)
	@Where(clause="item<>'PIZZA_SLICE'") //TODO any better way?
	private List<HOrderedItem> items;
	
	@OneToMany(fetch= FetchType.LAZY, mappedBy="order")
	@OrderBy(HOrderedItem.NATIVE_ORDER_FIELD)
	@Where(clause="item='PIZZA_SLICE'") //TODO any better way?
	private List<HSlice> slices;

	@Override
	public String toString() {
		return "Order [id=" + id + ", date=" + date + ", responsible=" + responsible;
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Date getDate() {
		return date;
	}


	public void setDate(Date date) {
		this.date = date;
	}


	public String getResponsible() {
		return responsible;
	}


	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}


	public List<HOrderedItem> getItems() {
		return items;
	}

	public void setItems(List<HOrderedItem> items) {
		this.items = items;
	}

	public List<HSlice> getSlices() {
		return slices;
	}

	public void setSlices(List<HSlice> slices) {
		this.slices = slices;
	}

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}

	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

}
