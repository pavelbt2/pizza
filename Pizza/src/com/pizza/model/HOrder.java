package com.pizza.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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

}
