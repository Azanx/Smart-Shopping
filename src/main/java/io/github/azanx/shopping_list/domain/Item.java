package io.github.azanx.shopping_list.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import net.minidev.json.annotate.JsonIgnore;

@Entity
public class Item {

	@Id
	@GeneratedValue
	private Long ID;

	@JsonIgnore
	@ManyToOne
	private ShoppingList parentList;

	private String name;

	public Item(String name, Long iD) {
		super();
		this.name = name;
		ID = iD;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public ShoppingList getParentList() {
		return parentList;
	}

	public void setParentList(ShoppingList parentList) {
		this.parentList = parentList;
	}

}
