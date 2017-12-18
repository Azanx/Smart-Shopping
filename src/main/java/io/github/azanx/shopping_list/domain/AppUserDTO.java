/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import io.github.azanx.shopping_list.domain.validation.EmailVerification;
import io.github.azanx.shopping_list.domain.validation.FieldsVerification;

/**
 * @author Kamil Piwowarski
 *
 */
@FieldsVerification.List({
		@FieldsVerification(field = "password", fieldMatch = "passwordVerification", message = "*Passwords do not match") })
public class AppUserDTO {

	@NotBlank(message = "*username can't be empty")
	@Size(min = 5, max = 15, message = "*username must be beetween 5 and 15 characters")
	private String userName;

	@NotBlank(message = "*email can't be empty")
	@EmailVerification
	private String email;

	@NotBlank(message = "*password can't be empty")
	private String password;

	@NotBlank(message = "*password can't be empty")
	private String passwordVerification;

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

	public String getPasswordVerification() {
		return passwordVerification;
	}

	public void setPasswordVerification(String password2) {
		this.passwordVerification = password2;
	}

	@Override
	public String toString() {
		return "AppUserDTO [userName=" + userName + ", email=" + email + "]";
	}
}
