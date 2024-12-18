    package com.luxeride.taxistfg.Controller;

    import com.luxeride.taxistfg.Dto.UserDTO;
    import com.luxeride.taxistfg.JWT.AuthResponse;
    import com.luxeride.taxistfg.Model.User;
    import com.luxeride.taxistfg.Service.AuthService;
    import com.luxeride.taxistfg.Service.UserService;
    import com.luxeride.taxistfg.Repository.UserRepository;
    import com.luxeride.taxistfg.Util.LoginRequest;
    import lombok.RequiredArgsConstructor;
    import org.hibernate.service.spi.ServiceException;
    import org.springframework.http.HttpStatus;
    import org.springframework.http.ResponseEntity;
    import org.springframework.security.core.context.SecurityContextHolder;
    import org.springframework.security.core.userdetails.UsernameNotFoundException;
    import org.springframework.web.bind.annotation.*;
    import java.util.LinkedHashMap;
    import java.util.Map;

    @RestController
    @RequestMapping("/api/users")
    @RequiredArgsConstructor
    public class UserController {

        private final UserService userService;
        private final AuthService authService;
        private final UserRepository userRepository;

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
        public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest loginRequest) {
            try {
                AuthResponse response = this.authService.login(loginRequest);
                return ResponseEntity.ok(response);
            } catch (ServiceException e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse("Error en el servicio"));
            }

        }

        @PutMapping("/editme")
        public ResponseEntity<UserDTO> editUser(@RequestBody UserDTO userDTO) {
            try {
                String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

                User existingUser = userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

                if (!existingUser.getEmail().equals(userDTO.getEmail()) &&
                        userRepository.existsByEmail(userDTO.getEmail())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(null);
                }


                if (!existingUser.getDNI().equals(userDTO.getDNI()) &&
                        userRepository.existsByDNI(userDTO.getDNI())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(null);
                }

                existingUser.setName(userDTO.getName());
                existingUser.setLastName(userDTO.getLastName());
                existingUser.setEmail(userDTO.getEmail());
                existingUser.setDNI(userDTO.getDNI());

                userRepository.save(existingUser);


                UserDTO editedUserDTO = new UserDTO();
                editedUserDTO.setName(userDTO.getName());
                editedUserDTO.setLastName(userDTO.getLastName());
                editedUserDTO.setEmail(userDTO.getEmail());
                editedUserDTO.setDNI(userDTO.getDNI());

                return ResponseEntity.ok(editedUserDTO);
            } catch (UsernameNotFoundException e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
            } catch (Exception e) {
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @DeleteMapping("/delete/{id}")
        public ResponseEntity<String> deleteUser(@PathVariable Integer id) {
            try {
                userService.deleteUser(id);
                return ResponseEntity.ok("Usuario eliminado exitosamente");
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Error: " + e.getMessage());
            }
        }

        @GetMapping("/fullname")
        public ResponseEntity<?> getCurrentUserFullName() {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

            Map<String, String> fullName = new LinkedHashMap<>();
            fullName.put("name", user.getName());
            fullName.put("lastName", user.getLastName());

            return ResponseEntity.ok(fullName);
        }

        @GetMapping("/info")
        public ResponseEntity<?> getCurrentUserFullInfo() {
            String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            User user=userRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            Map<String, String> fullInfo = new LinkedHashMap<>();
            fullInfo.put("name", user.getName());
            fullInfo.put("lastName", user.getLastName());
            fullInfo.put("email", user.getEmail());
            fullInfo.put("dni", user.getDNI());
            return ResponseEntity.ok(fullInfo);
        }






    }
