package io.github.azanx.shopping_list.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import net.minidev.json.annotate.JsonIgnore;

@Entity
public class ShoppingList {

	@Id
	@GeneratedValue
	private Long ID;

	private String Name;
	@OneToMany(mappedBy = "parentList")
	private Set<Item> items = new HashSet<>();

	@JsonIgnore
	@ManyToOne
	private AppUser owner;

	// empty constructor for DAO
	public ShoppingList() {
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

}
