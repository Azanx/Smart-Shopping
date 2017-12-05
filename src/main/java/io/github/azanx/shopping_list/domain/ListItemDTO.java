/**
 * 
 */
package io.github.azanx.shopping_list.domain;

/**
 * @author Kamil Piwowarski
 *
 */
public class ListItemDTO {

	private Long id;
	
	//number of the item inside of list, used for equals, hashcode. There rather won't be lists longer than 2^15-1 elements
	private Short itemNo;

	private String itemName;
	
	private Long parentListId;

	public ListItemDTO() {
		itemName = "";
	}
	
	public ListItemDTO(ListItem item) {
		this.id = item.getId();
		this.itemNo = item.getItemNo();
		this.itemName = item.getItemName();
		this.parentListId = item.getParentListId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Short getItemNo() {
		return itemNo;
	}

	public void setItemNo(Short itemNo) {
		this.itemNo = itemNo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Long getParentListId() {
		return parentListId;
	}

	public void setParentListId(Long parentListId) {
		this.parentListId = parentListId;
	} 
}
