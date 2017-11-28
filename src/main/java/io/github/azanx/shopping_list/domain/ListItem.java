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

import io.github.azanx.shopping_list.domain.exception.ListTooLongException;

/**
 * Domain Class representing item of the shopping list
 * @author Kamil Piwowarski
 *
 */
@Entity
@Table(name = "list_item")
public class ListItem {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;
	
	@Column(nullable = false)
	//number of the item inside of list, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	private Short itemNo;

	@Column(name = "item_name", nullable = false)
	private String itemname;
	
	@JsonIgnore
	@ManyToOne
	ShoppingList parentList;

	protected ListItem() {
	} 

	/**
	 * if invoking this constructor directly remember to use parents {@link ShoppingList#addListItem(ListItem)} method immediately afterwards
	 * @param itemname name of the new list item
	 * @param parentList list owning this item
	 */
	public ListItem(String itemname, ShoppingList parentList) {
		super();
		this.itemname = itemname;
		this.parentList = parentList;
		//check if collection won't grow over allowed limit (max value for itemNo)  
		if (parentList.getListItems().size()+1 > Short.MAX_VALUE) {
			throw new ListTooLongException(ListTooLongException.listType.ITEM_LIST, parentList.getId());
		} else
		this.itemNo = (short) (parentList.getListItems().size()+1);
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
	
	public Short getItemNo() {
		return itemNo;
	}

	public void setItemNo(Short itemNo) {
		this.itemNo = itemNo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((itemNo == null) ? 0 : itemNo.hashCode());
		//no need for null checks as parentList cannot be null, if it is - better to get NullPointerException
		result = prime * result + parentList.hashCode();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ListItem))
			return false;
		ListItem other = (ListItem) obj;
		if (itemNo == null) {
			if (other.itemNo != null)
				return false;
		} else if (!itemNo.equals(other.itemNo))
			return false;
		if (parentList == null) {
			if (other.parentList != null)
				return false;
		} else if (!parentList.equals(other.parentList))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ListItem [itemNo=" + itemNo + ", itemname=" + itemname + ", parentList=" + parentList + "]";
	}

	
}
