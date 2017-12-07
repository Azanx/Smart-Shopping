package io.github.azanx.shopping_list.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.AppUserDTO;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ListItemDTO;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.domain.ShoppingListDTO;
import io.github.azanx.shopping_list.domain.exception.ListTooLongException;
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.service.exception.DuplicateUserException;
import io.github.azanx.shopping_list.service.exception.ItemNotFoundException;
import io.github.azanx.shopping_list.service.exception.ListNotFoundException;
import io.github.azanx.shopping_list.service.exception.UserNotFoundException;

//

/**
 * Service used to separate repository usage and exception management (user not
 * found etc) for database access from controllers
 * 
 * @author Kamil Piwowarski
 */
@Service
public class RepositoryService {

	private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryService.class);

	private final AppUserRepository appUserRepository;
	private final ListItemRepository listItemRepository;
	private final ShoppingListRepository shoppingListRepository;

	@Autowired
	public RepositoryService(AppUserRepository appUserRepository, ListItemRepository listItemRepository,
			ShoppingListRepository shoppingListRepository) {
		this.appUserRepository = appUserRepository;
		this.listItemRepository = listItemRepository;
		this.shoppingListRepository = shoppingListRepository;
	}

	/**
	 * Checks if database has admin account configured, if not - creates it with
	 * default password 'admin' and generate WARNING in log
	 */
	@PostConstruct
	@Transactional(readOnly = false)
	public void init() {
		if (!appUserRepository.findByUserName("admin").isPresent()) {
			// check if admin exists, else create one and log
			LOGGER.warn("No 'admin' account found in DB, creating 'admin' account with password defaulting to 'admin'");
			AppUser newAdmin = new AppUser("admin", "admin", "admin@temp.pl");
			appUserRepository.save(newAdmin);
			
			//generate lists used in manual tests:
			newAdmin.addShoppingList("admin shopping").addListItem("mleko");
			newAdmin.addShoppingList("second list").addListItem("mleko z drugiej listy");
			appUserRepository.save(newAdmin);
		}
	}

	/**
	 * Returns AppUser instance if user with given userName exists
	 * 
	 * @param userName
	 *            name of the user to retrieve from the database
	 * @return AppUser
	 * @throws UserNotFoundException
	 *             if user with 'userName' doesn't exist
	 */
	@Transactional(readOnly = true)
	public AppUser getUser(String userName) {
		return appUserRepository//
			.findByUserName(userName) //
				.orElseThrow( //
					() -> new UserNotFoundException(userName));//
	}

	/**
	 * Retrieve shopping lists of an user from the database (without their items)
	 * 
	 * @param userName
	 *            name of the user whose lists to retrieve from the database
	 * @return collection containing all of the ShoppingLists belonging to this
	 *         user
	 * @throws ListNotFoundException
	 *             if user doesn't have any lists
	 */
	@Transactional(readOnly = true)
	public List<ShoppingList> getShoppingLists(String userName) {
		List<ShoppingList> lists = shoppingListRepository.findByOwnerNameOrderByListNo(userName);
		if (lists.isEmpty())
			throw new ListNotFoundException(userName);

		LOGGER.debug("Returning ShoppingLists for user: {}, Lists:\n {}", userName, //
				lists.stream()//
					.map(ShoppingList::toString)//
						.collect(Collectors.toList()));//
		return lists;
	}

	/**
	 * Retrieve ListItems of particular user's ShoppingList
	 * @deprecated
	 * @param userName
	 *            name of the user whose ListItems to retrieve from the database
	 * @param listNo
	 *            id (as in: number inside the list, NOT primary key in the DB)
	 *            of the list to retrieve
	 * @return collection containing all of the ListItems belonging to this
	 *         ShoppingList
	 * @throws ListNotFoundException
	 *             if user doesn't have list with this ID
	 */
	@Transactional(readOnly = true)
	public List<ListItem> getListItems(String userName, Short listNo) {
		LOGGER.debug("getItemsForUsersListNo: user: {}, listNo: {}", userName, listNo);
		
		List<ListItem> items = listItemRepository//
									.findByParentList_OwnerNameAndParentList_ListNo(userName, listNo);//
		if (items.isEmpty())
			throw new ItemNotFoundException(userName);
		
		LOGGER.debug("Returning ListItem's with ID's: {}", //
				items.stream()//
					.map(ListItem::getId)//
						.collect(Collectors.toList()));
		return items;
	}

	/**
	 * Retrieve ListItems of particular user's ShoppingList
	 * 
	 * @param userName
	 *            name of the user whose ListItems to retrieve from the database
	 * @param listId
	 *            id (as in: primary key in DB) of the list to retrieve
	 * @return collection containing all of the ListItems belonging to this
	 *         ShoppingList
	 * @throws ListNotFoundException
	 *             if user doesn't have list with this ID
	 */
	@Transactional(readOnly = true)
	public List<ListItem> getListItems(String userName, Long listId) {
		LOGGER.debug("getItemsForUsersListId: user: {}, listId: {}", userName, listId);
		
		retrieveShoppingList(listId, userName); //throws ListNotFound exception if user doesn't have list with this Id
		
		List<ListItem> items = listItemRepository.findByParentListIdOrderByItemNo(listId);

		LOGGER.debug("Returning ListItem's with ID's: {}", //
				items.stream()//
					.map(ListItem::getId)//
						.collect(Collectors.toList()));//
		return items;
	}
	
	/**
	 * Retrieve ShoppingList containing ListItems of particular user's ShoppingList
	 * 
	 * @param userName
	 *            name of the user whose ListItems to retrieve from the database
	 * @param listId
	 *            id (as in: primary key in DB) of the list to retrieve
	 * @return ShoppingList containing all of the ListItems belonging to it
	 * @throws ListNotFoundException
	 *             if user doesn't have list with this ID
	 */
	@Transactional(readOnly = true)
	public ShoppingList getShoppingListWithItems(String userName, Long listId) {
		LOGGER.debug("getShoppingListWithItemsForUsersListId: user: {}, listId: {}", userName, listId);
		
		ShoppingList list = retrieveShoppingList(listId, userName);
		
		List<ListItem> items = listItemRepository//
				.findByParentListIdOrderByItemNo(listId);
		list.setListItems(items);
		
		LOGGER.debug("Returning ListItem's with ID's: {}", //
				items.stream()//
						.map(ListItem::getId)//
						.collect(Collectors.toList()));
		return list;
		//TODO properly FETCH children in one query with the list
	}

	/**
	 * Add new user if there is no user with same name
	 * 
	 * @param newUser
	 *            AppUser instance of the new user to create
	 * @throws DuplicateUserException
	 *             if user with same name already exists
	 * @return AppUser instance of newly saved user
	 */
	@Transactional(readOnly = false)
	public AppUser addUser(AppUserDTO newUserDTO) {
		appUserRepository//
			.findByUserName(newUserDTO.getUserName())//
				.ifPresent(//
					user -> {throw new DuplicateUserException(user.getUserName());});//
		
		AppUser newUser = new AppUser(newUserDTO.getUserName(), newUserDTO.getPassword(), newUserDTO.getEmail());
		appUserRepository.save(newUser);
		
		LOGGER.info("addUser(): Created new user: {}", newUser);
		return newUser;
	}

	
	/**
	 * Add new shopping list for a given user
	 * 
	 * @param userName
	 *            name of the user for which to create the new list
	 * @param newListName
	 *            name of the new list
	 * @return newly created list
	 * @throws UserNotFoundException
	 *             if user with given name doesn't exist
	 * @throws ListTooLongException if size of the list containing ShoppingLists would exceed limit of Short type after adding new ShoppingList
	 */
	@Transactional(readOnly = false)
	public ShoppingList addShoppingListToUserByName(String userName, String newListName) {
		LOGGER.debug("addShoppingListToUserByName: user: {}, listName: {}", userName, newListName);
		
		AppUser user = getUser(userName); //throws UserNotFoundException
		
		// get count of user lists
		short count = shoppingListRepository.countByOwnerName(userName);
		if (count == Short.MAX_VALUE)
			throw new ListTooLongException(ListTooLongException.listType.SHOPPING_LIST, user.getId());
		count++;
		
		ShoppingList list = user.addShoppingList(newListName, count);
		list = shoppingListRepository.save(list);
		
		LOGGER.info("addShoppingListToUserByName: Created new list: {}", list);
		return list;
	}

	/**
	 * Add new ListItems to existing shopping list belonging to certain user
	 * 
	 * @param userName name of the user adding items (to check if it's his list)
	 * @param listItems ShoppingListDTO containing list of items to add
	 * @return ShoppingList populated with all current items
	 */
	@Transactional(readOnly = false)
	public List<ListItem> addItemsToShoppingList(String userName, ShoppingListDTO listWithNewItems) {
		LOGGER.debug("addItemsToShoppingList: user: {}, listId: {}", listWithNewItems.getOwnerName(), listWithNewItems.getId());
		
		//using ID provided by the client and username associated to current user (currently you can edit only your lists)
		ShoppingList list = retrieveShoppingList(listWithNewItems.getId(), userName);

		// get current count of ListsItems in the list
		short count = listItemRepository.countByParentListId(listWithNewItems.getId());
		
		//create list containing ListItems for new items to save in DB
		List<ListItem> newItems = new ArrayList<>(listWithNewItems.getListItems().size());
		for (ListItemDTO newItem : listWithNewItems.getListItems()) {
			//ignoring empty items from list provided by client
			if(!newItem.getItemName().isEmpty()) {
				LOGGER.debug("addItemsToShoppingList: adding item: {}, with No: {}", newItem.getItemName(), count);
				
				//must check if list isn't too long for Short type when adding every new item
				if (count == Short.MAX_VALUE)
					throw new ListTooLongException(ListTooLongException.listType.SHOPPING_LIST, listWithNewItems.getId());
				count++; //increase listNo for next item in the list
				
				ListItem item = list.addListItem(newItem.getItemName(), count);
				newItems.add(item);
			}
		}
		
		listItemRepository.save(newItems);
		//TODO check if it really saves values in batch
		
		newItems = listItemRepository.findByParentListIdOrderByItemNo(listWithNewItems.getId());
		
		LOGGER.debug("addItemsToShoppingList: returning items: {}", newItems);
		return newItems;
	}

	 /**
	 * Remove shopping list from database
	 * @param userName name of user to whom the list belongs
	 * @param listId Id of the list to remove
	 * @throws ListNotFoundException
	 */
	@Transactional(readOnly = false)
	 public void removeShoppingList(String userName, Long listId) {
		 LOGGER.debug("removeShoppingList: user: {}, list: {}", userName, listId);
		 
		 ShoppingList list = retrieveShoppingList(listId, userName);
		 
		 shoppingListRepository.delete(list);
		 
		 LOGGER.info("removeShoppingList: Deleted list: {} with listNo: {}", listId, list.getListNo());
		 
		 //after removing ShoppingList, must decrement number of every list with number greater than deleted one
		 List<ShoppingList> listsToReorder = shoppingListRepository.findByOwnerNameAndListNoGreaterThan(userName, list.getListNo());
		 for(ShoppingList currentList : listsToReorder) {
			 short newListNo = (short) (currentList.getListNo()-1);
			 
			 LOGGER.debug("reordering listNo: {} into {}", currentList.getListNo(), newListNo);
			 
			 currentList.setListNo(newListNo);
		 }
		 
		 shoppingListRepository.save(listsToReorder);
		 //TODO check if saves by batch or in loop
		 LOGGER.info("Reordered lists of user: {} with No greater than: {}", userName, list.getListNo());
	 }

	/**
	 * change status of the item to bought or unbought, according to value given inside ListItemDTO item
	 * @param userName name of user to whom the list belongs
	 * @param item ListItemDTO corresponding to the item in database we want to change. Values of item Id, parentListId, and Bought must be set correctly!
	 * @throws ListNotFoundException if given user doesn't have ShoppingList with Id set in item
	 * @throws ItemNotFoundException if ShoppingList declared inside item doesn't contain item with this Id
	 */
	@Transactional(readOnly = false)
	public void switchItemBoughtStatus(String userName, ListItemDTO item) {
		LOGGER.debug("switchItemBoughtStatus: user: {}, list: {}, item: {}", userName, item.getParentListId(), item.getId());

		retrieveShoppingList(item.getParentListId(), userName);
		
		ListItem listItem = listItemRepository//
				.findByIdAndParentListId(item.getId(), item.getParentListId())//
					.orElseThrow(//
						() -> new ItemNotFoundException(userName));
		
		listItem.setBought(item.getBought());
		listItemRepository.save(listItem);
		LOGGER.debug("switchItemBoughtStatus: changed ListItem id: {} to bought: {}",listItem.getId(), listItem.getBought());
	}
	
	/**
	 * Retrieve given ShoppingList from database or throw exception
	 * @param listId of the list to retrieve
	 * @param userName of AppUser owning the list
	 * @return ShoppingList
	 * @throws ListNotFoundException
	 */
	private ShoppingList retrieveShoppingList(Long listId, String userName) {
		return shoppingListRepository//
					.findByIdAndOwnerName(listId, userName)//
						.orElseThrow(//
							() -> new ListNotFoundException(listId, userName));//
	}
}
