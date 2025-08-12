package com.project.bookbackend.security;

import com.project.bookbackend.user.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtService jwtService;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtFilter(JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
		this.jwtService = jwtService;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
	                                @NonNull FilterChain filterChain
	) throws ServletException, IOException {

		if (request.getServletPath().equals("/api/v1/auth")) {
			filterChain.doFilter(request, response);
			return;
		}
		String authHeader = request.getHeader("Authorization");
		String jwtToken;
		String userEmail;

		if (authHeader != null && authHeader.startsWith("Bearer ")) {
			jwtToken = authHeader.substring(7);
			userEmail = jwtService.extractUsername(jwtToken);
		} else {
			filterChain.doFilter(request, response);
			return;
		}
		if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
			UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

			if (jwtService.isTokenValid(jwtToken, userDetails)) {
				UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities()
				);
				authToken.setDetails(new WebAuthenticationDetailsSource()
					.buildDetails(request)
				);
				SecurityContextHolder.getContext().setAuthentication(authToken);
			}
		}
		filterChain.doFilter(request, response);
	}
}







