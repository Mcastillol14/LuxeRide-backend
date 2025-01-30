package com.luxeride.taxistfg.Service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.luxeride.taxistfg.Model.Servicio;
import com.luxeride.taxistfg.Repository.ServicioRepository;

@Service
public class ServicioService {
    
    @Autowired
    private ServicioRepository servicioRepository;

    private Servicio obtenerServicioPorTipo(String tipo) {
        return servicioRepository.findByTipo(tipo)
            .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));
    }

    public void registrarServicio(Servicio servicio) {
        Optional<Servicio> existeServicio = servicioRepository.findByTipo(servicio.getTipo());
        if (existeServicio.isPresent()) {
            throw new IllegalArgumentException("Este tipo de servicio ya existe");
        }
        if (servicio.getTipo() == null || servicio.getTipo().isEmpty()) {
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        if (servicio.getDescripcion() == null || servicio.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }
        if (servicio.getPrecioPorKm() == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }
        
        Servicio servicioCreado = new Servicio();
        servicioCreado.setTipo(servicio.getTipo());
        servicioCreado.setDescripcion(servicio.getDescripcion());
        servicioCreado.setPrecioPorKm(servicio.getPrecioPorKm());
        servicioCreado.setEstado(true);
        servicioRepository.save(servicioCreado);
    }

    @Transactional
    public void desactivarServicio(Servicio servicio) {
        Servicio servicioActualizado = obtenerServicioPorTipo(servicio.getTipo());
        if (!servicioActualizado.isEstado()) {
            throw new IllegalArgumentException("El servicio ya está desactivado");
        }
        servicioActualizado.setEstado(false);
        servicioRepository.save(servicioActualizado);
    }

    @Transactional
    public void cambiarDescripcion(Servicio servicio) {
        Servicio servicioActualizado = obtenerServicioPorTipo(servicio.getTipo());
        if (servicio.getDescripcion() == null || servicio.getDescripcion().isEmpty()) {
            throw new IllegalArgumentException("La nueva descripción no puede estar vacía");
        }
        servicioActualizado.setDescripcion(servicio.getDescripcion());
        servicioRepository.save(servicioActualizado);
    }

    @Transactional
    public void cambiarPrecio(Servicio servicio) {
        Servicio servicioActualizado = obtenerServicioPorTipo(servicio.getTipo());
        if (servicio.getPrecioPorKm() == null) {
            throw new IllegalArgumentException("El nuevo precio no puede estar vacío");
        }
        servicioActualizado.setPrecioPorKm(servicio.getPrecioPorKm());
        servicioRepository.save(servicioActualizado);
    }
    
    public Page<Servicio> listarServiciosActivos(Pageable pageable) {
        return servicioRepository.findByEstadoTrue(pageable);
    }
    
    public Page<Servicio> listarTodosLosServicios(Pageable pageable) {
        return servicioRepository.findAll(pageable);
    }
}
