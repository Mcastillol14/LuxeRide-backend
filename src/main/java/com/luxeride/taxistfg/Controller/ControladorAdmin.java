package com.luxeride.taxistfg.Controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import com.luxeride.taxistfg.Model.Licencia;
import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Service.UsuarioService;
import com.luxeride.taxistfg.Util.DniRequest;
import com.luxeride.taxistfg.Model.UsuarioDTO;
import com.luxeride.taxistfg.Service.LicenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ControladorAdmin {
    private final UsuarioService usuarioService;
    private final LicenciaService licenciaService;

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
    @PutMapping("/editarUsuario/{id}")
public ResponseEntity<String> editarUsuario(
        @PathVariable Integer id,
        @RequestBody UsuarioDTO usuarioDTO) {
    try {
        usuarioService.editarUsuario(id, usuarioDTO.getNombre(), usuarioDTO.getApellidos(), usuarioDTO.getDni(), usuarioDTO.getEmail());
        return ResponseEntity.ok("Usuario editado correctamente.");
    } catch (ResponseStatusException e) {
        return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al editar usuario.");
    }
}



    @GetMapping("/allLicencias")
    public ResponseEntity<?> getAllLicencias(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String numero) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Licencia> licencias = licenciaService.obtenerLicenciasPorFiltro(pageable, numero);
            
            licencias.getContent().forEach(licencia -> {
                if (licencia.getCoche() != null) {
                    licencia.getCoche().setMatricula(null);  
                }
            });

            return ResponseEntity.ok(licencias);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Error al obtener las licencias: " + e.getMessage());
        }
    }
    @PutMapping("/activarLicencia/{id}")
public ResponseEntity<String> activarLicencia(@PathVariable Integer id) {
    try {
        licenciaService.activarLicencia(id);
        return ResponseEntity.ok("Licencia activada correctamente");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error al activar la licencia");
    }
}

@PutMapping("/desactivarLicencia/{id}")
public ResponseEntity<String> desactivarLicencia(@PathVariable Integer id) {
    try {
        licenciaService.desactivarLicencia(id);
        return ResponseEntity.ok("Licencia desactivada correctamente");
    } catch (IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body("Error al desactivar la licencia");
    }
}

    @PostMapping("/registrarLicencia")
    public ResponseEntity<String> registrarLicencia(@RequestBody Licencia licencia) {
        try {
            licenciaService.registrarLicencia(licencia);
            return ResponseEntity.ok("Licencia registrada correctamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al eliminar taxista.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al activar la cuenta.");
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error inesperado al desactivar la cuenta.");
        }
    }
}
