package com.electronic.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtHelper jwtHelper;

	private Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private UserDetailsService userDetailsService;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		// authorization

		String requestHeader = request.getHeader("Authorization");
		// Bearer 3453523sdfsfdc34523
		logger.info("Header :{}", requestHeader);

		String username = null;
		String token = null;

		if (requestHeader != null && requestHeader.startsWith("Bearer")) {

			token = requestHeader.substring(7);
			try {
				username = jwtHelper.getUsernameFromToken(token);
			} catch (IllegalArgumentException e) {
				logger.info("illegal while fetching the username");
				e.printStackTrace();
			} catch (ExpiredJwtException e) {
				logger.info("token expired");
				e.printStackTrace();
			} catch (MalformedJwtException e) {
				logger.info("some changes in token invalid");
				e.printStackTrace();
			} catch (Exception e) {
				logger.info("excption");
				e.printStackTrace();
			}

		} else {
			logger.info("invalid Header value !!");
		}

		if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			// fetch user deatils from
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
			boolean validateToken = jwtHelper.validateToken(token, userDetails);
			if (validateToken) {
				// set the authentication
				UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
						userDetails, null, userDetails.getAuthorities());
				authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authentication);

			} else {
				logger.info("validation fails");
			}
		}

		filterChain.doFilter(request, response);

	}

}
