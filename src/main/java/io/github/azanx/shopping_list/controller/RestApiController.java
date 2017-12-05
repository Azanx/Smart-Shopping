package io.github.azanx.shopping_list.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.ListItem;
import io.github.azanx.shopping_list.domain.ShoppingList;
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
	public RestApiController( UserService userService) {
		super();
		this.userService = userService;
	}

	/**
	 * Retrieves basic information about the user which can be used to create AppUser instance 
	 * @param userName name of the user to retrieve
	 *
	 */
	@RequestMapping(method = RequestMethod.GET)
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
	List<ShoppingList> getShoppingListsForUserName(@PathVariable String userName) {
		return userService.getShoppingListsForUser(userName);// method throws exception caught by
										// RestControllerAdvice
	}
	
	/**
	 * Retrieves all Items belonging to the list
	 * @param userName name of the user whose listItems to retrieve
	 * @param listId of the shopping list whose listItems to retrieve (listId field, not Id used as primary key in database) 
	 * 
	 */
	@RequestMapping(value = "/list/{listNo}", method = RequestMethod.GET)
	List<ListItem> getAllItemsFromList(@PathVariable String userName, @PathVariable Short listNo) {
		return userService.getItemsForUsersListNo(userName, listNo);
	}
	
	/**
	 * Retrieves single item from the list
	 * @param userName name of the user whose listItems to retrieve
	 * @param listId of the shopping list whose listItems to retrieve (listId field, not Id used as primary key in database)
	 * @param itemId of the item we wan't to retrieve (itemId field, not Id used as primary key in database)
	 * 
	 */
	@RequestMapping(value = "list/{listNo}/{itemId}", method = RequestMethod.GET)
	ListItem getSingleItemFromList(@PathVariable String userName, @PathVariable Short listNo, @PathVariable Short itemId) {
		//TODO implement
		return null;
	}

	@RequestMapping(value = "/list", method = RequestMethod.POST)
	ResponseEntity<?> addList(@PathVariable String userName, @RequestBody ShoppingList newList) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.POST)
	ResponseEntity<?> addListItemsToList(@PathVariable String userName, @PathVariable Short listId, @RequestBody List<ListItem> newItems) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "/list", method = RequestMethod.PUT)
	ResponseEntity<?> updateList(@PathVariable String userName, @RequestBody ShoppingList updatedList) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.PUT)
	ResponseEntity<?> updateListItemsOnList(@PathVariable String userName, @PathVariable Short listId, @RequestBody List<ListItem> updatedItems) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "/list/{listId}/remove", method = RequestMethod.PUT)
	ResponseEntity<?> removeMultipleListItemsFromList(@PathVariable String userName, @PathVariable Short listId, @RequestBody List<ListItem> updatedItems) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.DELETE)
	ResponseEntity<?> removeList(@PathVariable String userName, @PathVariable Short listId) {
		//TODO implement
		return null;
	}
	
	@RequestMapping(value = "list/{listId}/{itemId}", method = RequestMethod.DELETE)
	ResponseEntity<?> removeSingleItemFromList(@PathVariable String userName, @PathVariable Short listId, @PathVariable Short itemId) {
		//TODO implement
		return null;
	}
}
