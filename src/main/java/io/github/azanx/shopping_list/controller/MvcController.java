/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import java.util.Set;

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

	//TODO change all jsp files to html5
	
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
		Set<ShoppingList> shoppingLists = userService.getShoppingListsForUser(userName);
		mav.addObject("shoppingLists", shoppingLists);
		mav.addObject("newList", new ShoppingListDTO());
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

		return "redirect:list";
	}

	/**
	 * Send user to a page showing all items in given list
	 * 
	 * @param listNo
	 *            of the shopping list whose listItems to retrieve (listNo
	 *            field, not Id used as primary key in database)
	 */
	@RequestMapping(value = "/list/{listNo}", method = RequestMethod.GET)
	public ModelAndView showLists(String userName, @PathVariable Short listNo) {
		LOGGER.debug("showLists() method of MvcController called for user: {}", userName);

		ModelAndView mav = new ModelAndView("showList");
		mav.addObject("userName", userName);
		// TODO implement
		return mav;
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
		return "redirect:profile";
	}
}
