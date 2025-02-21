package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Licencia;
import com.luxeride.taxistfg.Repository.LicenciaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class LicenciaService {
    @Autowired
    private LicenciaRepository licenciaRepository;

    @Transactional
    public void registrarLicencia(Licencia licencia) {
        Optional<Licencia> existeLicencia = licenciaRepository.findByNumero(licencia.getNumero());
        if (existeLicencia.isPresent()) {
            throw new IllegalArgumentException("El número de licencia ya existe");
        }
        if (licencia.getNumero() == null || licencia.getNumero().isEmpty()) {
            throw new IllegalArgumentException("El número de licencia es obligatorio");
        }

        Licencia licenciaCreada = new Licencia();
        licenciaCreada.setNumero(licencia.getNumero());
        licenciaCreada.setEstado(true);
        licenciaRepository.save(licenciaCreada);
    }

    public Page<Licencia> obtenerLicenciasPorFiltro(Pageable pageable, String numero) {
        if (numero != null && !numero.isEmpty()) {
            return licenciaRepository.buscarLicenciasPorNumero(numero, pageable);
        }
        return licenciaRepository.findAll(pageable);
    }

    @Transactional
    public void activarLicencia(Integer id) {
        Optional<Licencia> existeLicencia = licenciaRepository.findById(id);
        if (!existeLicencia.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }
        Licencia licencia = existeLicencia.get();
        if (licencia.isEstado()) {
            throw new IllegalArgumentException("La licencia ya está activa");
        }
        licencia.setEstado(true);
        licenciaRepository.save(licencia);
    }

    @Transactional
    public void desactivarLicencia(Integer id) {
        Optional<Licencia> existeLicencia = licenciaRepository.findById(id);
        if (!existeLicencia.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }
        Licencia licencia = existeLicencia.get();
        if (!licencia.isEstado()) {
            throw new IllegalArgumentException("La licencia ya está desactivada");
        }
        licencia.setEstado(false);
        licenciaRepository.save(licencia);
    }

    @Transactional
    public void eliminarLicencia(Integer id) {
        Optional<Licencia> licencia = licenciaRepository.findById(id);
        if (!licencia.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }
        licenciaRepository.deleteById(id);
    }

    @Transactional
    public void editarLicencia(Integer id, Licencia licencia) {
        Optional<Licencia> licenciaExistente = licenciaRepository.findById(id);
        if (!licenciaExistente.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }

        Licencia licenciaActualizar = licenciaExistente.get();
        if (licencia.getNumero() != null && !licencia.getNumero().isEmpty()) {
            licenciaActualizar.setNumero(licencia.getNumero());
        }

        licenciaRepository.save(licenciaActualizar);
    }
}
