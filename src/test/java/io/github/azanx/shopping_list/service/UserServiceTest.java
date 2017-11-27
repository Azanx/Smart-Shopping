/**
 * 
 */
package io.github.azanx.shopping_list.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import io.github.azanx.shopping_list.config.JPAConfig;
import io.github.azanx.shopping_list.domain.AppUser;
import io.github.azanx.shopping_list.repository.AppUserRepository;
import io.github.azanx.shopping_list.repository.ListItemRepository;
import io.github.azanx.shopping_list.repository.ShoppingListRepository;
import io.github.azanx.shopping_list.service.exception.DuplicateUserException;
import io.github.azanx.shopping_list.service.exception.UserNotFoundException;

/**
 * @author Kamil Piwowarski
 *
 *
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(loader = AnnotationConfigContextLoader.class, classes = { JPAConfig.class })
public class UserServiceTest {

	@Autowired
	AppUserRepository appUserRepository;
	@Autowired
	ListItemRepository listItemRepository;
	@Autowired
	ShoppingListRepository shoppingListRepository;

	UserService userService;

	// clean repositories, create fresh userService instance
	@Before
	public void setup() throws Exception {

		this.appUserRepository.deleteAll();
		this.listItemRepository.deleteAll();
		this.shoppingListRepository.deleteAll();

		userService = new UserService(appUserRepository, listItemRepository, shoppingListRepository);
		userService.init(); // have to call it explicitly as I'm not autowiring
							// the class so POST_CONSTRUCT wouldn't start

	}

	// admin should be present after initialising the database during
	// userService construction
	@Test
	public void isAdminPresent() {
		AppUser userUnderTest = userService.getUserIfExistsElseThrow("admin");
		assertNotNull(userUnderTest);
	}

	@Test(expected = UserNotFoundException.class)
	public void throwsExceptionForNonExistingUser() {
		userService.getUserIfExistsElseThrow("fake_accout");
	}

	@Test
	public void addUserSuccessfull() {
		AppUser userUnderTest = new AppUser("User1", "password", "email@test.com");
		userService.addUser(userUnderTest);
	}

	@Test(expected = DuplicateUserException.class)
	public void addDuplicateUserFails() {
		AppUser userUnderTest = new AppUser("User1", "password", "email@test.com");
		userService.addUser(userUnderTest);
		userService.addUser(userUnderTest);
	}
}
