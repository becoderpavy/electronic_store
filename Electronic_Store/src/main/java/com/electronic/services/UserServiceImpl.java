package com.electronic.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.electronic.dto.PageableResponse;
import com.electronic.dto.UserDto;
import com.electronic.entites.Role;
import com.electronic.entites.User;
import com.electronic.exception.ResourceNotFoundException;
import com.electronic.helper.Helper;
import com.electronic.repository.RoleRepository;
import com.electronic.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private ModelMapper mapper;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Value("${user_id}")
	private String user_id;

	@Autowired
	private RoleRepository roleRepo;

	@Override
	public UserDto createUser(UserDto userDto) {

		User user = dtoToEntity(userDto);

		Role role = roleRepo.findById(user_id).get();
		user.getRoles().add(role);
		user.setUserId(UUID.randomUUID().toString());
		user.setPassword(passwordEncoder.encode(user.getPassword()));
		User newUser = userRepo.save(user);

		return entityToDto(newUser);
	}

	@Override
	public UserDto updateUser(UserDto userDto, String userId) {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

		user.setName(userDto.getName());
		user.setAbout(userDto.getAbout());
		user.setGender(userDto.getGender());
		user.setPassword(userDto.getPassword());
		user.setImageName(userDto.getImageName());

		User updateUser = userRepo.save(user);

		return entityToDto(updateUser);
	}

	@Override
	public void deleteUser(String userId) {

		User user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("user not found"));

		// delete user image

		userRepo.delete(user);

		String fullPath = imageUploadPath + user.getImageName();
		Path path = Paths.get(fullPath);
		try {
			Files.delete(path);
		} catch (IOException e) {
			logger.info("images not found with : {}", user.getUserId());
			e.printStackTrace();
		}
	}

	/*
	 * @Override public List<UserDto> getAllUser() { return
	 * userRepo.findAll().stream().map(e ->
	 * entityToDto(e)).collect(Collectors.toList()); }
	 */

	@Override
	public PageableResponse<UserDto> getAllUser(int pageNumber, int pageSize, String sortBy, String sortDir) {
		// Sort sort = Sort.by(sortBy);
		Sort sort = (sortDir.equalsIgnoreCase("desc")) ? (Sort.by(sortBy).descending()) : (Sort.by(sortBy).ascending());

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		Page<User> page = userRepo.findAll(pageable);
		/*
		 * List<User> list = page.getContent(); List<UserDto> listDto =
		 * list.stream().map(e -> entityToDto(e)).collect(Collectors.toList());
		 * 
		 * PageableResponse<UserDto> resp = new PageableResponse<UserDto>();
		 * resp.setContent(listDto); resp.setPageNumber(page.getNumber());
		 * resp.setPageSize(page.getSize());
		 * resp.setTotalElements(page.getTotalElements());
		 * resp.setTotalPages(page.getTotalPages()); resp.setLastPage(page.isLast());
		 */

		PageableResponse<UserDto> pageableResponse = Helper.getPageableResponse(page, UserDto.class);

		return pageableResponse;
	}

	@Override
	public UserDto getUserById(String userId) {
		User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("user not found"));
		return entityToDto(user);
	}

	@Override
	public UserDto getUserByEmail(String email) {
		User user = userRepo.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("user not found"));
		return entityToDto(user);
	}

	@Override
	public List<UserDto> searchUser(String keyword) {
		return userRepo.findByNameContaining(keyword).stream().map(e -> entityToDto(e)).collect(Collectors.toList());
	}

	public User dtoToEntity(UserDto userDto) {
		/*
		 * User user =
		 * User.builder().userId(userDto.getUserId()).name(userDto.getName()).email(
		 * userDto.getEmail())
		 * .password(userDto.getPassword()).gender(userDto.getGender()).about(userDto.
		 * getAbout()) .imageName(userDto.getImageName()).build();
		 */

		return mapper.map(userDto, User.class);
	}

	public UserDto entityToDto(User user) {
		/*
		 * UserDto userDto =
		 * UserDto.builder().userId(user.getUserId()).name(user.getName()).email(user.
		 * getEmail())
		 * .password(user.getPassword()).gender(user.getGender()).about(user.getAbout())
		 * .imageName(user.getImageName()).build();
		 */

		return mapper.map(user, UserDto.class);
	}

	@Override
	public Optional<User> findUserByEmailForGoogle(String email) {
		return userRepo.findByEmail(email);
	}

}
