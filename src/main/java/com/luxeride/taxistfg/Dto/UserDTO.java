package com.luxeride.taxistfg.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDTO {
    private String name;
    private String lastName;
    @JsonProperty("dni")
    private String DNI;
    private String email;
    private String password;
}

