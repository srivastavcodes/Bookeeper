package com.project.bookbackend.user;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository repository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userEmail) throws UsernameNotFoundException {
		return repository.findByUserEmail(userEmail)
			.orElseThrow(() -> new UsernameNotFoundException("Error: Couldn't find User: " + userEmail));
	}
}
