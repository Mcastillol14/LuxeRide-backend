package com.luxeride.taxistfg.Service;

import com.luxeride.taxistfg.Model.Coche;
import com.luxeride.taxistfg.Model.Licencia;
import com.luxeride.taxistfg.Model.Rol;
import com.luxeride.taxistfg.Model.Usuario;
import com.luxeride.taxistfg.Repository.CocheRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import com.luxeride.taxistfg.Repository.UsuarioRepository;
import com.luxeride.taxistfg.Repository.LicenciaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class CocheService {
    @Autowired
    private CocheRepository cocheRepository;
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private LicenciaRepository licenciaRepository;

    @Transactional
    public void registrarCoche(Coche coche) {
        Optional<Coche> existeCoche = cocheRepository.findByMatricula(coche.getMatricula());
        if (existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula ya existe");
        }
        if (coche.getMatricula() == null || coche.getMatricula().isEmpty()) {
            throw new IllegalArgumentException("La matricula es obligatoria");
        }
        if (coche.getMarca() == null || coche.getMarca().isEmpty()) {
            throw new IllegalArgumentException("La marca es obligatoria");
        }
        if (coche.getModelo() == null || coche.getModelo().isEmpty()) {
            throw new IllegalArgumentException("El modelo es obligatorio");
        }
        Coche CocheCreado = new Coche();
        CocheCreado.setMatricula(coche.getMatricula());
        CocheCreado.setMarca(coche.getMarca());
        CocheCreado.setModelo(coche.getModelo());
        CocheCreado.setDisponible(false);
        CocheCreado.setEstado(true);
        cocheRepository.save(CocheCreado);
    }

    public Page<Coche> listarCoches(Pageable pageable, String matricula) {
        if (matricula != null && !matricula.isEmpty()) {
            return cocheRepository.buscarCochesPorMatricula(matricula, pageable);
        }
        return cocheRepository.findAll(pageable);
    }

    @Transactional
    public void activarCoche(Integer id) {
        Optional<Coche> existeCoche = cocheRepository.findById(id);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula no existe");
        }
        Coche coche = existeCoche.get();
        if (coche.isEstado()) {
            throw new IllegalArgumentException("La matricula ya esta activa");
        }
        coche.setEstado(true);
        cocheRepository.save(coche);
    }

    @Transactional
    public void desactivarCoche(Integer id) {
        Optional<Coche> existeCoche = cocheRepository.findById(id);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula no existe");
        }
        Coche coche = existeCoche.get();
        if (!coche.isEstado()) {
            throw new IllegalArgumentException("La matricula ya esta desactiva");
        }
        coche.setEstado(false);
        cocheRepository.save(coche);
    }

    @Transactional
    public void disponibleCoche(Integer id) {
        Optional<Coche> existeCoche = cocheRepository.findById(id);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula no existe");
        }
        Coche coche = existeCoche.get();
        if (coche.isDisponible()) {
            throw new IllegalArgumentException("La matricula ya esta disponible");
        }
        coche.setDisponible(true);
        cocheRepository.save(coche);
    }

    @Transactional
    public void noDisponibleCoche(Integer id) {
        Optional<Coche> existeCoche = cocheRepository.findById(id);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula no existe");
        }
        Coche coche = existeCoche.get();
        if (!coche.isDisponible()) {
            throw new IllegalArgumentException("La matricula ya esta en no disponible");
        }
        coche.setDisponible(false);
        cocheRepository.save(coche);
    }

    @Transactional
    public void eliminarCoche(Integer id) {
        Optional<Coche> existeCoche = cocheRepository.findById(id);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("La matricula no existe");
        }
        cocheRepository.deleteById(id);
    }

    @Transactional
    public void addUsuarioToCoche(Integer cocheId, Integer usuarioId) {
        Optional<Coche> existeCoche = cocheRepository.findById(cocheId);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("El coche con el ID proporcionado no existe");
        }

        Coche coche = existeCoche.get();

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("El usuario con el ID proporcionado no existe");
        }

        Usuario usuario = usuarioOpt.get();

        if (usuario.getRol() != Rol.ROL_TAXISTA) {
            throw new IllegalArgumentException("El usuario no tiene el rol de TAXISTA");
        }

        coche.addUsuario(usuario);
        cocheRepository.save(coche);
    }
    @Transactional
    public void deleteUsuarioToCoche(Integer cocheId, Integer usuarioId) {
        Optional<Coche> existeCoche = cocheRepository.findById(cocheId);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("El coche con el ID proporcionado no existe");
        }

        Coche coche = existeCoche.get();

        Optional<Usuario> usuarioOpt = usuarioRepository.findById(usuarioId);
        if (!usuarioOpt.isPresent()) {
            throw new IllegalArgumentException("El usuario con el ID proporcionado no existe");
        }

        Usuario usuario = usuarioOpt.get();

        if (!coche.getUsuarios().contains(usuario)) {
            throw new IllegalArgumentException("El usuario no está asignado a este coche");
        }

        coche.removeUsuario(usuario);
        cocheRepository.save(coche);
    }

    @Transactional
    public void addLicenciaToCoche(Integer cocheId, Integer licenciaId) {
        Optional<Coche> existeCoche = cocheRepository.findById(cocheId);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("El coche con el ID proporcionado no existe");
        }

        Coche coche = existeCoche.get();

        Optional<Licencia> existeLicencia = licenciaRepository.findById(licenciaId);
        if (!existeLicencia.isPresent()) {
            throw new IllegalArgumentException("La licencia con el ID proporcionado no existe");
        }

        Licencia licencia = existeLicencia.get();

        if (licencia.getCoche() != null) {
            throw new IllegalArgumentException("La licencia ya está asignada a otro coche");
        }

        coche.setLicencia(licencia);
        licencia.setCoche(coche);

        cocheRepository.save(coche);
        licenciaRepository.save(licencia);
    }

    @Transactional
    public List<Usuario> obtenerUsuariosDeCoche(Integer cocheId) {
        Optional<Coche> existeCoche = cocheRepository.findById(cocheId);
        if (!existeCoche.isPresent()) {
            throw new IllegalArgumentException("El coche con el ID proporcionado no existe");
        }

        Coche coche = existeCoche.get();
        return new ArrayList<>(coche.getUsuarios());
    }
}