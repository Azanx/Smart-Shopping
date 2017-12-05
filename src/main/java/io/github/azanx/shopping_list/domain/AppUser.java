package io.github.azanx.shopping_list.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.github.azanx.shopping_list.domain.exception.ListTooLongException;

/**
 * Domain Class representing application user
 * @author Kamil Piwowarski
 *
 */
@Entity
@Table(name = "app_user") // without this annotation parameter it would be
							// mapped to table
							// "AppUser"
public class AppUser implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 207113748011038111L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
	@GenericGenerator(name = "native", strategy = "native")
	private Long id;

	@Column(name = "user_name", unique = true, nullable = false)
	private String userName;

	@Column(nullable = false)
	private String email;

	@JsonIgnore
	@OneToMany(mappedBy = "owner", cascade = javax.persistence.CascadeType.ALL, orphanRemoval = true)
	@OrderBy("listNo")
	private List<ShoppingList> shoppingLists = new ArrayList<>();

	@JsonIgnore
	@Column(name = "user_password", nullable = false)
	private String password; // probably to change when properly securing theapp

	protected AppUser() {
	} // JPA use

	public AppUser(String userName, String password, String email) {
		super();
		this.userName = userName;
		this.email = email;
		this.password = password;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<ShoppingList> getShoppingLists() {
		return shoppingLists;
	}

	/**
	 * Add given shopping list to this user
	 * @param newShoppingList instance of ShoppingList to be add for this user
	 * @return true if list added succesfully, false otherwise (couldn't add to the underlying collection)
	 * @throw IllegalArgumentException if ShoppingList isn't owned by this user instance
	 */
	public boolean addShoppingList(ShoppingList newShoppingList) {
		if(!newShoppingList.getOwner().equals(this))
			throw new IllegalArgumentException("list isn't owned by this AppUser instance!");
		return shoppingLists.add(newShoppingList);
		//TODO remove this method
	}

	/**
	 * Add shopping list of given name to this user. Remember to initialize the ShoppingList collection before calling or your get LazyInitializationException!!
	 * @param listName name of the new list to be created for this user
	 * @return reference to the new ShoppngList instance
	 */
	public ShoppingList addShoppingList(String listName) {
		Short listNo;
		ShoppingList newShoppingList;
		if (getShoppingLists().size()+1 > Short.MAX_VALUE) {
			throw new ListTooLongException(ListTooLongException.listType.SHOPPING_LIST, this.getId());
		} else {
			listNo = (short) (shoppingLists.size()+1);
			newShoppingList = new ShoppingList(listName, this, listNo);
		}
		shoppingLists.add(newShoppingList);
		return newShoppingList;
	}
	
	/**
	 * Add shopping list of given name to this user. Use if you know current ShoppingList collection size but don't have the collection initialize (for example, you checked it with query
	 * @param listName name of the new list to be created for this user
	 * @return reference to the new ShoppngList instance
	 */
	public ShoppingList addShoppingList(String listName, Short listNo) {
		ShoppingList newShoppingList;
			newShoppingList = new ShoppingList(listName, this, listNo);
		return newShoppingList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime +  userName.hashCode(); //no need for null check as userName must be not null
		//TODO readd nullchecks when finished - this method generally shouldn't throw exceptions
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AppUser))
			return false;
		AppUser other = (AppUser) obj;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AppUser [userName=" + userName + ", email=" + email + "]";
	}
}
