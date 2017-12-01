/**
 * 
 */
package io.github.azanx.shopping_list.domain;

/**
 * @author Kamil Piwowarski
 *
 */
public class ShoppingListDTO {

	private Long id;
	
	private String ownerName;

	//number of the list inside of user, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	private Short listNo;
	
	private String listName;
	
	public ShoppingListDTO() {
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
}
