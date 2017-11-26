package io.github.azanx.shopping_list.service;

import java.util.Collection;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.rest.exception.ListNotFoundException;
import io.github.azanx.shopping_list.rest.exception.UserNotFoundException;

//used to separate repository usage and exception management for database access (user not found etc) from RestApiController

@Service
public class UserService {

	private final AppUserRepository appUserRepository;

	private final ListItemRepository listItemRepository;

	private final ShoppingListRepository shoppingListRepository;

	@Autowired
	public UserService(AppUserRepository appUserRepository, ListItemRepository listItemRepository,
			ShoppingListRepository shoppingListRepository) {
		super();
		this.appUserRepository = appUserRepository;
		this.listItemRepository = listItemRepository;
		this.shoppingListRepository = shoppingListRepository;
	}

	// temporary for test use
	@PostConstruct
	public void init() {
		if (!appUserRepository.findByUserName("admin").isPresent()) {
			AppUser newAdmin = new AppUser("admin", "admin", "admin@temp.pl");
			ShoppingList shopList = new ShoppingList("admin shopping", newAdmin);
			ListItem listItem1 = new ListItem("mleko", shopList);
			shopList.addListItem(listItem1);
			newAdmin.addShoppingList(shopList);
			newAdmin.addShoppingList("second list").addListItem("mleko z drugiej listy");
			appUserRepository.save(newAdmin);
			;

		}
	}

	/**
	 * Returns AppUser instance if user with userName exists
	 * 
	 * @throws UserNotFoundException
	 *             if user with 'userName' doesn't exist
	 * 
	 */
	// throws clause not necessarry as it's an unchecked exception
	public AppUser getUserIfExistsElseThrow(String userName) {
		return appUserRepository.findByUserName(userName).orElseThrow(() -> new UserNotFoundException(userName));
	}

	public Collection<ShoppingList> getShoppingListsForUser(String userName) {
		getUserIfExistsElseThrow(userName); // check if user exists
		Collection<ShoppingList> lists = shoppingListRepository.findByOwnerUserName(userName);
		if (lists.isEmpty())
			throw new ListNotFoundException(userName);
		return lists;
	}

	public Collection<ListItem> getItemsForUsersListId(String userName, Long listId) {
			try{
				getShoppingListsForUser(userName); //check if user exists and has lists
			}catch(ListNotFoundException lnfe) {
				throw new ListNotFoundException(listId, userName);
			}
			Collection<ListItem> items = listItemRepository.findByParentListId(listId);
			
			return items;
		}
}
