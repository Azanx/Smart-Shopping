/**
 * 
 */
package io.github.azanx.shopping_list.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import javax.transaction.Transactional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.github.azanx.shopping_list.config.JPAConfig;
import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.service.exception.DuplicateUserException;
import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import io.github.azanx.shopping_list.service.exception.UserNotFoundException;

/**
 * @author Kamil Piwowarski
 *
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { JPAConfig.class })
public class UserServiceTest {

	@Autowired
	AppUserRepository appUserRepository;
	@Autowired
	ListItemRepository listItemRepository;
	@Autowired
	ShoppingListRepository shoppingListRepository;

	UserService userService;
	AppUser userUnderTest;
	String userName = "User";

	// clean repositories, create fresh userService instance
	@Before
	public void setup() throws Exception {

		this.appUserRepository.deleteAll();
		this.listItemRepository.deleteAll();
		this.shoppingListRepository.deleteAll();

		userService = new UserService(appUserRepository, listItemRepository, shoppingListRepository);
		userService.init(); // have to call it explicitly as I'm not autowiring
							// the class so POST_CONSTRUCT wouldn't start
		userUnderTest = new AppUser(userName, "password", "email@test.com");
		userService.addUser(userUnderTest);
	}

	/**
	 * admin should be present after initialising the database during
	 * userService construction
	 */
	@Test
	public void isAdminPresent() {
		AppUser userUnderTest = userService.getUserIfExistsElseThrow("admin");
		assertNotNull(userUnderTest);
	}
	
	@Test
	public void addNewUserSucceeds() {
		//user already added during @Before
		assertNotNull(userService.getUserIfExistsElseThrow(userName));
	}
	
	@Test(expected = UserNotFoundException.class)
	public void getNonExistendUserThrows() {
		userService.getUserIfExistsElseThrow("fake");
	}

	@Test(expected = UserNotFoundException.class)
	public void throwsExceptionForNonExistingUser() {
		userService.getUserIfExistsElseThrow("fake_accout");
	}

	@Test(expected = DuplicateUserException.class)
	public void addDuplicateUserFails() {
		userService.addUser(userUnderTest);
	}
	
	@Test(expected = DuplicateUserException.class)
	public void addDuplicateUserWithDifferentObjectsFails() {
		AppUser userUnderTest2 = new AppUser("User", "password1", "email1@test.com");
		userService.addUser(userUnderTest2);
	}
	
	@Test(expected = ListNotFoundException.class)
	public void getShoppingListsForNonExistentUserFails() {
		userService.getShoppingListsForUser("fake_account");
	}
	
	@Test(expected = ListNotFoundException.class)
	public void getShoppingListsForUserWhenNoListsFails() {
		userService.getShoppingListsForUser(userName);
	}
	
	/**
	 * Using separate AppUser instance to separate this test from methods like "update" "addList" etc
	 */
	@Test
	public void getShoppingListsForUserWithOneList() {
		String userName = "Test2"; //hiding class field with the same name
		AppUser userUnderTest = new AppUser(userName, "password", "email@test.com"); //hiding class field with the same name
		userUnderTest.addShoppingList("test list");
		userService.addUser(userUnderTest);
		assertEquals(1, userService.getShoppingListsForUser(userName).size());
	}
	
	@Test
	public void getShoppingListsForUserWithMultipleListsHasCorrectCollectionLength() {
		String userName = "Test2"; //hiding class field with the same name
		AppUser userUnderTest = new AppUser(userName, "password", "email@test.com"); //hiding class field with the same name
		int amount = 3; //how many lists to add
		for(int i=0; i<amount; i++)
			userUnderTest.addShoppingList("test list"); //lists can have duplicate names
		userService.addUser(userUnderTest);
		assertEquals(amount, userService.getShoppingListsForUser(userName).size());
	}
	
	/**
	 * Add list for user1, then list for user2. Get list of user2 and check if the item in it is correct
	 * checks if getting list uses listNo and not Id
	 */
	@Test
	public void getShoppingListsForSecondUserListContainsCorrectItem() {
		AppUser user1 = new AppUser("User1", "password", "email@test.com");
		user1.addShoppingList("user1list");
		userService.addUser(user1);
		String user2Name = "User2";
		AppUser user2 = new AppUser(user2Name, "password", "email@test.com");
		ShoppingList user2list = new ShoppingList("user2list", user2);
		String itemName = "Some Item";
		ListItem item = new ListItem(itemName, user2list);
		user2list.addListItem(item);
		user2.addShoppingList(user2list);
		userService.addUser(user2);
		
		//get Items from user2 list number one and check if they contain inserted item u
		assertTrue(userService.getItemsForUsersListId(user2Name, (short)1).contains(item));
	}
		
	@Test(expected = UserNotFoundException.class)
	public void addShoppingListToUserByName_FailsIfNonExistentUser() {
		userService.addShoppingListToUserByName("fake_user", userName);
	}
	
	@Test
	@Transactional //LazyInitializationException thrown without it at second asssert
	public void addShoppingListToUserByName_succeedsForExistingUser() {
		ShoppingList list = userService.addShoppingListToUserByName(userUnderTest.getUserName(), userName);
		assertNotNull(list);
		assertTrue( userService.getShoppingListsForUser(userName).contains(list));
	}
//getShoppingListsForUser
//getItemsForUsersListId
}
