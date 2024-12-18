package com.luxeride.taxistfg.Controller;

import com.luxeride.taxistfg.JWT.AuthResponse;
import com.luxeride.taxistfg.Model.User;
import com.luxeride.taxistfg.Service.AuthService;
import com.luxeride.taxistfg.Service.JwtService;
import com.luxeride.taxistfg.Service.UserService;
import com.luxeride.taxistfg.Util.DriverRequest;
import com.luxeride.taxistfg.Util.LoginRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AuthService authService;
    private final JwtService jwtService;
    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResponse authResponse = authService.login(loginRequest);

            if (authResponse == null || authResponse.getToken() == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            String token = authResponse.getToken();
            List<String> roles = jwtService.getRolesFromToken(token);


            if (roles != null && roles.contains("ROLE_ADMIN")) {
                return ResponseEntity.ok(authResponse);
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/alluser")
    public ResponseEntity<Page<User>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<User> users = userService.findPaginated(pageRequest);
        return ResponseEntity.ok(users);
    }
    @PutMapping("/edituser")


    @PostMapping("/adddriver")
    public ResponseEntity<String> addDriver(@RequestBody DriverRequest driverRequest) {
        try {
            String dni = driverRequest.getDni();
            userService.addDriver(dni);  // Llamamos al servicio para asignar el rol de driver
            return ResponseEntity.ok("Usuario asignado como taxista exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/removedriver")
    public ResponseEntity<String> removeDriver(@RequestBody DriverRequest driverRequest) {
        try {
            String dni = driverRequest.getDni();
            userService.removeDriver(dni);
            return ResponseEntity.ok("Rol de taxista eliminado exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/alldriver")
    public ResponseEntity<Page<User>> getAllDrivers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            PageRequest pageRequest = PageRequest.of(page, size);
            Page<User> drivers = userService.getAllDrivers("DRIVER", pageRequest);
            return ResponseEntity.ok(drivers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

