package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.JWT.AuthResponse;
import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Repository.UsuarioRepository;
import com.luxeride.taxistfg.Util.LoginRequest;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
public AuthResponse login(LoginRequest request) {
    Usuario usuario = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

    if (!usuario.isAccountNonLocked()) {
        throw new RuntimeException("Tu cuenta está bloqueada. Contacta con el soporte.");
    }

    authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

    String token = jwtService.getToken(usuario);

    return AuthResponse.builder()
            .token(token)
            .build();
}
}

