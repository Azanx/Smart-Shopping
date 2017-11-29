/**
 * 
 */
package io.github.azanx.shopping_list.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import io.github.azanx.shopping_list.service.UserService;

/**
 * @author Kamil Piwowarski
 *
 */
@Controller
@RequestMapping("/{userName}")
public class MvcController {

	private final UserService userService;
	
	@Autowired
	public MvcController(UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Send user to a page showing all his lists
	 * @param userName name of the user to retrieve
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView home(@PathVariable String userName) {
		ModelAndView mav = new ModelAndView("showAllLists");
		mav.addObject("userName", userName);
		//TODO implement
		return mav;
	}
	
	/**
	 * Send user to a page showing all items in given list
	 * @param userName name of the user to retrieve
	 * @param listNo of the shopping list whose listItems to retrieve (listNo field, not Id used as primary key in database)
	 */
	@RequestMapping(value = "/list/{listNo}", method = RequestMethod.GET)
	public ModelAndView showLists(@PathVariable String userName, @PathVariable Short listNo) {
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
	public ModelAndView showUserProfile(@PathVariable String userName) {
		ModelAndView mav = new ModelAndView("userProfile");
		mav.addObject("userName", userName);
		//TODO implement
		return mav;
	}
}
