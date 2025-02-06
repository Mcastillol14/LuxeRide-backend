package com.luxeride.taxistfg.Service;

import java.math.BigDecimal;
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

    @Transactional
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
    public void desactivarServicio(Integer id) {
        Optional<Servicio> servicioOpt = servicioRepository.findById(id);
        if (!servicioOpt.isPresent()) {
            throw new IllegalArgumentException("El servicio no existe");
        }
        Servicio servicio = servicioOpt.get();
        if (!servicio.isEstado()) {
            throw new IllegalArgumentException("El servicio ya está desactivado");
        }
        servicio.setEstado(false);
        servicioRepository.save(servicio);
    }

    @Transactional
    public void editarServicio(Integer Id, String tipo, String descripcion, BigDecimal precioPorKm, Boolean estado) {
        Optional<Servicio> servicioOpt= servicioRepository.findById(Id);
        if (!servicioOpt.isPresent()) {
            throw new IllegalArgumentException("El servicio no existe");
        }
        Servicio servicioActualizado= servicioOpt.get();
        Optional<Servicio> servicioOptTipo = servicioRepository.findByTipo(tipo);
        if (servicioOptTipo.isPresent() && !servicioOpt.get().getId().equals(Id)) {
            throw new IllegalArgumentException("Este tipo de servicio ya existe");
        }
        if(tipo==null || tipo.trim().isEmpty()){
            throw new IllegalArgumentException("El tipo es obligatorio");
        }
        if(descripcion==null || descripcion.trim().isEmpty()){
            throw new IllegalArgumentException("La descripcion es obligatoria");
        }
        if (precioPorKm == null) {
            throw new IllegalArgumentException("El precio es obligatorio");
        }
        servicioActualizado.setTipo(tipo);
        servicioActualizado.setDescripcion(descripcion);
        servicioActualizado.setPrecioPorKm(precioPorKm);
        servicioRepository.save(servicioActualizado);
    }

    @Transactional
    public void activarServicio(Integer id) {
        Optional <Servicio> servicioOpt = servicioRepository.findById(id);
        if (!servicioOpt.isPresent()) {
            throw new IllegalArgumentException("El servicio no existe");
        }
        Servicio servicio = servicioRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("El servicio no existe"));
        if (servicio.isEstado()) {
            throw new IllegalArgumentException("El servicio ya está activo");
        }
        servicio.setEstado(true);
        servicioRepository.save(servicio);
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

    public Page<Servicio> obtenerServiciosPorFiltro(Pageable pageable, Boolean estado) {
        return servicioRepository.findByEstadoOrEstadoIsNull(estado, estado == null ? true : null, pageable);
    }
}