package ar.backend.flota.repository;

import ar.backend.flota.models.Transportista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface TransportistaRepository extends JpaRepository<Transportista, Integer> {
    // Los métodos CRUD básicos heredados son suficientes por ahora
    Optional <Transportista> findByTelefono(String telefono);
}