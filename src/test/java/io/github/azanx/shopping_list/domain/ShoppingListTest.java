/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Kamil Piwowarski
 *
 */

public class ShoppingListTest {

	private ShoppingList listUnderTest;
	private AppUser testUser;
	
	@Before
	public void setup() {
		testUser = new AppUser("test_user", "123", "test@test.com");
		listUnderTest = new ShoppingList("Test shopping list", testUser); 
		testUser.addShoppingList(listUnderTest);
	}
	
	@Test
	public void addItemByName() {
		String itemName = "Test item 1";
		ListItem newItem = listUnderTest.addListItem(itemName);
		assertEquals(itemName, newItem.getItemName());
	}
	
	@Test
	public void addItem() {
		String itemName = "Test item 1";
		ListItem newItem = new ListItem(itemName, this.listUnderTest);
		this.listUnderTest.addListItem(newItem);
		assertEquals(itemName, newItem.getItemName());
	}
	
	@Test
	public void twoSimilarListsNotEqual() {
		ShoppingList list2 = new ShoppingList("Test shopping list", testUser); 
		testUser.addShoppingList(list2);
		assertNotEquals(list2, listUnderTest);
	}
	
	@Test public void addTwoItems() {
		//TODO implement
	}

	@Test public void removeItem() {
		//TODO implement
	}

}
