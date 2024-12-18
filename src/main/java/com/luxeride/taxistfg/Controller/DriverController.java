package com.luxeride.taxistfg.Controller;

import com.luxeride.taxistfg.JWT.AuthResponse;
import com.luxeride.taxistfg.Service.AuthService;
import com.luxeride.taxistfg.Service.JwtService;
import com.luxeride.taxistfg.Util.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/driver")
@RequiredArgsConstructor
public class DriverController {
    private final AuthService authService;
    private final JwtService jwtService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {

            AuthResponse authResponse = authService.login(loginRequest);

            if (authResponse == null || authResponse.getToken() == null) {
                logger.warn("Error al inicar sesion: Token nulo");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authResponse.getToken();
            List<String> roles = jwtService.getRolesFromToken(token);


            if (roles != null && roles.contains("ROLE_DRIVER")) {
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
