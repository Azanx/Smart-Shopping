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
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;

@RestController
@RequestMapping(value = "/api/{userName}")
public class RestApiController {

	private final AppUserRepository appUserRepository;

	private final ListItemRepository listItemRepository;

	private final ShoppingListRepository shoppingListRepository;

	@Autowired
	public RestApiController(AppUserRepository appUserRepository, ListItemRepository listItemRepository,
			ShoppingListRepository shoppingListRepository) {
		super();
		this.appUserRepository = appUserRepository;
		this.listItemRepository = listItemRepository;
		this.shoppingListRepository = shoppingListRepository;
	}

	@RequestMapping
	AppUser getUserInformation(@PathVariable String userName) {
		return getUserIfExistsElseThrow(userName); //if user doesn't exist method throws exception
										//caught by RestControllerAdvice - hence no need for
										// return value and checking it's success
	}

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	Collection<ShoppingList> getShoppingListsForUserName(@PathVariable String userName) {
		getUserIfExistsElseThrow(userName); // method throws exception caught by
											// RestControllerAdvice
		return shoppingListRepository.findByOwnerUserName(userName);
	}
	
	@RequestMapping(value = "/list/{id}")
	Collection<ListItem> geItemsForListId(@PathVariable String userName, @PathVariable Long id) {
		return listItemRepository.findByParentListId(id);
	}

	/**
	 * Returns AppUser instance if user with userName exists
	 * 
	 * @throws UserNotFoundException if user with 'userName' doesn't exist
	 * 
	 */
	//throws clause not necessarry as it's an unchecked exception
	AppUser getUserIfExistsElseThrow(String userName) {
		return appUserRepository.findByUserName(userName)
				.orElseThrow(
				() -> new UserNotFoundException(userName)
				);
	}
}
