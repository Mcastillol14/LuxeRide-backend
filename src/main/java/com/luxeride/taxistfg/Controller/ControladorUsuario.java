package com.luxeride.taxistfg.Controller;



import com.luxeride.taxistfg.Model.CocheDTO;
import com.luxeride.taxistfg.Model.Servicio;
import com.luxeride.taxistfg.Service.CocheService;
import com.luxeride.taxistfg.Service.ServicioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.luxeride.taxistfg.Model.UsuarioDTO;
import com.luxeride.taxistfg.Repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class ControladorUsuario {
    private static final Logger logger = LoggerFactory.getLogger(ControladorUsuario.class);

    @Autowired
    private final UsuarioService usuarioService;
    @Autowired
    private final AuthService authService;
    @Autowired
    private final UsuarioRepository usuarioRepository;
    @Autowired
    private final CocheService cocheService;
    @Autowired
    private final ServicioService servicioService;

    @PostMapping("/registrar")
    public ResponseEntity<String> registrarUsario(@RequestBody Usuario usuario) {
        try {
            usuarioService.registrarUsario(usuario);
            return ResponseEntity.ok("Usuario registrado exitosamente");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/iniciar")
    public ResponseEntity<AuthResponse> iniciar(@RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            return ResponseEntity.ok(authResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, "Usuario no encontrado"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(new AuthResponse(null, "Tu cuenta está bloqueada. Contacta con el soporte"));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new AuthResponse(null, "Error en el inicio de sesión"));
        }
    }
    

    @GetMapping("/info")
    public ResponseEntity<?> obtenerInformacion() {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String email = (String) auth.getPrincipal();
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    
            UsuarioDTO usuarioDTO = new UsuarioDTO(
                    usuario.getId(),
                    usuario.getNombre(),
                    usuario.getApellidos(),
                    usuario.getDni(),
                    usuario.getEmail(),
                    usuario.getRol(),
                    usuario.isAccountNonLocked()
            );
    
            return ResponseEntity.ok(usuarioDTO);
        } catch (UsernameNotFoundException e) {
            logger.error("Usuario no encontrado", e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        } catch (Exception e) {
            logger.error("Error al obtener información del usuario", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al obtener la información del usuario");
        }
    }
    @GetMapping("/enServicio")
    public List<CocheDTO> obtenerCochesEnServicio() {
        return cocheService.listarCochesEnServicio();
    }

    @GetMapping("/allServicios")
    public List<Servicio> obtenerServicios(){
        return servicioService.obtenerServicios();
    }

}