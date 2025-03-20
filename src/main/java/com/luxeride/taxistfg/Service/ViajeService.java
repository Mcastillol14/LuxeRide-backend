package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Viaje;
import com.luxeride.taxistfg.Repository.ViajeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ViajeService {

    @Autowired
    private ViajeRepository viajeRepository;
    @Transactional
    public void registrarViaje(Viaje viaje) {
        if (viaje == null) {
            throw new IllegalArgumentException("El viaje no puede ser nulo");
        }

        if (viaje.getHoraInicio() == null || viaje.getHoraLlegada() == null) {
            throw new IllegalArgumentException("Las horas de inicio y llegada son obligatorias");
        }

        if (viaje.getOrigen() == null || viaje.getOrigen().isEmpty()) {
            throw new IllegalArgumentException("El origen no puede estar vacío");
        }

        if (viaje.getDestino() == null || viaje.getDestino().isEmpty()) {
            throw new IllegalArgumentException("El destino no puede estar vacío");
        }

        if (viaje.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es obligatorio");
        }

        if (viaje.getTaxista() == null) {
            throw new IllegalArgumentException("El taxista es obligatorio");
        }

        if (viaje.getServicio() == null) {
            throw new IllegalArgumentException("El servicio es obligatorio");
        }

        if (viaje.getCoche() == null) {
            throw new IllegalArgumentException("El coche es obligatorio");
        }

        if (Objects.isNull(viaje.getFoto()) || viaje.getFoto().length == 0) {
            throw new IllegalArgumentException("La foto del viaje es obligatoria");
        }

        viajeRepository.save(viaje);
    }

}
