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
    private AppUser user;

    @Before
    public void setup() {
        user = new AppUser("test_user", "123", "test@test.com");
        user.setId(1L); // we have to set id manually as we are not writing to database!
        listUnderTest = user.addShoppingList("Test shopping list");
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
        ShoppingList list2 = user.addShoppingList("Test shopping list");
        user.addShoppingList(list2);
        assertNotEquals(list2, listUnderTest);
    }
}
