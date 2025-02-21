package com.luxeride.taxistfg.Model;

// Definición de los roles disponibles para los usuarios en el sistema
public enum Rol {
    // Rol de cliente, los usuarios que solicitan viajes
    ROL_CLIENTE,

    // Rol de taxista, los conductores de los coches
    ROL_TAXISTA,

    // Rol de administrador, tiene privilegios para gestionar todo el sistema
    ROL_ADMIN
}
