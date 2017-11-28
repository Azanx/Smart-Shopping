package io.github.azanx.shopping_list.domain;

import java.util.HashSet;
import java.util.Set;

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

import io.github.azanx.shopping_list.domain.exception.ListTooLongException;

@Entity
@Table(name = "shopping_list")
public class ShoppingList {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	//number of the item inside of list, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	@Column(nullable = false)
	private Short itemNo;
	
	@Column(name = "list_name", nullable = false)
	private String listName;

	@JsonIgnore
	@ManyToOne
	private AppUser owner;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parentList", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)	
	Set<ListItem> listItems = new HashSet<>();

	// empty constructor for JPA
	protected ShoppingList() {
	}

	/**
	 * if invoking this constructor directly remember to use parents {@link AppUser#addShoppingList(ShoppingList)} method immediately afterwards
	 * @param listName name of the new list
	 * @param owner AppUser owning the list
	 */
	public ShoppingList(String listName, AppUser owner) {
		super();
		this.listName = listName;
		this.owner = owner;
		//check if collection won't grow over allowed limit (max value for itemNo)  
		if (owner.getShoppingList().size()+1 > Short.MAX_VALUE) {
			throw new ListTooLongException(ListTooLongException.listType.ITEM_LIST, owner.getId());
		} else
		this.itemNo = (short) (owner.getShoppingList().size()+1);
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

	public Set<ListItem> getListItems() {
		return listItems;
	}
	
	public void setListItems(Set<ListItem> listItems) {
		this.listItems = listItems;
	}	

	/**
	 * @param newItem
	 * @return true if ListItem added succesfully, false otherwise (couldn't add to the underlying collection)
	 * throws IllegalArgumentException if ListItem isn't owned by this ShoppingList
	 */
	public boolean addListItem(ListItem newItem) {
		if(!newItem.getParentList().equals(this))
			throw new IllegalArgumentException("list isn't owned by this AppUser instance!");
		return this.getListItems().add(newItem);
	}
	
	/**
	 * @param itemName name of the item to add into the list
	 * @return reference to the new item
	 */
	public ListItem addListItem(String itemName) {
		ListItem newItem = new ListItem(itemName, this);
		this.getListItems().add(newItem);
		return newItem;
	}

	public Short getItemNo() {
		return itemNo;
	}

	public void setItemNo(Short itemNo) {
		this.itemNo = itemNo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemNo == null) ? 0 : itemNo.hashCode());
		//no need for null checks as owner cannot be null
		result = prime * result + owner.hashCode();
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ShoppingList))
			return false;
		ShoppingList other = (ShoppingList) obj;
		if (itemNo == null) {
			if (other.itemNo != null)
				return false;
		} else if (!itemNo.equals(other.itemNo))
			return false;
		if (owner == null) {
			if (other.owner != null)
				return false;
		} else if (!owner.equals(other.owner))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ShoppingList [itemNo=" + itemNo + ", listName=" + listName + ", owner=" + owner + "]";
	}
}
