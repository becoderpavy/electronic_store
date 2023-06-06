package com.electronic.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.electronic.security.JwtAuthenticationEntryPoint;
import com.electronic.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtAuthenticationEntryPoint authenticationEntryPoint;

	@Autowired
	private JwtAuthenticationFilter authenticationFilter;

	private final String[] public_urls = { "/swagger-ui/**", "webjars/**", "/swagger-resources/**", "/v3/api-docs",
			"/v2/api-docs" };

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/*
	 * @Bean public UserDetailsService userDetailsService() { UserDetails normal =
	 * User.builder().username("user").password(passwordEncoder().encode("1234")).
	 * roles("NORMAL") .build();
	 * 
	 * UserDetails admin =
	 * User.builder().username("admin").password(passwordEncoder().encode("1234")).
	 * roles("ADMIN") .build();
	 * 
	 * return new InMemoryUserDetailsManager(normal, admin); }
	 */

	@Bean
	public DaoAuthenticationProvider getDaoAuthenticationProvider() {
		DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
		daoAuthenticationProvider.setUserDetailsService(userDetailsService);
		daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
		return daoAuthenticationProvider;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		/*
		 * http.authorizeHttpRequests().anyRequest().authenticated().and().formLogin().
		 * loginPage("login.html")
		 * .loginProcessingUrl("/process-url").defaultSuccessUrl("/dashboard").
		 * failureUrl("error").and().logout() .logoutUrl("do-logout");
		 */

		http.csrf().disable().cors().disable().authorizeRequests().antMatchers("/auth/login").permitAll()
				.antMatchers(HttpMethod.POST, "/users/").permitAll().antMatchers("/auth/google").permitAll()
				.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN").antMatchers(public_urls).permitAll()

				.anyRequest().authenticated().and().exceptionHandling()
				.authenticationEntryPoint(authenticationEntryPoint).and().sessionManagement()
				.sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
		return auth.getAuthenticationManager();
	}

}
