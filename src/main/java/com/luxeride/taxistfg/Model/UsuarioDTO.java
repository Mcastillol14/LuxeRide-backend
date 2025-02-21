package com.luxeride.taxistfg.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String dni;
    private String email;
    private Rol rol;
    private boolean accountNonLocked;
}

