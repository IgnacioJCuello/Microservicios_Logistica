package ar.backend.logistica.repository;

import ar.backend.logistica.models.ParametroTarifa;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParametroRepository extends JpaRepository<ParametroTarifa, Integer> {
    Optional<ParametroTarifa> findByPrecioCombustibleAndCargoGestion(double precioCombustible, double cargoGestion);
}