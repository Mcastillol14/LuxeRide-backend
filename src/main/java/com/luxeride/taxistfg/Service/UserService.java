package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Dto.UserDTO;

import com.luxeride.taxistfg.Model.User;
import com.luxeride.taxistfg.Repository.UserRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
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
        User user = new User();
        user.setName(userDTO.getName());
        user.setLastName(userDTO.getLastName());
        user.setDNI(userDTO.getDNI());
        user.setEmail(userDTO.getEmail());
        user.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
        user.getRoles().add("CLIENT");
        userRepository.save(user);

    }
    public User loginUser(UserDTO userDTO) {
        Optional<User> existingUserByEmail = userRepository.findByEmail(userDTO.getEmail());
        if (!existingUserByEmail.isPresent()) {
            throw new IllegalArgumentException("El correo proporcianado no esta registrado");
        }
        User user= existingUserByEmail.get();
        if (!bCryptPasswordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("La contraseña es incorrecta");
        }
        return user;
    }

    public UserDetails loadUserByDni(String dni) throws UsernameNotFoundException {
        return userRepository.findByDNI(dni)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario buscado por : "+dni+" no encontrado"));

    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario buscado por: "+email+" no encontrado"));
    }
}
