package ar.backend.logistica.repository;

import ar.backend.logistica.models.Contenedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ar.backend.logistica.models.enums.EstadoContenedor;
import java.util.List;

@Repository
public interface ContenedorRepository extends JpaRepository<Contenedor, Integer> {
    List<Contenedor> findByEstado(EstadoContenedor estado);

    List<Contenedor> findByEstadoNot(EstadoContenedor estado);


}