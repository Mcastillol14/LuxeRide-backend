package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Servicio;
import com.luxeride.taxistfg.Repository.ServicioRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import jakarta.transaction.Transactional;

@Service
public class ServicioService {

    @Autowired
    private ServicioRepository servicioRepository;

    @Transactional
    public void crearServicio(Servicio servicio) {
        if (servicioRepository.findByTipo(servicio.getTipo()).isPresent()) {
            throw new IllegalArgumentException("Este tipo de servicio ya existe");
        }

        if (servicio.getTipo() == null || servicio.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        if (servicio.getDescripcion() == null || servicio.getDescripcion().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción es obligatoria");
        }
        if (servicio.getPrecioPorKm() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }

        servicio.setEstado(true);
        servicioRepository.save(servicio);
    }

    @Transactional
    public void borrarServicio(Integer id) {
        if (!servicioRepository.existsById(id)) {
            throw new IllegalArgumentException("El servicio no existe");
        }
        servicioRepository.deleteById(id);
    }

    @Transactional
    public void activarServicio(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));

        if (servicio.isEstado()) {
            throw new IllegalArgumentException("El servicio ya está activo");
        }

        servicio.setEstado(true);
        servicioRepository.save(servicio);
    }

    @Transactional
    public void desactivarServicio(Integer id) {
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));

        if (!servicio.isEstado()) {
            throw new IllegalArgumentException("El servicio ya está desactivado");
        }

        servicio.setEstado(false);
        servicioRepository.save(servicio);
    }

    public Page<Servicio>  obtenerServicios(String tipo, Pageable pageable) {
        if (tipo == null || tipo.trim().isEmpty()) {
            return servicioRepository.findAll(pageable);
        }
        return servicioRepository.buscarServiciosPorTipo(tipo, pageable);
    }
}
