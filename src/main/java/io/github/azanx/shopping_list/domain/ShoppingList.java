package io.github.azanx.shopping_list.domain;

import java.util.LinkedHashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain Class representing shopping list
 * @author Kamil Piwowarski
 *
 */
@Entity
@Table(name = "shopping_list")
public class ShoppingList{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(name="owner_name", insertable=false, updatable=false)
	private String ownerName;

	//number of the list inside of user, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	@Column(nullable = false)
	private Short listNo;
	
	@Column(name = "list_name", nullable = false)
	private String listName;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(name="owner_name", referencedColumnName="user_name")
	private AppUser owner;
	
	@JsonIgnore
	@OneToMany(mappedBy = "parentList", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)	
	@OrderBy("itemNo")
	Set<ListItem> listItems = new LinkedHashSet<>();

	// empty constructor for JPA
	protected ShoppingList() {
	}

	/**
	 * if invoking this constructor directly remember to use parents {@link AppUser#addShoppingList(ShoppingList)} method immediately afterwards
	 * @param listName name of the new list
	 * @param owner AppUser owning the list
	 * @param listNo 
	 */
	protected ShoppingList(String listName, AppUser owner, Short listNo) {
		this.listName = listName;
		this.owner = owner;
		this.listNo = listNo;
		this.ownerName = owner.getUserName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerUserName() {
		return ownerName;
	}

	public void setOwnerUserName(String parentUserName) {
		this.ownerName = parentUserName;
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

	public Short getListNo() {
		return listNo;
	}

	public void setListNo(Short listNo) {
		this.listNo = listNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((listNo == null) ? 0 : listNo.hashCode());
		//no need for null checks as owner cannot be null, if it is - better to get NullPointerException and fix it
		//TODO readd nullchecks when finished - this method generally shouldn't throw exceptions
		result = prime * result + ownerName.hashCode();
//		result = prime * result + ((ownerId == null) ? 0 : ownerId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ShoppingList))
			return false;
		ShoppingList other = (ShoppingList) obj;
		if (listNo == null) {
			if (other.listNo != null)
				return false;
		} else if (!listNo.equals(other.listNo))
			return false;
		if (ownerName == null) {
			if (other.ownerName != null)
				return false;
		} else if (!ownerName.equals(other.ownerName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ShoppingList [id=" + id + ", ownerName=" + ownerName + ", listNo=" + listNo + ", listName=" + listName
				+ "]";
	}
}
