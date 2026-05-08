package ar.backend.logistica.repository;

import ar.backend.logistica.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org. springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente,Integer>{

    Optional <Cliente> findByEmail(String email);

    List<Cliente> findByApellidoContainingIgnoreCase(String apellido);
    
}
