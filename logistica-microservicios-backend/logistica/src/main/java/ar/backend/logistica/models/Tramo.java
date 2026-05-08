package ar.backend.logistica.models;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonBackReference;
import ar.backend.logistica.models.enums.EstadoTramo;

@Entity
@Table(name = "TRAMOS")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Tramo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_TRAMO")
    private int idTramo;

    @Column(name = "TIPO_TRAMO")
    private String tipoTramo;

    @Column(name = "NUMERO_ORDEN")
    private Integer numeroOrden;

    @Column(name = "DISTANCIA_ESTIMADA")
    private Double distanciaEstimada;

    @Column(name = "FECHA_HORA_INICIO_ESTIMADA")
    private LocalDateTime fechaHoraInicioEstimada;

    @Column(name = "FECHA_HORA_FIN_ESTIMADA")
    private LocalDateTime fechaHoraFinEstimada;

    @Column(name = "FECHA_HORA_INICIO_REAL")
    private LocalDateTime fechaHoraInicioReal;

    @Column(name = "FECHA_HORA_FIN_REAL")
    private LocalDateTime fechaHoraFinReal;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoTramo estado;

    @Column(name = "PATENTE_CAMION")
    private String patenteCamion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UBICACION_ORIGEN")
    private Ubicacion ubicacionOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UBICACION_DESTINO")
    private Ubicacion ubicacionDestino;

    @Column(name = "ID_TRANSPORTISTA")
    private Integer idTransportista;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RUTA", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ruta ruta;
}