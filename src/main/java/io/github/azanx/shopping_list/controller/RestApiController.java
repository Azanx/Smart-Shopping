package io.github.azanx.shopping_list.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.service.UserService;

/**
 * Controller for the REST API
 * @author Kamil Piwowarski
 *
 */
@RestController
@RequestMapping(value = "/api/{userName}")
public class RestApiController {

	private final UserService userService;

	@Autowired
	public RestApiController( UserService userService, ListItemRepository listItemRepository,
			ShoppingListRepository shoppingListRepository) {
		super();
		this.userService = userService;
	}

	/**
	 * Retrieves basic information about the user which can be used to create AppUser instance 
	 * @param userName name of the user to retrieve
	 *
	 */
	@RequestMapping
	AppUser getUserInformation(@PathVariable String userName) {
		return userService.getUserIfExistsElseThrow(userName); //if user doesn't exist method throws exception
										//caught by RestControllerAdvice - hence no need for
										// return value and checking it's success
	}

	/**
	 * Retrieves lists belonging to the user
	 * @param userName name of the user whose lists to retrieve
	 * 
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	Collection<ShoppingList> getShoppingListsForUserName(@PathVariable String userName) {
		return userService.getShoppingListsForUser(userName);// method throws exception caught by
										// RestControllerAdvice
	}
	
	/**
	 * Retrieves Items belonging to the list
	 * @param userName name of the user whose listItems to retrieve
	 * @param listId of the shopping list whose listItems to retrieve (listId field, not Id used as primary key in database) 
	 * 
	 */
	@RequestMapping(value = "/list/{id}")
	Collection<ListItem> geItemsForListId(@PathVariable String userName, @PathVariable Short listId) {
		return userService.getItemsForUsersListId(userName, listId);
	}


}
