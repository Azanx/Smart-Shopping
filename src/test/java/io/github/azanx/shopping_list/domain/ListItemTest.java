/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Kamil Piwowarski
 *
 */
public class ListItemTest {

	private AppUser user;
	private ShoppingList list;
	/**
	 * Initialize parent classes, we are testing equals behaviour so cannot mock them  
	 */
	@Before
	public void init() {
		user = new AppUser("testUser", "password", "email@test.com");
		user.setId(1L); // we have to set id manually as we are not writing to database!
		list = user.addShoppingList("test list");
//		list.setId(1L);; // we have to set id manually as we are not writing to database!
	}

	/**
	 * Test if two objects with same parameters but different parent list are not equal
	 */
	@Test
	public void testSimilarObjectsWithDifferentParentNotEqual() {
		ShoppingList list2 = user.addShoppingList("test list2");
		ListItem item1 = new ListItem("Item", list);
		list.addListItem(item1);
		ListItem item2 = new ListItem("Item", list2);
		list2.addListItem(item2);
		assertNotEquals(item2, item1);
	}
	
	/**
	 * Items with same name should be allowed in a list but not equal
	 */
	@Test
	public void testSimilarObjectsWithSameParentNotEqual() {
		ListItem item1 = new ListItem("Item", list);
		list.addListItem(item1);
		ListItem item2 = new ListItem("Item", list);
		list.addListItem(item2);
		assertNotEquals(item2, item1);
	}
}
