package io.github.azanx.shopping_list.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "shopping_list")
public class ShoppingList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "list_name", nullable = false)
	private String listName;

	@JsonIgnore
	@ManyToOne
	private AppUser owner;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parentList", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)	
	List<ListItem> listItems = new ArrayList<>();

	// empty constructor for JPA
	protected ShoppingList() {
	}

	public ShoppingList(String listName, AppUser owner, List<ListItem> listItems) {
		super();
		this.listName = listName;
		this.owner = owner;
		this.listItems = listItems;
	}

	public ShoppingList(String listName, AppUser owner) {
		super();
		this.listName = listName;
		this.owner = owner;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public AppUser getOwner() {
		return owner;
	}

	public void setOwner(AppUser owner) {
		this.owner = owner;
	}

	public List<ListItem> getListItems() {
		return listItems;
	}

//	public void setListItems(List<ListItem> listItems) {
//		this.listItems = listItems;
//	}
	
	public void addListItem(ListItem newItem) {
		this.getListItems().add(newItem);
	}
	
	public ListItem addListItem(String itemName) {
		ListItem newItem = new ListItem(itemName, this);
		this.getListItems().add(newItem);
		return newItem;
	}
}
