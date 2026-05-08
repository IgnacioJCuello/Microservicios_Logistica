package ar.backend.logistica.models;

import ar.backend.logistica.models.enums.EstadoContenedor;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CONTENEDORES")
public class Contenedor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CONTENEDOR")
    private int idContenedor;

    @Column(name = "PESO" , nullable = false)
    private Double peso;

    @Column(name = "VOLUMEN" , nullable = false)
    private Double volumen;

    @Enumerated(EnumType.STRING)
    @Column(name = "ESTADO", nullable = false)
    private EstadoContenedor estado;
    
    @OneToOne(mappedBy = "contenedor", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @ToString.Exclude 
    @EqualsAndHashCode.Exclude 
    private Solicitud solicitud; 
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DEPOSITO_ACTUAL")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Deposito deposito;
}