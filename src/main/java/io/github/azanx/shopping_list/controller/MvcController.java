/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.domain.AppUserDTO;
import io.github.azanx.shopping_list.domain.ListItemDTO;
import io.github.azanx.shopping_list.domain.ShoppingList;
import io.github.azanx.shopping_list.domain.ShoppingListDTO;
import io.github.azanx.shopping_list.service.RepositoryService;

/**
 * @author Kamil Piwowarski
 *
 */
@Controller
@RequestMapping("/")
public class MvcController {

	private static final Logger LOGGER = LoggerFactory.getLogger(MvcController.class);

	private final RepositoryService repositoryService;
	private String userName; // populated by @ModelAttribute method used for
								// easier access inside controller methods

	/**
	 * Initialize controller and it's repository service
	 * @param repositoryService used to communicate with DB repositories 
	 */
	@Autowired
	public MvcController(RepositoryService userService) {
		this.repositoryService = userService;
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
	public ModelAndView showLists(Model model) {
		LOGGER.debug("home() method of MvcController called for user: {}", userName);

		ModelAndView mav = new ModelAndView("showAllLists", model.asMap());
		List<ShoppingList> shoppingLists = repositoryService.getShoppingLists(userName);
		mav.addObject("shoppingLists", shoppingLists); //current ShoppingLists of the user to display
		if(!model.containsAttribute("newList"))
			mav.addObject("newList", new ShoppingListDTO()); //backing object for name of the new ShoppingList
		mav.addObject("listToDelete", new ShoppingListDTO()); //backing object for ShoppingList to delete
		return mav;
	}

	/**
	 * adds new shopping list for the current user
	 * 
	 * @param newList
	 *            ShoppingListDTO with basic information about new list (must
	 *            include list name)
	 */
	@RequestMapping(value = "/list", method = RequestMethod.POST)
	public String addList(@Valid @ModelAttribute("newList") ShoppingListDTO newList, BindingResult binding, RedirectAttributes attr, HttpSession session) {
		LOGGER.debug("addList() method of MvcController called for user: {}", userName);
		if(!binding.hasErrors()) 
			repositoryService.addShoppingListToUserByName(userName, newList.getListName());
		else {
			attr.addFlashAttribute("org.springframework.validation.BindingResult.newList", binding);
			attr.addFlashAttribute("newList", newList);
			for(FieldError ferr:binding.getFieldErrors()) {
				LOGGER.info("addList(): field error: " + ferr.getDefaultMessage());
			}
		}
		
		return "redirect:/list";
	}

	/**
	 * Delete given ShoppingList of the user
	 * @param listToDelete ShoppingList to delete for this user
	 */
	@RequestMapping(value = "/list/delete", method = RequestMethod.POST)
	public String deleteList(@ModelAttribute("listToDelete") ShoppingListDTO listToDelete) {
		LOGGER.debug("deleteList() method of MvcController called for user: {} list: {}", userName, listToDelete.getId());
		
		repositoryService.removeShoppingList(userName, listToDelete.getId());
		return "redirect:/list";
	}

	/**
	 * Send user to a page showing all items in given list (if accessible by
	 * current user)
	 * 
	 * @param listId
	 *            of the shopping list whose listItems to retrieve (listId -
	 *            primary key from DB
	 */
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.GET)
	public ModelAndView showListWithId(@PathVariable Long listId) {
		LOGGER.debug("showListsWithId() method of MvcController called for user: {}, and list: {}", userName, listId);

		ModelAndView mav = new ModelAndView("showList");
		ShoppingList shoppingListWithExistingItems = repositoryService.getShoppingListWithItems(userName,
				listId);
		ShoppingListDTO shoppingListWithEmptyItems = new ShoppingListDTO(10);
		ListItemDTO listItemToModify = new ListItemDTO();
		mav.addObject("listItems", shoppingListWithExistingItems); //ShoppingList containing ListItems to display
		mav.addObject("shoppingList", shoppingListWithEmptyItems); //backing object for new ListItems to add
		mav.addObject("listItemToModify", listItemToModify); //backing object for list object to modify (set as bought / not bought)
		return mav;
	}

	/**
	 * adds new item to the given shopping list (if editable by current user)
	 * 
	 * @param newList
	 *            ShoppingListDTO with basic information about new list (must
	 *            include list name)
	 */
	@RequestMapping(value = "/list/{listId}", method = RequestMethod.POST)
	public String addItemToListWithId(@PathVariable Long listId,
			@ModelAttribute("shoppingList") ShoppingListDTO newList) {
		LOGGER.debug("addItemToListWithId() method of MvcController called for user: {}, and list: {}", userName,
				listId);

		// list should belong to current user as only owner can edit list!
		// someone could've inserted other username to the DTO on client side
		// and change the listId
		newList.setOwnerName(userName);
		newList.setId(listId);
		repositoryService.addItemsToShoppingList(userName, newList);
		return "redirect:/list/" + listId;
	}

	/**
	 * Change status of the given item "bought" field as specified in
	 * ListItemDTO item
	 * 
	 * @param listId
	 *            of the shopping list whose listItems to retrieve (listId -
	 *            primary key from DB
	 * @param item
	 *            ListItemDTO containing listId and new 'bought' value
	 */
	@RequestMapping(value = "/list/{listId}/setBought", method = RequestMethod.POST)
	public String switchItemBoughtStatus(@PathVariable Long listId, @ModelAttribute ListItemDTO item) {
		LOGGER.debug(
				"switchItemBoughtStatus: method of MvcController called for user: {}, and listItem: {} from list: {}",
				userName, item.getId(), listId);
	
		item.setParentListId(listId);
		repositoryService.switchItemBoughtStatus(userName, item);
		return "redirect:/list/" + listId;
	}

	/**
	 * Send user to a page showing his profile (information about his account)
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView showUserProfile() {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", userName);

		ModelAndView mav = new ModelAndView("userProfile");
		AppUser user = repositoryService.getUser(userName);
		mav.addObject("user", user);
		return mav;
		// TODO add basic html5 side validation (for example input type="email")
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String editUserProfile(@ModelAttribute AppUserDTO appUser) {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", userName);

		// TODO implement
		return "redirect:/profile";
	}
	
	/**
	 * Send user to a page allowing to login into his/her account
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ModelAndView showLoginPage(Model model) {
		LOGGER.debug("showLoginPage() method of MvcController called");
		ModelAndView mav = new ModelAndView("login", model.asMap());
		if(!model.containsAttribute("user")) //model can already contain this attribute if there was binding error after sending form 
			mav.addObject("user", new AppUserDTO());
		return mav;
	}
	
	/**
	 * Logs the user into the system or sends him back to login form
	 * @param user AppUserDTO containing login details
	 * @param binding BindingResult
	 * @param attr RedirectAttribute to pass back to login form in case of login failure
	 */
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String loginUser(@Valid @ModelAttribute AppUserDTO user, BindingResult binding, RedirectAttributes attr) {
		LOGGER.debug("loginUser() method of MvcController called");
		if(binding.hasErrors())
		{
			attr.addFlashAttribute("org.springframework.validation.BindingResult.user", binding);
			attr.addFlashAttribute("user", user);
			LOGGER.debug("loginUser(): field errors: {}",//
					binding.getFieldErrors()//
						.stream()//
							.map(FieldError::getDefaultMessage)//
								.collect(Collectors.toList()));//
		}
		//TODO implement
		return "redirect:/login";
	}
}
