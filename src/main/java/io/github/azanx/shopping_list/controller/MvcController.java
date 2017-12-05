/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.AppUserDTO;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.domain.ShoppingListDTO;
import io.github.azanx.shopping_list.service.UserService;

/**
 * @author Kamil Piwowarski
 *
 */
@Controller
@RequestMapping("/")
public class MvcController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MvcController.class);
	private final UserService userService;
	private String userName; // populated by @ModelAttribute method used for
								// easier access inside controller methods

	@Autowired
	public MvcController(UserService userService) {
		this.userService = userService;
	}

	/**
	 * @param userName
	 *            name of the user to retrieve
	 * @return userName attribute
	 */
	@ModelAttribute("userName")
	String getUserName() {
		String userName = "admin";
		// TODO change this method after implementing spring security
		LOGGER.debug("Controller for user {}", userName);

		this.userName = userName;
		return userName;
	}

	/**
	 * Send user to home page, currently just redirect to "list" page showing
	 * all his list. May change in future
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home() {
		LOGGER.debug("home() method of MvcController called for user: {}", userName);
		return "redirect:list";
	}

	/**
	 * Send user to a page showing all his lists
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView showLists() {
		LOGGER.debug("home() method of MvcController called for user: {}", userName);

		ModelAndView mav = new ModelAndView("showAllLists");
		List<ShoppingList> shoppingLists = userService.getShoppingListsForUser(userName);
		mav.addObject("shoppingLists", shoppingLists);
		mav.addObject("newList", new ShoppingListDTO());
		mav.addObject("listToDelete", new ShoppingListDTO());
		return mav;
	}

	/**
	 * adds new shopping list for the current user
	 * @param newList ShoppingListDTO with basic information about new list (must include list name)
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String addList(@ModelAttribute("newList") ShoppingListDTO newList, BindingResult result) {
		LOGGER.debug("addList() method of MvcController called for user: {}", userName);

		userService.addShoppingListToUserByName(userName, newList.getListName());

		return "redirect:/list";
	}
	
	@RequestMapping(value = "/list/delete", method = RequestMethod.POST)
	public String deleteList(@ModelAttribute("listToDelete") ShoppingListDTO listToDelete, BindingResult result) {
		userService.removeShoppingList(userName, listToDelete.getId());
		//TODO implement
		return "redirect:/list";
	}

	/**
	 * Send user to a page showing all items in given list (if accessible by current user)
	 * 
	 * @param listId
	 *            of the shopping list whose listItems to retrieve (listId - primary key from DB
	 */
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.GET)
	public ModelAndView showListWithId(@PathVariable Long listId) {
		LOGGER.debug("showListsWithId() method of MvcController called for user: {}, and list: {}", userName, listId);
		
		ModelAndView mav = new ModelAndView("showList");

		//get ShoppingList with given Id for current user populated with its items
		ShoppingList shoppingListWithExistingItems = userService.getShoppingListWithItemsForUsersListId(userName, listId);
		mav.addObject("listItems", shoppingListWithExistingItems);
		
		//create new shopping list backing form object (mainly to retrieve new items)
		ShoppingListDTO shoppingListWithEmptyItems= new ShoppingListDTO(10);
		mav.addObject("shoppingList", shoppingListWithEmptyItems); 
		// TODO implement
		return mav;
	}

	/**
	 * adds new item to the given shopping list (if editable by current user)
	 * @param newList ShoppingListDTO with basic information about new list (must include list name)
	 */
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.POST)
	public String addItemToListWithId(@PathVariable Long listId, @ModelAttribute("shoppingList") ShoppingListDTO newList, BindingResult result) {
		LOGGER.debug("addItemToListWithId() method of MvcController called for user: {}, and list: {}", userName, listId);
		//list should belong to current user as only owner can edit list! someone could've inserted other username to the DTO on client side and change the listId, so no sense in passing them
		newList.setOwnerName(userName);
		newList.setId(listId);
		userService.addItemsToShoppingList(userName, newList);
		return "redirect:/list/"+listId;
	}
	
	/**
	 * Send user to a page showing his profile (information about his account)
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView showUserProfile() {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", userName);

		ModelAndView mav = new ModelAndView("userProfile");
		AppUser user = userService.getUserIfExistsElseThrow(userName);
		mav.addObject("user", user);
		return mav;
		//TODO add basic html5 side validation (for example input type="email")
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String editUserProfile(@ModelAttribute AppUserDTO appUser) {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", userName);

		// TODO implement
		return "redirect:/profile";
	}
}
