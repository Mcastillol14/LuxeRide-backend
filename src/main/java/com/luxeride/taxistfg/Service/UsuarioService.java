package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Rol;
import com.luxeride.taxistfg.Model.Usuario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import jakarta.transaction.Transactional;

import com.luxeride.taxistfg.Repository.UsuarioRepository;

@Service
public class UsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private PasswordEncoder encriptadoPassword;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder encriptadoPassword) {
        this.usuarioRepository = usuarioRepository;
        this.encriptadoPassword = encriptadoPassword;
    }

    @Transactional
    public void registrarUsario(Usuario usuario) {
        Optional<Usuario> existingUsuarioByEmail = usuarioRepository.findByEmail(usuario.getEmail());
        Optional<Usuario> existingUsuarioByDni = usuarioRepository.findByDni(usuario.getDni());

        if (existingUsuarioByEmail.isPresent()) {
            throw new IllegalArgumentException("El correo electronico ya existe");
        }
        if (existingUsuarioByDni.isPresent()) {
            throw new IllegalArgumentException("El DNI ya existe");
        }
        if (usuario.getNombre() == null || usuario.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre es obligatorio");
        }
        if (usuario.getApellidos() == null || usuario.getApellidos().isEmpty()) {
            throw new IllegalArgumentException("Los apellidos son obligatorios");
        }
        if (usuario.getDni() == null || usuario.getDni().isEmpty()) {
            throw new IllegalArgumentException("El DNI es obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El correo electronico es obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        PasswordEncoder encriptarPassword = new BCryptPasswordEncoder();
        String passwordEncriptada = encriptarPassword.encode(usuario.getPassword());
        Usuario usuarioCreado = new Usuario();
        usuarioCreado.setNombre(usuario.getNombre());
        usuarioCreado.setApellidos(usuario.getApellidos());
        usuarioCreado.setDni(usuario.getDni());
        usuarioCreado.setEmail(usuario.getEmail());
        usuarioCreado.setPassword(passwordEncriptada);
        usuarioCreado.setRol(Rol.ROL_CLIENTE);
        usuarioCreado.setAccountNonLocked(true);
        usuarioRepository.save(usuarioCreado);
    }

    public Optional<Usuario> obtenerUsuarioPorEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public Page<Usuario> obtenerTodosLosUsuarios(Pageable pageable) {
        return usuarioRepository.findAll(pageable);

    }
    public Page<Usuario> obtenerUsuariosPorFiltro(Pageable pageable, String rol, String dni) {
        Rol rolEnum = (rol != null && !rol.isEmpty()) ? Rol.valueOf(rol) : null;
    
        return usuarioRepository.findByRolAndDni(pageable, rolEnum, dni);
    }
    

    @Transactional
    public void addTaxista(Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByDni(usuario.getDni());
        if (!existingUsuario.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario no existe");
        }
        Usuario usuarioActualizado = existingUsuario.get();
        if (usuarioActualizado.getRol() == Rol.ROL_TAXISTA) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya es taxista");
        }
        usuarioActualizado.setRol(Rol.ROL_TAXISTA);

        usuarioRepository.save(usuarioActualizado);
    }

    @Transactional
    public void eliminarTaxista(Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByDni(usuario.getDni());
        if (!existingUsuario.isPresent()) {
            throw new IllegalArgumentException("El usuario que quieres eliminar de taxista no existe");
        }
        Usuario usuarioActualizado = existingUsuario.get();
        if (usuarioActualizado.getRol() == Rol.ROL_CLIENTE) {
            throw new IllegalArgumentException("El usuario que quieres eliminar de taxita no es taxista");
        }
        usuarioActualizado.setRol(Rol.ROL_CLIENTE);
        usuarioRepository.save(usuarioActualizado);
    }

    public Page<Usuario> obtenerTodosLosTaxistas(Pageable pageable) {
        return usuarioRepository.findByRol(Rol.ROL_TAXISTA, pageable);
    }

    @Transactional
    public void desactivarCuenta(Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByDni(usuario.getDni());

        if (!existingUsuario.isPresent()) {
            throw new IllegalArgumentException("El usuario que quieres desactivar no existe");
        }

        Usuario usuarioActualizado = existingUsuario.get();

        if (!usuarioActualizado.isAccountNonLocked()) {
            return;
        }

        usuarioActualizado.setAccountNonLocked(false);
        usuarioRepository.save(usuarioActualizado);
    }

    @Transactional
    public void activarCuenta(Usuario usuario) {
        Optional<Usuario> existingUsuario = usuarioRepository.findByDni(usuario.getDni());

        if (!existingUsuario.isPresent()) {
            throw new IllegalArgumentException("El usuario que quieres activar no existe");
        }

        Usuario usuarioActualizado = existingUsuario.get();

        if (usuarioActualizado.isAccountNonLocked()) {
            return; 
        }

        usuarioActualizado.setAccountNonLocked(true);
        usuarioRepository.save(usuarioActualizado);
    }

}
