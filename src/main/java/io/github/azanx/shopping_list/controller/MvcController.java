/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.service.UserService;

/**
 * @author Kamil Piwowarski
 *
 */
@Controller
@RequestMapping("/{userName}")
public class MvcController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MvcController.class);
	private final UserService userService;
	private String userName; //populated by @ModelAttribute method used for easier access inside controller methods
	
	@Autowired
	public MvcController(UserService userService) {
		this.userService = userService;
	}
	
	@ModelAttribute("userName")
	String getUserName(@PathVariable String userName){
		LOGGER.debug("Controller for user {}",userName);
		this.userName = userName;
		return userName;
	}
	
	/**
	 * Send user to a page showing all his lists
	 * @param userName name of the user to retrieve
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home() {
		ModelAndView mav = new ModelAndView("showAllLists");
//		mav.addObject("userName", userName);
		Set<ShoppingList> shoppingLists = userService.getShoppingListsForUser(userName);
		mav.addObject("shoppingLists", shoppingLists);
		return mav;
	}
	
	@RequestMapping(value="list", method = RequestMethod.POST)
	public ModelAndView addList(@ModelAttribute("newListName") String newListName) {
		userService.addShoppingListToUserByName(userName, newListName);
		//TODO implement
		return null;
	}
	
	/**
	 * Send user to a page showing all items in given list
	 * @param userName name of the user to retrieve
	 * @param listNo of the shopping list whose listItems to retrieve (listNo field, not Id used as primary key in database)
	 */
	@RequestMapping(value = "/list/{listNo}", method = RequestMethod.GET)
	public ModelAndView showLists(String userName, @PathVariable Short listNo) {
		ModelAndView mav = new ModelAndView("showList");
		mav.addObject("userName", userName);
		//TODO implement
		return mav;
	}
	
	/**
	 * Send user to a page showing his profile (information about his account)
	 * @param userName name of the user to retrieve
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView showUserProfile() {
		ModelAndView mav = new ModelAndView("userProfile");
		//TODO implement
		return mav;
	}
}
