/**
 * 
 */
package io.github.azanx.shopping_list.domain;

/**
 * @author Kamil Piwowarski
 *
 */
public class AppUserDTO {

	private String userName;

	private String email;

	private String password;

	public AppUserDTO() {
	}
	
	public AppUserDTO(AppUser user) {
		this.userName = user.getUserName();
		this.email = user.getEmail();
		this.password = user.getPassword();
	}

	public AppUserDTO(String userName, String email, String password) {
		this.userName = userName;
		this.email = email;
		this.password = password;
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
}
