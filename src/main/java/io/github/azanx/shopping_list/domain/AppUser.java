package io.github.azanx.shopping_list.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Domain Class representing application user
 * @author Kamil Piwowarski
 *
 */
@Entity
@Table(name = "app_user") // without this annotation parameter it would be
							// mapped to table
							// "AppUser"
public class AppUser {

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
	private Set<ShoppingList> shoppingList = new HashSet<>();

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

	public Set<ShoppingList> getShoppingList() {
		return shoppingList;
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
		return this.getShoppingList().add(newShoppingList);
	}

	/**
	 * Add shopping list of given name to this user
	 * @param listName name of the new list to be created for this user
	 * @return reference to the new ShoppngList instance
	 */
	public ShoppingList addShoppingList(String listName) {
		ShoppingList newShoppingList = new ShoppingList(listName, this);
		this.getShoppingList().add(newShoppingList);
		return newShoppingList;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = prime +  userName.hashCode(); //no need for null check as userName must be not null
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
		return "AppUser [userName=" + userName + ", email=" + email + ", password=" + password + "]";
	}
}
