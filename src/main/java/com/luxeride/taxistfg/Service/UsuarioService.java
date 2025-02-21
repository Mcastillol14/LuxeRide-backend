package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Rol;
import com.luxeride.taxistfg.Model.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import com.luxeride.taxistfg.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.transaction.Transactional;
import java.util.Optional;

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

        if (existingUsuarioByDni.isPresent() && existingUsuarioByEmail.isPresent()) {
            throw new IllegalArgumentException("El DNI y el correo ya existen");
        } else if (existingUsuarioByEmail.isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya existe");
        } else if (existingUsuarioByDni.isPresent()) {
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

        String passwordEncriptada = encriptadoPassword.encode(usuario.getPassword());
        usuario.setPassword(passwordEncriptada);
        usuario.setRol(Rol.ROL_CLIENTE);
        usuario.setAccountNonLocked(true);

        usuarioRepository.save(usuario);
    }

    @Transactional
    public void editarUsuario(Integer id, String nombre, String apellidos, String dni, String email) {
        Optional<Usuario> existingUsuario = usuarioRepository.findById(id);

        if (!existingUsuario.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "El usuario que quieres editar no existe");
        }

        Usuario usuarioActualizado = existingUsuario.get();

        if (nombre != null && !nombre.trim().isEmpty()) {
            usuarioActualizado.setNombre(nombre);
        }
        if (apellidos != null && !apellidos.trim().isEmpty()) {
            usuarioActualizado.setApellidos(apellidos);
        }
        if (dni != null && !dni.trim().isEmpty()) {
            usuarioActualizado.setDni(dni);
        }
        if (email != null && !email.trim().isEmpty()) {
            Optional<Usuario> existingUsuarioByEmail = usuarioRepository.findByEmail(email);
            if (existingUsuarioByEmail.isPresent() && !existingUsuarioByEmail.get().getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está en uso");
            }
            usuarioActualizado.setEmail(email);
        }

        usuarioRepository.save(usuarioActualizado);
    }

    public Page<Usuario> obtenerUsuariosPorFiltro(Pageable pageable, String dni) {
        if (dni != null && !dni.isEmpty()) {
            return usuarioRepository.findByDni(pageable, dni);
        } else {
            return usuarioRepository.findAll(pageable);
        }
    }

    @Transactional
    public void bloquearCuenta(Integer id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setAccountNonLocked(false);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void desbloquearCuenta(Integer id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setAccountNonLocked(true);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void addTaxista(Integer id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        usuario.setRol(Rol.ROL_TAXISTA);
        usuarioRepository.save(usuario);
    }

    @Transactional
    public void deleteTaxista(Integer id) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findById(id);
        if (!usuarioOpt.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        Usuario usuario = usuarioOpt.get();
        if (usuario.getRol() == Rol.ROL_TAXISTA) {
            usuario.setRol(Rol.ROL_CLIENTE);
            usuarioRepository.save(usuario);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no tiene el rol de taxista");
        }
    }
}
