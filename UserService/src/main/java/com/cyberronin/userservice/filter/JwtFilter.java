package com.cyberronin.userservice.filter;

import com.cyberronin.userservice.model.CustomUserDetails;
import com.cyberronin.userservice.util.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;

// NOT USED IN THIS SERVICE, IMPLEMENTED IN API GATEWAY

/*
Intercepts every request and authenticates it if valid
if no jwt (bearer token) found then ignores it and Security config will block the request

Filter flow:
Register: User -> Filter (No JWT found, so it does nothing) -> Security Config
Login: User -> Filter (No JWT found, so it does nothing) -> Security Config -> Login Controller (gets username and password and gives JWT)
Any other Route: User -> Filter (validate JWT) -> Security Config (if not valid then block else allow) -> Controller
*/

@Component
public class JwtFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String authHeader = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                UUID userId = jwtUtil.extractUserId(token);
                String username = jwtUtil.extractUsername(token);
                String role = jwtUtil.extractRole(token);

                List<SimpleGrantedAuthority> authorities = List.of(new SimpleGrantedAuthority(role));

                CustomUserDetails principal = new CustomUserDetails(
                        userId,
                        username,
                        authorities
                );

                // Create the Authentication object
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(principal, null, authorities);

                // wrap the chain in the Security Context as WebFlux is used
                return chain.filter(exchange)
                        .contextWrite(ReactiveSecurityContextHolder.withAuthentication(authToken));
            }
        }
        return chain.filter(exchange);
    }
}