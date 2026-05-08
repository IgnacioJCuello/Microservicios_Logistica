package ar.backend.flota.repository;

import ar.backend.flota.models.Camion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CamionRepository extends JpaRepository<Camion, String> { // ID es String (Patente)
    List<Camion> findByCapacidadVolumenGreaterThanEqualAndCapacidadPesoGreaterThanEqual(
            double capacidadVolumen, double capacidadPeso
    );
}