package ar.backend.logistica.models;

import jakarta.persistence.*;
import lombok.*; 
import java.time.LocalDateTime;
import java.util.List;
import ar.backend.logistica.models.enums.EstadoSolicitud;
import java.util.ArrayList;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SOLICITUDES")
public class Solicitud {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_SOLICITUD")
    private int idSolicitud;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoSolicitud estado;

    @Column(name = "FECHA_HORA_CREACION", nullable = false)
    private LocalDateTime fechaHoraCreacion;

    @Column(name = "COSTO_ESTIMADO")
    private Double costoEstimado;

    @Column(name = "TIEMPO_ESTIMADO")
    private Double tiempoEstimado; 

    @Column(name = "COSTO_FINAL")
    private Double costoFinal;

    @Column(name = "TIEMPO_REAL")
    private Double tiempoReal; 

    @Column(name = "DISTANCIA_TOTAL")
    private Double distanciaTotal;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_RUTA_ASIGNADA")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Ruta rutaAsignada;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CLIENTE")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Cliente cliente;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UBICACION_ORIGEN")
    private Ubicacion ubicacionOrigen;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UBICACION_DESTINO")
    private Ubicacion ubicacionDestino;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PARAMETROS_TARIFA")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private ParametroTarifa parametroTarifa;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CONTENEDOR") 
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Contenedor contenedor;

    @OneToMany(mappedBy = "solicitud", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Ruta> rutasTentativas = new ArrayList<>();
}