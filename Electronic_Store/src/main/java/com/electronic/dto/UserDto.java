package com.electronic.dto;

import java.util.HashSet;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.electronic.validate.ImageNameValid;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class UserDto {

	private String userId;

	@Size(min = 3, max = 10, message = "invalid name")
	@ApiModelProperty(value = "user_name", name = "username", required = true)
	private String name;

	@Email
	// @Pattern(regexp = "")
	private String email;

	@NotBlank(message = "password is required")
	private String password;

	@NotBlank
	private String gender;

	@NotBlank(message = "write something about your self")
	private String about;

	@ImageNameValid
	private String imageName;

	private Set<RoleDto> roles = new HashSet<RoleDto>();

}
