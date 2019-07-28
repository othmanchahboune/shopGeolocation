package com.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.common.Status;
import com.entities.auth.Role;
import com.entities.auth.User;
import com.repositories.RoleRepository;
import com.repositories.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDetails loadUserByUsername(String email) {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			List<GrantedAuthority> authorities = getUserAuthority(user.getRoles());
			return buildUserForAuthentication(user, authorities);
		} else {
			throw new UsernameNotFoundException("username not found");
		}
	}

	private UserDetails buildUserForAuthentication(User user, List<GrantedAuthority> authorities) {
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
	}

	private List<GrantedAuthority> getUserAuthority(Set<Role> userRoles) {
		Set<GrantedAuthority> roles = new HashSet<>();
		userRoles.forEach((role) -> roles.add(new SimpleGrantedAuthority(role.getRoleName())));

		return new ArrayList<>(roles);
	}

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public Role findRoleByRoleName(String roleName) {
		return roleRepository.findByRoleName(roleName);
	}

	public void saveUser(User user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setStatus(Status.ACTIVE);
		Role userRole = roleRepository.findByRoleName("USER");
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}

}
