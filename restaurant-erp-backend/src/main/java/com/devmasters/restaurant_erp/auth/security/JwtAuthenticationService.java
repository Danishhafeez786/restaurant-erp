package com.devmasters.restaurant_erp.auth.security;

import com.devmasters.restaurant_erp.auth.domain.User;
import com.devmasters.restaurant_erp.auth.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;

    public Optional<UsernamePasswordAuthenticationToken> authenticate(String token) {

        if (!jwtTokenProvider.validateToken(token)) {
            return Optional.empty();
        }

        String email = jwtTokenProvider.getEmail(token);
        String role = jwtTokenProvider.getRole(token);
        int tokenVersion = jwtTokenProvider.getTokenVersion(token);

        User user = userRepository.findByEmail(email)
                .orElse(null);

        if (user == null) {
            return Optional.empty();
        }

        if (user.getTokenVersion() != tokenVersion) {
            return Optional.empty();
        }

        return Optional.of(
                new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        List.of(new SimpleGrantedAuthority("ROLE_" + role))
                )
        );
    }
}