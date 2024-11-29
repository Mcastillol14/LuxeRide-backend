package com.luxeride.taxistfg.Util;

import lombok.Data;

@Data
public class UserRegistrationRequest {
    private String name;
    private String lastName;
    private String dni;
    private String email;
    private String password;
}
