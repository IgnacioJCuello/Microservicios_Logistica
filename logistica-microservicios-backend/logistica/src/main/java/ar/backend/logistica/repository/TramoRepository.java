package ar.backend.logistica.repository;

import ar.backend.logistica.models.Tramo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TramoRepository extends JpaRepository<Tramo, Integer> {
    int countByRutaIdRutaAndNumeroOrdenGreaterThan(int idRuta, Integer numeroOrden);
    List<Tramo> findByRutaIdRuta(Integer idRuta);

}