package com.electronic.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.electronic.dto.JwtRequest;
import com.electronic.dto.JwtResponse;
import com.electronic.dto.UserDto;
import com.electronic.entites.User;
import com.electronic.exception.BadApiRequestException;
import com.electronic.security.JwtHelper;
import com.electronic.services.UserService;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@Api(value = "AuthController", description = " Apis for authentication operations")
public class AuthController {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private AuthenticationManager manager;

	@Autowired
	private UserService userService;

	@Autowired
	private JwtHelper helper;

	@Value("${googleClientId}")
	private String googleClientId;

	@Value("${newPassword}")
	private String newPassword;

	private Logger logger = LoggerFactory.getLogger(AuthController.class);

	@PostMapping("/login")
	public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request) {
		this.doAuthenticate(request.getEmail(), request.getPassword());

		UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());

		String token = this.helper.generateToken(userDetails);

		UserDto userDto = modelMapper.map(userDetails, UserDto.class);
		JwtResponse response = JwtResponse.builder().jwtToken(token).user(userDto).build();
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	private void doAuthenticate(String email, String password) {

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
		try {
			manager.authenticate(authentication);
		} catch (BadCredentialsException e) {
			throw new BadApiRequestException(" Invalid Username or Password  !!");
		}

	}

	@GetMapping("/current")
	public ResponseEntity<UserDto> getCurrentUser(Principal principal) {
		String name = principal.getName();
		return new ResponseEntity<>(modelMapper.map(userDetailsService.loadUserByUsername(name), UserDto.class),
				HttpStatus.OK);
	}

	// login with google api

	@PostMapping("/google")
	public ResponseEntity<JwtResponse> loginWithGoogle(@RequestBody Map<String, Object> data) throws IOException {

		// get id token from request
		String idToken = data.get("idToken").toString();

		NetHttpTransport httpTransport = new NetHttpTransport();

		JacksonFactory factory = new JacksonFactory();

		GoogleIdTokenVerifier.Builder verifier = new GoogleIdTokenVerifier.Builder(httpTransport, factory)
				.setAudience(Collections.singleton(googleClientId));

		GoogleIdToken googleIdToken = GoogleIdToken.parse(verifier.getJsonFactory(), idToken);

		GoogleIdToken.Payload payload = googleIdToken.getPayload();

		logger.info("payload :{}", payload);
		String email = payload.getEmail();

		User user = null;

		user = userService.findUserByEmailForGoogle(email).orElse(null);

		if (user == null) {
			user = saveUser(email, data.get("name").toString(), data.get("photoUrl").toString());
		}

		ResponseEntity<JwtResponse> login = this
				.login(JwtRequest.builder().email(user.getEmail()).password(newPassword).build());

		return login;
	}

	private User saveUser(String email, String name, String photoUrl) {

		UserDto userDto = UserDto.builder().name(name).email(email).password(newPassword).imageName(photoUrl)
				.roles(new HashSet<>()).build();

		UserDto user = userService.createUser(userDto);

		return this.modelMapper.map(user, User.class);
	}

}
