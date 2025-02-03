package com.luxeride.taxistfg.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Service.UsuarioService;
import com.luxeride.taxistfg.Util.DniRequest;
import com.luxeride.taxistfg.Model.UsuarioDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ControladorAdmin {
    private final UsuarioService usuarioService;

    @GetMapping("/allUsuarios")
public ResponseEntity<Page<UsuarioDTO>> allUsuarios(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(required = false) String rol,   
        @RequestParam(required = false) String dni) { 

    Pageable pageable = PageRequest.of(page, size);

    Page<Usuario> usuarioPage = usuarioService.obtenerUsuariosPorFiltro(pageable, rol, dni);

    Page<UsuarioDTO> usuarioDTOPage = usuarioPage.map(usuario -> new UsuarioDTO(
            usuario.getId(),
            usuario.getNombre(),
            usuario.getApellidos(),
            usuario.getDni(),
            usuario.getEmail(),
            usuario.getRol(),
            usuario.isAccountNonLocked()));

    return ResponseEntity.ok(usuarioDTOPage);
}


    @GetMapping("/allTaxistas")
    public ResponseEntity<Page<UsuarioDTO>> allTaxistas(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Usuario> usuarioPage = usuarioService.obtenerTodosLosTaxistas(pageable);
        Page<UsuarioDTO> usuarioDTOPage = usuarioPage.map(usuario -> new UsuarioDTO(
                usuario.getId(),
                usuario.getNombre(),
                usuario.getApellidos(),
                usuario.getDni(),
                usuario.getEmail(),
                usuario.getRol(),
                usuario.isAccountNonLocked()));

        return ResponseEntity.ok(usuarioDTOPage);
    }

    @PutMapping("/addTaxista")
    public ResponseEntity<String> addTaxista(@RequestBody Usuario usuario) {
        try {
            usuarioService.addTaxista(usuario);
            return ResponseEntity.ok("El usuario ha sido actualizado a taxista correctamente.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al añadir taxista.");
        }
    }

    @PutMapping("/eliminarTaxista")
    public ResponseEntity<String> eliminarTaxista(@RequestBody Usuario usuario) {
        try {
            usuarioService.eliminarTaxista(usuario);
            return ResponseEntity.ok("El usuario ha sido eliminado de taxista correctamente.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al eliminar taxista.");
        }
    }

    @PutMapping("/activarCuenta")
    public ResponseEntity<String> activarCuenta(@RequestBody DniRequest request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setDni(request.getDni());
            usuarioService.activarCuenta(usuario);
            return ResponseEntity.ok("Cuenta activada correctamente.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al activar la cuenta.");
        }
    }

    @PutMapping("/desactivarCuenta")
    public ResponseEntity<String> desactivarCuenta(@RequestBody DniRequest request) {
        try {
            Usuario usuario = new Usuario();
            usuario.setDni(request.getDni());
            usuarioService.desactivarCuenta(usuario);
            return ResponseEntity.ok("Cuenta desactivada correctamente.");
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al desactivar la cuenta.");
        }
    }
}
