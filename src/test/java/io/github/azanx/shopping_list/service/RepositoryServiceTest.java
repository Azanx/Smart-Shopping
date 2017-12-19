/**
 * 
 */
package io.github.azanx.shopping_list.service;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.github.azanx.shopping_list.config.JPAConfig;
import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.AppUserDTO;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ListItemDTO;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.domain.ShoppingListDTO;
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.service.exception.DuplicateUserException;
import io.github.azanx.shopping_list.service.exception.ItemNotFoundException;
import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import io.github.azanx.shopping_list.service.exception.UserNotFoundException;

/**
 * @author Kamil Piwowarski
 *
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { JPAConfig.class })
public class RepositoryServiceTest {

	@Autowired
	AppUserRepository appUserRepository;
	@Autowired
	ListItemRepository listItemRepository;
	@Autowired
	ShoppingListRepository shoppingListRepository;
	
	PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	RepositoryService userService;
	AppUser user;
	AppUserDTO userDTO;
	String userName = "User";

	// clean repositories, create fresh userService instance
	@Before
	public void setup() throws Exception {

		this.appUserRepository.deleteAll();
		this.listItemRepository.deleteAll();
		this.shoppingListRepository.deleteAll();

		userService = new RepositoryService(appUserRepository, listItemRepository, shoppingListRepository, passwordEncoder);
		userService.init(); // have to call it explicitly as I'm not autowiring
							// the class so POST_CONSTRUCT wouldn't start
							//currently using local implementation to make sure fields generated for manual tests wouldn't break automatic tests
		userDTO = new AppUserDTO(userName, "password".toCharArray(), "email@test.com");
		user = userService.addUser(userDTO);
	}

	@Test
	public void addUser_Succeeds() {
		// user already added during @Before
		assertNotNull(userService.getUser(userName));
	}
	
	@Test
	public void addUser_passwordClearedAfterSavingInDB() {
		AppUserDTO testUser = new AppUserDTO("user2", "password".toCharArray(), "email@test.com");
		userService.addUser(testUser);
		assertFalse(Arrays.equals("password".toCharArray(), testUser.getPassword()));
		assertArrayEquals("        ".toCharArray(), testUser.getPassword());
	}

	@Test(expected = UserNotFoundException.class)
	public void getUserIfExistsElseThrow_ForNonExistingUser_Fails() {
		userService.getUser("fake_accout");
	}

	@Test(expected = DuplicateUserException.class)
	public void addUser_DuplicateUser_Fails() {
		userService.addUser(userDTO);
	}

	@Test(expected = DuplicateUserException.class)
	public void addUser_DuplicateUserWithDifferentObjects_Fails() {
		AppUserDTO userUnderTest2 = new AppUserDTO("User", "password1".toCharArray(), "email1@test.com");
		userService.addUser(userUnderTest2);
	}

	public void getShoppingListsForUser_WhenUserNonExistent_Fails() {
		assertTrue(userService.getShoppingLists("fake_account").isEmpty());
	}

	public void getShoppingListsForUser_WhenNoLists_Fails() {
		assertTrue(userService.getShoppingLists(userName).isEmpty());
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
		List<ShoppingList> lists = userService.getShoppingLists(userName);
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
		assertEquals(amount, userService.getShoppingLists(userName).size());
	}

	/**
	 * Add list for user1, then list for user2. Get list of user2 and check if
	 * the item in it is correct checks if getting list uses listNo and not Id
	 */
	@Test
	public void getShoppingListsForUser_with2usersAndLists_ContainsCorrectItem() {
		AppUser user1 = userService.addUser(new AppUserDTO("User1", "password".toCharArray(), "email@test.com"));
		user1.addShoppingList("user1list");
		appUserRepository.save(user1);
		String user2Name = "User2";
		AppUser user2 = userService.addUser(new AppUserDTO(user2Name, "password".toCharArray(), "email@test.com"));
		ShoppingList user2list = user2.addShoppingList("user2list");
		shoppingListRepository.save(user2list);
		String itemName = "Some Item";
		ListItem item = new ListItem(itemName, user2list);
		user2list.addListItem(item);
		listItemRepository.save(item);
		// get Items from user2 list number one and check if they contain
		// inserted item u
		assertTrue(userService.getListItems(user2Name, (short) 1).contains(item));
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
		assertTrue(userService.getShoppingLists(userName).contains(list));
	}

	@Test
	public void addShoppingListToUserByName_succeedsForMultipleLists() {

		userService.addShoppingListToUserByName(userName, "LIST 1");
		ShoppingList list2 = userService.addShoppingListToUserByName(user.getUserName(), "LIST 2");

		userService.addShoppingListToUserByName(user.getUserName(), "LIST 3");
		assertTrue(userService.getShoppingLists(userName).contains(list2));
	}

	@Test(expected = ListNotFoundException.class)
	public void getItemsForUsersListId_FailsIfListNotExists() {
		userService.getListItems(userName, 100000L);
	}

	@Test(expected = ListNotFoundException.class)
	public void getItemsForUsersListId_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = userService.addUser(new AppUserDTO("second", "password".toCharArray(), "email@test.com"));
		ShoppingList user2list = userService.addShoppingListToUserByName(user2.getUserName(), "user2list");
		userService.getListItems(userName, user2list.getId());
	}

	@Test
	public void getItemsForUsersListId_SucceedsIfUserHaveThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		List<ListItem> items = userService.getListItems(userName, list.getId());
		assertNotNull(items);
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListWithItemsForUsersListId_FailsIfListNotExists() {
		userService.getShoppingListWithItems(userName, 100000L);
	}

	@Test(expected = ListNotFoundException.class)
	public void getShoppingListWithItemsForUsersListId_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = userService.addUser(new AppUserDTO("second", "password".toCharArray(), "email@test.com"));
		ShoppingList user2list = userService.addShoppingListToUserByName(user2.getUserName(), "user2list");
		userService.getShoppingListWithItems(userName, user2list.getId());
	}

	@Test
	public void getShoppingListWithItemsForUsersListId_SucceedsIfUserHaveThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		ShoppingList items_list = userService.getShoppingListWithItems(userName, list.getId());
		assertNotNull(items_list.getListItems());
	}
	
	@Test(expected = ListNotFoundException.class)
	public void addItemsToShoppingList_FailsIfListNotExists() {
		ShoppingListDTO items = new ShoppingListDTO(userName, 100000L, 2);
		items.getListItems().get(0).setItemName("some item"); //set item name for the new item in the list
		
		userService.addItemsToShoppingList(userName, items);
	}
	
	@Test(expected = ListNotFoundException.class)
	public void addItemsToShoppingList_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = userService.addUser(new AppUserDTO("second", "password".toCharArray(), "email@test.com"));
		userService.addShoppingListToUserByName(user2.getUserName(), "second's list");
		ShoppingListDTO items = new ShoppingListDTO(userName, 100000L, 2);
		items.getListItems().get(0).setItemName("some item"); //set item name for the new item in the list
		
		userService.addItemsToShoppingList(user.getUserName(), items);
	}
	
	@Test
	public void addItemsToShoppingList_SucceedsIfUserHaveThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		ShoppingListDTO items = new ShoppingListDTO(userName, list.getId(), 1);
		ListItemDTO item = items.getListItems().get(0);
		item.setItemName("some item"); //set item name for the new item in the list
		
		List<ListItem> updatedItemsList = userService.addItemsToShoppingList(user.getUserName(), items);
		
		//updatedItemsList contains/get won't work as ListItem equality/hashCode check depends on listNo which was assigned right now
		//as list didn't contain any items before adding new one, we can be sure it's the only item in the set - we can compare it's name to the original item
		for (ListItem newItem : updatedItemsList)
			assertEquals(item.getItemName(), newItem.getItemName());
	}
	
	@Test 
	public void addItemsToShoppingList_AddsNOTHINGIfGivenListOfEmptyItems() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list1");
		ShoppingListDTO items = new ShoppingListDTO(userName, list.getId(), 5);
		for (ListItemDTO item : items.getListItems())
			item.setItemName(""); //set item name for the new item in the list
		
		List<ListItem> updatedItemsList = userService.addItemsToShoppingList(user.getUserName(), items);
		
		//as list didn't contain any items before adding new one, we should get an empty set
		assertTrue(updatedItemsList.isEmpty());
	}
	
	@Test(expected = ListNotFoundException.class)
	public void removeShoppingList_FailsIfListNotExist() {
		userService.removeShoppingList(userName, 10000L);
	}
	
	@Test(expected = ListNotFoundException.class)
	public void removeShoppingList_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = userService.addUser(new AppUserDTO("second", "password".toCharArray(), "email@test.com"));
		ShoppingList list2 = userService.addShoppingListToUserByName(user2.getUserName(), "some list");
		userService.removeShoppingList(userName, list2.getId());
	}
	
	@Test
	public void removeShoppingList_SucceedsIfUserHasThisList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list");
		userService.removeShoppingList(userName, list.getId());
		assertNull(shoppingListRepository.findOne(list.getId()));
	}
	
	@Test
	public void removeShoppingList_DecrementsListNoForListsWithHigherNumber() {
		short count = 3; //how many lists to create
		ShoppingList list = null; //contains last created list
		for(short i=0; i<count; i++)
			list = userService.addShoppingListToUserByName(userName, "list");
		
		short lastListNo = list.getListNo();//number of the last list before removal
		userService.removeShoppingList(userName, list.getId()+1-count);//remove first list, using id relative to the last list
																	//in case there were some lists created during userService.init()
		list = shoppingListRepository.findOne(list.getId());
		assertEquals(lastListNo-1, list.getListNo().shortValue());//listNo of last list (and every other greater than removed list) should be now lower by 1
	}
	
	@Test
	public void removeShoppingList_RemovesAlsoItemsBelongingToList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list");
		ShoppingListDTO listDTO = new ShoppingListDTO(list);
		listDTO.setListItems(new ArrayList<ListItemDTO>());
		listDTO.getListItems().add(new ListItemDTO("item"));
		userService.addItemsToShoppingList(userName, listDTO);
		
		assertFalse(listItemRepository//
				.findByParentListIdOrderByItemNo(list.getId())//
					.isEmpty()); //check if list of items really was full after creation
		
		userService.removeShoppingList(userName, list.getId());
		
		assertTrue(listItemRepository//
				.findByParentListIdOrderByItemNo(list.getId())//
					.isEmpty()); //check if items were deleted alongside containing shoppinglist
	}
	
	@Test(expected = ListNotFoundException.class)
	public void switchItemBoughtStatus_FailsIfListOwnedByAnotherUser() {
		AppUser user2 = 
		userService.addUser(new AppUserDTO("second", "password".toCharArray(), "email@test.com"));
		
		ShoppingListDTO listWithItems = //
				new ShoppingListDTO(//
						userService.addShoppingListToUserByName(user2.getUserName(), "some list"));
		
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		listWithItems.getListItems()//
				.add(0, new ListItemDTO("item"));
		List<ListItem> items = userService.addItemsToShoppingList(user2.getUserName(), listWithItems);
		//there is only one element in the list so we are using index 0, new item defaults to Bought=false so we need to change status to true
		listWithItems.getListItems().get(0).setId(items.get(0).getId());//setting item Id to one added during saving in DB
		listWithItems.getListItems().get(0).setParentListId(items.get(0).getParentListId());//setting parent list id
		listWithItems.getListItems().get(0).setBought(true);
		
		userService.switchItemBoughtStatus(userName, listWithItems.getListItems().get(0));
	}
	
	@Test(expected = ListNotFoundException.class)
	public void switchItemBoughtStatus_FailsIfListDoesntExist() {
		ShoppingListDTO listWithItems = new ShoppingListDTO(userName, 10000L, 2);
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		listWithItems.getListItems().add(new ListItemDTO("item"));
		ListItemDTO item = listWithItems.getListItems().get(0);
		item.setId(1L);
		item.setParentListId(10000L);

		userService.switchItemBoughtStatus(userName, listWithItems.getListItems().get(0));
		
	}
	
	@Test(expected = ItemNotFoundException.class)
	public void switchItemBoughtStatus_FailsIfListDoesntHaveThisItem() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list");
		ShoppingListDTO listWithItems = new ShoppingListDTO(list);
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		listWithItems.getListItems().add(new ListItemDTO("item"));
		ListItemDTO item = listWithItems.getListItems().get(0);
		item.setId(1L);
		item.setParentListId(listWithItems.getId());
	
		userService.switchItemBoughtStatus(userName, listWithItems.getListItems().get(0));
	}
	
	@Test(expected = ItemNotFoundException.class)
	public void switchItemBoughtStatus_FailsIfItemExistsInAnotherList() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list");
		ShoppingListDTO listWithItems = new ShoppingListDTO(list);
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		
		ShoppingList list2 = userService.addShoppingListToUserByName(userName, "list2");
		ShoppingListDTO listWithItems2 = new ShoppingListDTO(list2);
		listWithItems2.setListItems(new ArrayList<ListItemDTO>());
		listWithItems2.getListItems().add(new ListItemDTO("item"));
		List<ListItem> items = userService.addItemsToShoppingList(userName, listWithItems2);
		
		listWithItems.getListItems().add(new ListItemDTO(items.get(0)));	
		
		ListItemDTO item = listWithItems.getListItems().get(0);
		item.setParentListId(listWithItems.getId());//setting parentlist id to first list,
		//trying to fool the userService into changing item from another list than declared
	
		userService.switchItemBoughtStatus(userName, item);
	}

	@Test
	public void switchItemBoughtStatus_SucceedsIfItemValid() {
		ShoppingList list = userService.addShoppingListToUserByName(userName, "list");
		ShoppingListDTO listWithItems = new ShoppingListDTO(list);
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		
		listWithItems.getListItems().add(new ListItemDTO("item"));
		List<ListItem> items = userService.addItemsToShoppingList(userName, listWithItems);
		
		
		listWithItems.setListItems(new ArrayList<ListItemDTO>());
		listWithItems.getListItems().add(new ListItemDTO(items.get(0)));
		ListItemDTO item = listWithItems.getListItems().get(0);
		item.setBought(true);
		
		userService.switchItemBoughtStatus(userName, item);
		
		assertTrue(listItemRepository.//
						findOne(item//
								.getId())//
									.getBought());
	}
}
