package com.pizza.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="T_ORDER")
public class HOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ORDER_DATE", nullable = false)
	private String date;

	@Column(name = "RESPONSIBLE", nullable = false)
	private String responsible;
	
	@OneToMany(targetEntity = HOrderedItem.class, fetch= FetchType.EAGER) // TODO refactor to be lazy - not that easy..
	@JoinColumn(name = "ORDER_ID")
	private List<HOrderedItem> items;

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


	public String getDate() {
		return date;
	}


	public void setDate(String date) {
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

}
