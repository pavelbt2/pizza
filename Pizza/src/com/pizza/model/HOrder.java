package com.pizza.model;

import java.util.Date;
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
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="T_ORDER")
public class HOrder {
	
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
	private Set<HOrderedItem> items;

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


	public Set<HOrderedItem> getItems() {
		return items;
	}


	public void setItems(Set<HOrderedItem> items) {
		this.items = items;
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
