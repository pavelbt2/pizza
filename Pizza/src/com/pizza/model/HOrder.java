package com.pizza.model;

import java.util.Date;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;


@Entity
@Table(name="T_ORDER")
public class HOrder {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	@Column(name = "ORDER_DATE", nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd")
	private Date date;

	@Column(name = "RESPONSIBLE", nullable = false)
	private String responsible;
	
	@OneToMany(fetch= FetchType.EAGER, mappedBy="order") // TODO refactor to be lazy - not that easy..
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

}
