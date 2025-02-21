package com.luxeride.taxistfg.Util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
public class AuthResponse {
    private String token;  // El token JWT si la autenticación fue exitosa
    private String message;  // El mensaje de error en caso de fallo

    // Constructor para cuando hay un mensaje de error
    public AuthResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }
}

