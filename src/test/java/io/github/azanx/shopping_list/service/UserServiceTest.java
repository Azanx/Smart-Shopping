/**
 * 
 */
package io.github.azanx.shopping_list.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

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
	AppUser user;
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
		user = new AppUser(userName, "password", "email@test.com");
		userService.addUser(user);
	}

	/**
	 * admin should be present after initialising the database during
	 * userService construction
	 */
	@Test
	public void getUserIfExistsElseThrow_isAdminPresent_Succeeds() {
		AppUser userUnderTest = userService.getUserIfExistsElseThrow("admin");
		assertNotNull(userUnderTest);
	}

	@Test
	public void addUser_Succeeds() {
		// user already added during @Before
		assertNotNull(userService.getUserIfExistsElseThrow(userName));
	}

	@Test(expected = UserNotFoundException.class)
	public void getUserIfExistsElseThrow_ForNonExistingUser_Fails() {
		userService.getUserIfExistsElseThrow("fake_accout");
	}

	@Test(expected = DuplicateUserException.class)
	public void addUser_DuplicateUser_Fails() {
		userService.addUser(user);
	}

	@Test(expected = DuplicateUserException.class)
	public void addUser_DuplicateUserWithDifferentObjects_Fails() {
		AppUser userUnderTest2 = new AppUser("User", "password1", "email1@test.com");
		userService.addUser(userUnderTest2);
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListsForUser_WhenUserNonExistent_Fails() {
		userService.getShoppingListsForUser("fake_account");
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListsForUser_WhenNoLists_Fails() {
		userService.getShoppingListsForUser(userName);
	}

	/**
	 * Using separate AppUser instance to separate this test from methods like
	 * "update" "addList" etc
	 */
	@Test
	public void getShoppingListsForUser_WithOneList_Succeeds() {
		ShoppingList list = user.addShoppingList("test list");
		appUserRepository.save(user); // dont have "updateUser method yet so
										// using repository instead
		Set<ShoppingList> lists = userService.getShoppingListsForUser(userName);
		assertEquals(1, lists.size()); // check if number of users lists is
										// correct
		assertTrue(lists.contains(list)); // check if user really has list equal
											// to created list
	}

	@Test
	public void getShoppingListsForUser_WithMultipleLists_Succeeds() {
		int amount = 3; // how many lists to add
		for (int i = 0; i < amount; i++)
			user.addShoppingList("test list"); // lists can have duplicate names
		appUserRepository.save(user);
		assertEquals(amount, userService.getShoppingListsForUser(userName).size());
	}

	/**
	 * Add list for user1, then list for user2. Get list of user2 and check if
	 * the item in it is correct checks if getting list uses listNo and not Id
	 */
	@Test
	public void getShoppingListsForUser_with2usersAndLists_ContainsCorrectItem() {
		AppUser user1 = new AppUser("User1", "password", "email@test.com");
		userService.addUser(user1);
		user1.addShoppingList("user1list");
		appUserRepository.save(user1);
		String user2Name = "User2";
		AppUser user2 = new AppUser(user2Name, "password", "email@test.com");
		userService.addUser(user2);
		ShoppingList user2list = user2.addShoppingList("user2list");
		shoppingListRepository.save(user2list);
		String itemName = "Some Item";
		ListItem item = new ListItem(itemName, user2list);
		user2list.addListItem(item);
		listItemRepository.save(item);
		// get Items from user2 list number one and check if they contain
		// inserted item u
		assertTrue(userService.getItemsForUsersListNo(user2Name, (short) 1).contains(item));
	}

	@Test(expected = UserNotFoundException.class)
	public void addShoppingListToUserByName_FailsIfNonExistentUser() {
		userService.addShoppingListToUserByName("fake_user", userName);
	}

	@Test
	// @Transactional //LazyInitializationException thrown without it at second
	// asssert
	public void addShoppingListToUserByName_succeedsForExistingUser() {
		ShoppingList list = userService.addShoppingListToUserByName(user.getUserName(), "LIST 1");
		assertNotNull(list);
		assertTrue(userService.getShoppingListsForUser(userName).contains(list));
	}

	@Test
	public void addShoppingListToUserByName_succeedsForMultipleLists() {

		userService.addShoppingListToUserByName(userName, "LIST 1");
		ShoppingList list2 = userService.addShoppingListToUserByName(user.getUserName(), "LIST 2");

		userService.addShoppingListToUserByName(user.getUserName(), "LIST 3");
		assertTrue(userService.getShoppingListsForUser(userName).contains(list2));
	}

	@Test(expected = ListNotFoundException.class)
	public void getItemsForUsersListId_FailsIfListNotExists() {
		userService.getItemsForUsersListId(userName, 100000L);
	}

	@Test(expected = ListNotFoundException.class)
	public void getItemsForUsersListId_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = new AppUser("second", "password", "email@test.com");
		userService.addUser(user2);
		ShoppingList user2list = userService.addShoppingListToUserByName(user2.getUserName(), "user2list");
		userService.getItemsForUsersListId(userName, user2list.getId());
	}

	@Test
	public void getItemsForUsersListId_SucceedsIfUserHaveThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		Set<ListItem> items = userService.getItemsForUsersListId(userName, list.getId());
		assertNotNull(items);
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListWithItemsForUsersListId_FailsIfListNotExists() {
		userService.getShoppingListWithItemsForUsersListId(userName, 100000L);
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListWithItemsForUsersListId_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = new AppUser("second", "password", "email@test.com");
		userService.addUser(user2);
		ShoppingList user2list = userService.addShoppingListToUserByName(user2.getUserName(), "user2list");
		userService.getShoppingListWithItemsForUsersListId(userName, user2list.getId());
	}

	@Test
	public void getShoppingListWithItemsForUsersListId_SucceedsIfUserHaveThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		ShoppingList items_list = userService.getShoppingListWithItemsForUsersListId(userName, list.getId());
		assertNotNull(items_list.getListItems());
	}
}
