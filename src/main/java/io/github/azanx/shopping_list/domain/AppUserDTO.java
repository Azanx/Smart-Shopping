/**
 * 
 */
package io.github.azanx.shopping_list.domain;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
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
//TODO add custom validation for passwords
	@NotEmpty(message = "*password can't be empty")
	@Size(min = 6, message = "*password must have minimum 6 characters!")
	private char[] password;

	@NotEmpty(message = "*password can't be empty")
	private char[] passwordVerification;

	public AppUserDTO() {
	}

	public AppUserDTO(AppUser user) {
		this.userName = user.getUserName();
		this.email = user.getEmail();
		this.password = user.getPassword().toCharArray();
	}

	public AppUserDTO(String userName, char[] password, String email) {
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

	public char[] getPassword() {
		return password;
	}

	public void setPassword(char[] password) {
		this.password = password;
	}

	public char[] getPasswordVerification() {
		return passwordVerification;
	}

	public void setPasswordVerification(char[] password2) {
		this.passwordVerification = password2;
	}

	@Override
	public String toString() {
		return "AppUserDTO [userName=" + userName + ", email=" + email + "]";
	}
}
