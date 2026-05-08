package ar.backend.logistica.models;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList; 
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference; 

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "RUTAS")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_RUTA")
    private int idRuta;

    @Column(name = "TIEMPO_ESTIMADO")
    private Double tiempoEstimado;

    @Column(name = "COSTO_ESTIMADO")
    private Double costoEstimado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOLICITUD", nullable = false)
    @JsonBackReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Solicitud solicitud;

    @OneToMany(mappedBy = "ruta", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @Builder.Default
    @JsonManagedReference
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Tramo> tramos = new ArrayList<>(); 
}