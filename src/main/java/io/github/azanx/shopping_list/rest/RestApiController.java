package io.github.azanx.shopping_list.rest;

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

	@RequestMapping
	AppUser getUserInformation(@PathVariable String userName) {
		return userService.getUserIfExistsElseThrow(userName); //if user doesn't exist method throws exception
										//caught by RestControllerAdvice - hence no need for
										// return value and checking it's success
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	Collection<ShoppingList> getShoppingListsForUserName(@PathVariable String userName) {
		userService.getUserIfExistsElseThrow(userName); // method throws exception caught by
											// RestControllerAdvice
		//return shoppingListRepository.findByOwnerUserName(userName);
		return userService.getShoppingListsForUser(userName);
	}
	
	@RequestMapping(value = "/list/{id}")
	Collection<ListItem> geItemsForListId(@PathVariable String userName, @PathVariable Long id) {
		return userService.getItemsForUsersListId(userName, id);
	}


}
