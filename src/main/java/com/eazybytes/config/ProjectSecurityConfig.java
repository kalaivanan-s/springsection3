package com.eazybytes.config;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ProjectSecurityConfig {

	@Bean
	SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {

		/**
		 * Below is the custom security configurations
		 * 
		 */
		http
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
        );
		http
	        .sessionManagement(session -> session
	            .invalidSessionUrl("/invalidSession")
	    );
		 http
	        .logout(logout -> logout
	        		.logoutSuccessUrl("/logout")
	        .invalidateHttpSession(true)                                        
	                                 
	        );
		 
		 
		
		http.authorizeHttpRequests()
		.antMatchers("/notices", "/contact").permitAll()
		.antMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards","/createUser/**").hasRole("MANAGER").anyRequest().authenticated()
		.and().formLogin()
		.and().httpBasic()
		.and().csrf().disable();

		return http.build();
		

		// .requestMatchers("/myAccount","/myBalance","/myLoans","/myCards").authenticated()
		// .requestMatchers("/notices","/contact").permitAll()

		/**
		 * Configuration to deny all the requests
		 */
		/*
		 * http.authorizeHttpRequests().anyRequest().denyAll() .and().formLogin()
		 * .and().httpBasic();
		 * 
		 * return http.build();
		 */

		/**
		 * Configuration to permit all the requests
		 */
		/*
		 * http.authorizeHttpRequests().anyRequest().permitAll() .and().formLogin()
		 * .and().httpBasic(); return http.build();
		 */
	}
	/*
	 * @Bean public InMemoryUserDetailsManager userDetailsService() { /* Approach 1
	 * where we use withDefaultPasswordEncoder() method while creating the user
	 * details
	 */
	/*
	 * UserDetails admin =
	 * User.withDefaultPasswordEncoder().username("admin").password("12345").
	 * authorities("admin") .build(); UserDetails user =
	 * User.withDefaultPasswordEncoder().username("user").password("12345").
	 * authorities("read") .build(); return new InMemoryUserDetailsManager(admin,
	 * user);
	 * 
	 * }
	 */

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService(DataSource dataSource) {
		return new JdbcUserDetailsManager(dataSource);
	}

}