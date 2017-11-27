/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ShoppingList;

/**
 * @author Kamil Piwowarski
 *
 */

public class ShoppingListTest {

	private ShoppingList listUnderTest;
	@Before
	public void setup() {
		//setting owner as null, we won't be using the owner it isolated tests for ShoppingList class anyway
		listUnderTest = new ShoppingList("Test shopping list", null); 
	}
	
	@Test
	public void addItemByName() {
		String itemName = "Test item 1";
		ListItem newItem = listUnderTest.addItem(itemName);
		assertEquals(itemName, newItem.getItemname());
	}
	
	@Test
	public void addItem() {
		String itemName = "Test item 1";
		ListItem newItem = new ListItem(itemName, this.listUnderTest);
		this.listUnderTest.addListItem(newItem);
		assertEquals(itemName, newItem.getItemname());
	}
	
	@Test void addTwoItems() {
		
	}

	@Test public void removeItem() {
		//TODO
	}

}
