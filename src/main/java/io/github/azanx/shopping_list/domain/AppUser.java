package io.github.azanx.shopping_list.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "app_user") // without this annotation parameter it would be mapped to table
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
	private List<ShoppingList> shoppingList = new ArrayList<>();

	@JsonIgnore
	@Column(name = "user_password", nullable = false)
	private String password; // probably to change when properly securing the
								// app

	protected AppUser() {
	} // JPA use

	public AppUser(String userName, String password, String email){
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

	public List<ShoppingList> getShoppingList() {
		return shoppingList;
	}

	public boolean addShoppingList(ShoppingList newShoppingList) {
		return this.getShoppingList().add(newShoppingList);
	}
	
	public ShoppingList addShoppingList(String listName) {
		ShoppingList newShoppingList = new ShoppingList(listName, this);
		this.getShoppingList().add(newShoppingList);
		return newShoppingList;
	}
	
}
