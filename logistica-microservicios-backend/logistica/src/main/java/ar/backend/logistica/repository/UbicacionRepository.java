package ar.backend.logistica.repository;

import ar.backend.logistica.models.Ubicacion;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UbicacionRepository extends JpaRepository<Ubicacion, Integer> {
    Optional<Ubicacion> findByLatitudAndLongitud(Double latitud, Double longitud);
}