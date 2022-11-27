package com.eazybytes.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {
	@Autowired
	private UserDetailsService jdbcUserDetailsManager;

	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@GetMapping("/registration")
	public String sayHello() {
		
		
			return "registration";
	}

	@PostMapping("/saveUser")
	@ResponseBody
	public String saveHello(@RequestParam String username, @RequestParam String password, ModelMap model) {
		
		boolean flag = ((JdbcUserDetailsManager) jdbcUserDetailsManager).userExists(username);
		if (flag)
			return "\"" + username + "\" already exist in Database";
		else
			((JdbcUserDetailsManager) jdbcUserDetailsManager).createUser(
					User.withUsername(username).password(passwordEncoder.encode(password)).roles("USER").build());
			return "\"" + username + "\" created in Database";
	}
		
		
	

	@PostMapping("/createUser/{username}/{password}/{role}")
	@ResponseBody
	public String createUser(@PathVariable("username") String username, @PathVariable("password") String password,
			@PathVariable("role") String role) {
		((JdbcUserDetailsManager) jdbcUserDetailsManager).createUser(
				User.withUsername(username).password(passwordEncoder.encode(password)).roles("USER").build());
		return checkIfUserExists(username);
	}

	@GetMapping("/createUser/{username}")
	@ResponseBody
	public String checkIfUserExists(@PathVariable("username") String username) {
		boolean flag = ((JdbcUserDetailsManager) jdbcUserDetailsManager).userExists(username);
		if (flag)
			return "\"" + username + "\" exist in Database";
		else
			return "\"" + username + "\" does not exist in Database";
	}

}