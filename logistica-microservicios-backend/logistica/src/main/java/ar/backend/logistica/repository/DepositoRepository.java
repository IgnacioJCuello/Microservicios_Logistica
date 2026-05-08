package ar.backend.logistica.repository;

import ar.backend.logistica.models.Deposito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
@Repository
public interface DepositoRepository extends JpaRepository<Deposito, Integer> {

    //Busca un deposito qu eeste ubicado en una ubicacion especifica
    Optional<Deposito> findByIdUbicacion(int idUbicacion);
}