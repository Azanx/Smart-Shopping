package io.github.azanx.shopping_list.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import net.minidev.json.annotate.JsonIgnore;

@Entity
public class AppUser {

	@Id
	@GeneratedValue
	private Long ID;

	@OneToMany(mappedBy = "owner")
	private Set<ShoppingList> userLists = new HashSet<>();

	private String name;
	private String email;
	@JsonIgnore
	private String password; // probably to change while securing app

	public Long getID() {
		return ID;
	}

	public void setID(Long iD) {
		ID = iD;
	}

	public Set<ShoppingList> getUserLists() {
		return userLists;
	}

	public void setUserLists(Set<ShoppingList> userLists) {
		this.userLists = userLists;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
}
