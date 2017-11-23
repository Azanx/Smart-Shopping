package io.github.azanx.shopping_list.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "list_item")
public class ListItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "item_name", nullable = false)
	private String itemname;
	
	@JsonIgnore
	@ManyToOne
	ShoppingList parentList;

	protected ListItem() {
	} 

	public ListItem(String itemname, ShoppingList parentList) {
		super();
		this.itemname = itemname;
		this.parentList = parentList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public ShoppingList getParentList() {
		return parentList;
	}

	public void setParentList(ShoppingList parentList) {
		this.parentList = parentList;
	}
}
