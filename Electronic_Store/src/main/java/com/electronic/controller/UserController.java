package com.electronic.controller;

import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.electronic.dto.ApiResponseMessage;
import com.electronic.dto.ImageResponse;
import com.electronic.dto.UserDto;
import com.electronic.services.FileService;
import com.electronic.services.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/users")
@Api(value = "UserController", description = "APIs for user operations")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private FileService fileService;

	@Value("${user.profile.image.path}")
	private String imageUploadPath;

	private Logger logger = LoggerFactory.getLogger(HomeController.class);

	@GetMapping("/demo")
	public ResponseEntity<?> demo(MultipartFile file) {
		return new ResponseEntity<>("Ok", HttpStatus.OK);
	}

	@PostMapping("/")
	@ApiResponses(value = { @ApiResponse(code = 201, message = "new used created !!"),
			@ApiResponse(code = 401, message = "Not Authorized") })
	public ResponseEntity<?> createUser(@Valid @RequestBody UserDto user) {
		return new ResponseEntity<>(userService.createUser(user), HttpStatus.CREATED);
	}

	@PutMapping("/{userid}")
	public ResponseEntity<?> updateUser(@RequestBody UserDto userDto, @PathVariable String userid) {
		return new ResponseEntity<>(userService.updateUser(userDto, userid), HttpStatus.OK);
	}

	@DeleteMapping("/{userId}")
	public ResponseEntity<?> deleteUser(@PathVariable String userId) {
		userService.deleteUser(userId);
		ApiResponseMessage msg = ApiResponseMessage.builder().message("User Delete Sucessfully").status(HttpStatus.OK)
				.success(true).build();
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

//	@GetMapping("/")
//	public ResponseEntity<?> getAllUser() {
//		return new ResponseEntity<>(userService.getAllUser(), HttpStatus.OK);
//	}

	@GetMapping("/")
	@ApiOperation(value = "get All Users", response = ResponseEntity.class, tags = { "user-controller" })
	public ResponseEntity<?> getAllUser(
			@RequestParam(value = "pageNumber", defaultValue = "0", required = false) int pageNumber,
			@RequestParam(value = "pageSize", defaultValue = "3", required = false) int pageSize,
			@RequestParam(value = "sortBy", defaultValue = "name", required = false) String sortBy,
			@RequestParam(value = "sortDir", defaultValue = "asc", required = false) String sortDir) {

		return new ResponseEntity<>(userService.getAllUser(pageNumber, pageSize, sortBy, sortDir), HttpStatus.OK);
	}

	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserById(@PathVariable String userId) {
		return new ResponseEntity<>(userService.getUserById(userId), HttpStatus.OK);
	}

	@GetMapping("/email/{email}")
	public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
		return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
	}

	@GetMapping("/search/{keyword}")
	public ResponseEntity<?> getUserBySearch(@PathVariable String keyword) {
		return new ResponseEntity<>(userService.searchUser(keyword), HttpStatus.OK);
	}

	@PostMapping("/image/{userId}")
	public ResponseEntity<ImageResponse> uploadUserImage(@PathVariable String userId,
			@RequestParam("user_img") MultipartFile file) throws IOException {

		String imageName = fileService.uploadFile(file, imageUploadPath);
		ImageResponse imgRes = ImageResponse.builder().imageName(imageName).success(true).status(HttpStatus.CREATED)
				.message("image upload sucessfully").build();
		UserDto user = userService.getUserById(userId);
		user.setImageName(imageName);
		UserDto updateUser = userService.updateUser(user, userId);
		return new ResponseEntity<ImageResponse>(imgRes, HttpStatus.CREATED);

	}

	@GetMapping("/image/{userid}")
	public void serveUserImage(@PathVariable String userid, HttpServletResponse resp) throws IOException {
		UserDto user = userService.getUserById(userid);

		logger.info("user image name :{}", user.getImageName());
		InputStream ios = fileService.getFile(imageUploadPath, user.getImageName());

		resp.setContentType(MediaType.IMAGE_JPEG_VALUE);
		StreamUtils.copy(ios, resp.getOutputStream());
	}

}
