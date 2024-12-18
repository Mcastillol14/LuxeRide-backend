package com.luxeride.taxistfg.Util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class UpdateRequest {
    private String name;
    private String lastName;
    private String email;
    private String dni;
}
