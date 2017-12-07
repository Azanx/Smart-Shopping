/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotBlank;

/**
 * @author Kamil Piwowarski
 *
 */
public class ShoppingListDTO {

	private Long id;
	
	private String ownerName;

	//number of the list inside of user, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	private Short listNo;
	
	@NotBlank(message="*In order to add a list you must assign it a name!")
	private String listName;
	
	//not using set as itemNo used for equality/hashCode will be assigned by @Service class, used to retrieve new items from form
	private List<ListItemDTO> listItems;
	
	/**
	 * Creates object without initialising listItems list
	 */
	public ShoppingListDTO() {
	}
	
	/**
	 * Creates object and initialise listItems list
	 * @param capacity
	 */
	public ShoppingListDTO(int capacity) {
		listItems = new ArrayList<ListItemDTO>(capacity);
		while(listItems.size()<capacity)
			listItems.add(new ListItemDTO());
	}

	public ShoppingListDTO(String ownerName, int capacity) {
		this(capacity);
		this.ownerName = ownerName;
	}
	
	public ShoppingListDTO(String ownerName, Long listId, int capacity) {
		this(ownerName, capacity);
		this.id = listId;
	}
	
	/**
	 * Converts ShoppingList into ShoppingListDTO. Doesn't initialize listItems!
	 * @param list ShoppingList to convert into DTO
	 */
	public ShoppingListDTO(ShoppingList list) {
		this.id = list.getId();
		this.ownerName = list.getOwnerUserName();
		this.listNo = list.getListNo();
		this.listName = list.getListName();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public Short getListNo() {
		return listNo;
	}

	public void setListNo(Short listNo) {
		this.listNo = listNo;
	}

	public String getListName() {
		return listName;
	}

	public void setListName(String listName) {
		this.listName = listName;
	}

	public List<ListItemDTO> getListItems() {
		return listItems;
	}

	public void setListItems(List<ListItemDTO> listItems) {
		this.listItems = listItems;
	}
}
