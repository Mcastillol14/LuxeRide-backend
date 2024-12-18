package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Dto.UserDTO;

import com.luxeride.taxistfg.Model.User;
import com.luxeride.taxistfg.Repository.UserRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private UserRepository userRepository;
    private PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public void registerUser(UserDTO userDTO) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(userDTO.getEmail());
        Optional<User> existingUserByDNI = userRepository.findByDNI(userDTO.getDNI());

        if (existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("El correo ya existe");
        }
        if (existingUserByDNI.isPresent()) {
            throw new IllegalArgumentException("El DNI ya existe");
        }
        if (userDTO.getPassword() == null || userDTO.getPassword().isEmpty()) {
            throw new IllegalArgumentException("La contraseña es obligatoria");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encryptedPassword = passwordEncoder.encode(userDTO.getPassword());

        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setDNI(userDTO.getDNI());
        user.setEmail(userDTO.getEmail());
        user.setPassword(encryptedPassword);

        user.getRoles().add("CLIENT");

        userRepository.save(user);
    }



    public void deleteUser(Integer id){
        userRepository.deleteById(id);
    }

    public UserDetails loadUserByDni(String dni) throws UsernameNotFoundException {
        return userRepository.findByDNI(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario buscado por : "+dni+" no encontrado"));

    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario buscado por: "+email+" no encontrado"));
    }

    public UserDTO convertUserToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(user.getName());
        userDTO.setLastName(user.getLastName());
        userDTO.setEmail(user.getEmail());
        userDTO.setDNI(user.getDNI());
        return userDTO;
    }
    public UserDTO getCurrentUserFullName() {
        User user = getCurrentUser();
        return convertUserToUserDTO(user);
    }

    public User getCurrentUser() {
        String username = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
    public Page<User> findPaginated(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
    public void addDriver(String dni) {
        User user = userRepository.findByDNI(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario con DNI " + dni + " no encontrado"));

        if (user.getRoles().contains("DRIVER")) {
            throw new IllegalArgumentException("El usuario ya tiene el rol de driver.");
        }

        user.getRoles().add("DRIVER");
        userRepository.save(user);
    }
    public void removeDriver(String dni){
        User user=userRepository.findByDNI(dni).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + dni));
        if (user.getRoles().contains("DRIVER")) {
            user.getRoles().remove("DRIVER");
            userRepository.save(user);
        } else {
            throw new IllegalArgumentException("El usuario no tiene el rol de driver.");
        }
    }
    public Page<User> getAllDrivers(String role, Pageable pageable) {
        return userRepository.findByRolesContaining(role, pageable);
    }
}
