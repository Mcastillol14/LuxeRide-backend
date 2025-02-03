package com.luxeride.taxistfg.Controller;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.luxeride.taxistfg.Service.AuthService;
import com.luxeride.taxistfg.Service.UsuarioService;
import com.luxeride.taxistfg.Util.LoginRequest;
import com.luxeride.taxistfg.JWT.AuthResponse;
import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ControladorUsuario {
    private static final Logger logger = LoggerFactory.getLogger(ControladorUsuario.class);

    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsario(@RequestBody Usuario usuario){
        try{
            usuarioService.registrarUsario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/iniciar")
    public ResponseEntity<AuthResponse> iniciarSesion(@RequestBody LoginRequest loginRequest) {
        try{
            AuthResponse response = this.authService.login(loginRequest);
            return ResponseEntity.ok(response);
        }catch(Exception e){
            logger.error("Error al iniciar sesión", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new AuthResponse(null));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> obtenerInformacion() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            logger.debug("Authentication: " + auth);
            logger.debug("Principal: " + auth.getPrincipal());
            logger.debug("Authorities: " + auth.getAuthorities());

            String email = (String) auth.getPrincipal();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
            
            Map<String, String> informacion = new LinkedHashMap<>();
            informacion.put("nombre", usuario.getNombre());
            informacion.put("apellidos", usuario.getApellidos());
            informacion.put("email", usuario.getEmail());
            informacion.put("dni", usuario.getDni());
            return ResponseEntity.ok(informacion);
        } catch (UsernameNotFoundException e) {
            logger.error("Usuario no encontrado", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            logger.error("Error al obtener información del usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al obtener la información del usuario");
        }
    }

}