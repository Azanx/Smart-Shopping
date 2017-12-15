/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import java.security.Principal;
import java.util.List;

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
	String getUserName(Principal principal) {
		LOGGER.debug("Controller for user {}", principal.getName());

		return principal.getName();
	}

	/**
	 * Send user to home page, currently just redirect to "list" page showing
	 * all his list. May change in future
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String home(Principal principal, Model model) {
		LOGGER.debug("home() method of MvcController called for user: {}", principal.getName());
		
		return "redirect:list";
	}

	/**
	 * Send user to a page showing all his lists
	 */
	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView showLists(Principal principal, Model model) {
		LOGGER.debug("home() method of MvcController called for user: {}", principal.getName());

		ModelAndView mav = new ModelAndView("showAllLists", model.asMap());
		List<ShoppingList> shoppingLists = repositoryService.getShoppingLists(principal.getName());
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
	public String addList(Principal principal, @Valid @ModelAttribute("newList") ShoppingListDTO newList, BindingResult binding, RedirectAttributes attr, HttpSession session) {
		LOGGER.debug("addList() method of MvcController called for user: {}", principal.getName());
		if(!binding.hasErrors()) 
			repositoryService.addShoppingListToUserByName(principal.getName(), newList.getListName());
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
	public String deleteList(Principal principal, @ModelAttribute("listToDelete") ShoppingListDTO listToDelete) {
		LOGGER.debug("deleteList() method of MvcController called for user: {} list: {}", principal.getName(), listToDelete.getId());
		
		repositoryService.removeShoppingList(principal.getName(), listToDelete.getId());
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
	public ModelAndView showListWithId(Principal principal, @PathVariable Long listId) {
		LOGGER.debug("showListsWithId() method of MvcController called for user: {}, and list: {}", principal.getName(), listId);

		ModelAndView mav = new ModelAndView("showList");
		ShoppingList shoppingListWithExistingItems = repositoryService.getShoppingListWithItems(principal.getName(),
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
	public String addItemToListWithId(Principal principal, @PathVariable Long listId,
			@ModelAttribute("shoppingList") ShoppingListDTO newList) {
		LOGGER.debug("addItemToListWithId() method of MvcController called for user: {}, and list: {}", principal.getName(),
				listId);

		// list should belong to current user as only owner can edit list!
		// someone could've inserted other username to the DTO on client side
		// and change the listId
		newList.setOwnerName(principal.getName());
		newList.setId(listId);
		repositoryService.addItemsToShoppingList(principal.getName(), newList);
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
	public String switchItemBoughtStatus(Principal principal, @PathVariable Long listId, @ModelAttribute ListItemDTO item) {
		LOGGER.debug(
				"switchItemBoughtStatus: method of MvcController called for user: {}, and listItem: {} from list: {}",
				principal.getName(), item.getId(), listId);
	
		item.setParentListId(listId);
		repositoryService.switchItemBoughtStatus(principal.getName(), item);
		return "redirect:/list/" + listId;
	}

	/**
	 * Send user to a page showing his profile (information about his account)
	 */
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	public ModelAndView showUserProfile(Principal principal) {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", principal.getName());

		ModelAndView mav = new ModelAndView("userProfile");
		AppUser user = repositoryService.getUser(principal.getName());
		mav.addObject("user", user);
		return mav;
		// TODO add basic html5 side validation (for example input type="email")
	}

	@RequestMapping(value = "/profile", method = RequestMethod.POST)
	public String editUserProfile(Principal principal, @ModelAttribute AppUserDTO appUser) {
		LOGGER.debug("showUserProfile() method of MvcController called for user: {}", principal.getName());

		// TODO implement
		return "redirect:/profile";
	}
}
