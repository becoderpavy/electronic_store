package com.electronic.services;

import java.util.List;
import java.util.Optional;

import com.electronic.dto.PageableResponse;
import com.electronic.dto.UserDto;
import com.electronic.entites.User;

public interface UserService {

	UserDto createUser(UserDto userDto);

	UserDto updateUser(UserDto userDto, String userId);

	void deleteUser(String userId);

	/* List<UserDto> getAllUser(); */
	PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir);

	UserDto getUserById(String userId);

	UserDto getUserByEmail(String email);

	List<UserDto> searchUser(String keyword);

	Optional<User> findUserByEmailForGoogle(String email);

}
