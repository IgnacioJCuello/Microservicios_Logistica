package ar.backend.logistica.service;

import ar.backend.logistica.dto.ContenedorResponseDTO;
import ar.backend.logistica.dto.ContenedoresDepositoDTO;
import ar.backend.logistica.dto.DepositoRequestDTO;
import ar.backend.logistica.dto.DepositoResponseDTO;
import ar.backend.logistica.models.Contenedor;
import ar.backend.logistica.models.Deposito;
import ar.backend.logistica.repository.DepositoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoResponseDTO crear(DepositoRequestDTO dto) {
        log.info("[SERVICIO] [CREAR_DEPOSITO] Procesando alta. Nombre: {}, Costo Diario: {}", dto.getNombre(), dto.getCostoEstadiaDiario());
        
        Deposito deposito = Deposito.builder()
            .nombre(dto.getNombre())
            .costoEstadiaDiario(dto.getCostoEstadiaDiario())
            .idUbicacion(dto.getIdUbicacion())
            .build();

        deposito = depositoRepository.save(deposito);
        
        log.info("[SERVICIO] [CREAR_DEPOSITO] Guardado exitoso. ID Generado: {}", deposito.getIdDeposito());

        return toResponse(deposito);
    }


    public DepositoResponseDTO actualizar(int id, DepositoRequestDTO dto) {
        log.info("[SERVICIO] [ACTUALIZAR_DEPOSITO] Buscando DepositoID: {}", id);
        
        Deposito deposito = depositoRepository.findById(id)
            .orElseThrow(() -> {
                log.error("[SERVICIO] [ERROR] Fallo al actualizar. Depósito ID {} no existe.", id);
                return new RuntimeException("Depósito no encontrado");
            });

        deposito.setNombre(dto.getNombre());
        deposito.setCostoEstadiaDiario(dto.getCostoEstadiaDiario());
        deposito.setIdUbicacion(dto.getIdUbicacion());

        deposito = depositoRepository.save(deposito);
        
        log.info("[SERVICIO] [ACTUALIZAR_DEPOSITO] Datos actualizados correctamente.");
        
        return toResponse(deposito);
    }

    private DepositoResponseDTO toResponse(Deposito d) {
        DepositoResponseDTO res = new DepositoResponseDTO();
        res.setIdDeposito(d.getIdDeposito());
        res.setNombre(d.getNombre());
        res.setCostoEstadiaDiario(d.getCostoEstadiaDiario());
        res.setIdUbicacion(d.getIdUbicacion());
        return res;
    }

    public ContenedoresDepositoDTO consultarContenedores (int idDeposito) {
        log.info("[SERVICIO] [STOCK_DEPOSITO] Verificando inventario para DepositoID: {}", idDeposito);
        
        Deposito deposito = depositoRepository.findById(idDeposito)
            .orElseThrow(() -> {
                log.error("[SERVICIO] [ERROR] Consulta fallida. Depósito ID {} no existe.", idDeposito);
                return new RuntimeException("El deposito no se encuentra cargado");
            });
            
        List<Contenedor> contenedores = deposito.getContenedores();
        
        log.info("[SERVICIO] [STOCK_DEPOSITO] Stock encontrado: {} contenedores.", (contenedores != null ? contenedores.size() : 0));

        List<ContenedorResponseDTO> contenedoresResponse = contenedores
            .stream()
            .map(contenedor -> new ContenedorResponseDTO(
                contenedor.getIdContenedor(),
                contenedor.getPeso(),
                contenedor.getVolumen(),
                contenedor.getEstado().name()
            ))
            .collect(Collectors.toList());
        
        return ContenedoresDepositoDTO.builder()
            .contenedores(contenedoresResponse)
            .build();
    }
}