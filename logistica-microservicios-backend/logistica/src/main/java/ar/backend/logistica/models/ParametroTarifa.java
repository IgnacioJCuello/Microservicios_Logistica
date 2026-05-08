package ar.backend.logistica.models;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "PARAMETROS_TARIFA")
public class ParametroTarifa {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PARAMETRO_TARIFA")
    private int idParametroTarifa;

    @Column(name = "PRECIO_COMBUSTIBLE" , nullable = false)
    private double precioCombustible;

    @Column(name = "CARGO_GESTION" , nullable = false)
    private double cargoGestion;

    @OneToMany(mappedBy = "parametroTarifa", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonManagedReference("tarifa-solicitud")
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Solicitud> solicitudes = new ArrayList<>();
}