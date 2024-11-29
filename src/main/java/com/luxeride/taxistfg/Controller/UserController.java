package com.luxeride.taxistfg.Controller;

import com.luxeride.taxistfg.Dto.UserDTO;
import com.luxeride.taxistfg.Service.UserService;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final ConcurrentMapCacheManager cacheManager;
    private UserService userService;

    public UserController(UserService userService, ConcurrentMapCacheManager cacheManager) {
        this.userService = userService;
        this.cacheManager = cacheManager;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        try {
            userService.registerUser(userDTO);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody UserDTO userDTO) {
        try {
            userService.loginUser(userDTO);
            return ResponseEntity.ok("Inicio de sesion exitoso");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
