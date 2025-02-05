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
        Optional<Licencia> existingLicenciaByNumero = licenciaRepository.findByNumero(licencia.getNumero());
        
        if (existingLicenciaByNumero.isPresent()) {
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
            return licenciaRepository.findByNumeroContaining(numero, pageable);
        }
        return licenciaRepository.findAll(pageable);
    }

    @Transactional
    public void activarLicencia(Integer id) {
        Optional<Licencia> licenciaOpt = licenciaRepository.findById(id);
        if (!licenciaOpt.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }
        
        Licencia licencia = licenciaOpt.get();
        if (licencia.isEstado()) {
            throw new IllegalArgumentException("La licencia ya está activa");
        }
        
        licencia.setEstado(true);
        licenciaRepository.save(licencia);
    }

    @Transactional
    public void desactivarLicencia(Integer id) {
        Optional<Licencia> licenciaOpt = licenciaRepository.findById(id);
        if (!licenciaOpt.isPresent()) {
            throw new IllegalArgumentException("La licencia no existe");
        }
        
        Licencia licencia = licenciaOpt.get();
        if (!licencia.isEstado()) {
            throw new IllegalArgumentException("La licencia ya está desactivada");
        }
        
        licencia.setEstado(false);
        licenciaRepository.save(licencia);
    }
}
