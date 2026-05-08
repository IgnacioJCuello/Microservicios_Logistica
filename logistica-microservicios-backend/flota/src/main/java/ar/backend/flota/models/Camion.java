package ar.backend.flota.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "CAMIONES")
public class Camion {

    @Id 
    @Column(name = "PATENTE", unique = true, nullable = false)
    private String patente; 

    @Column(name = "CAPACIDAD_PESO")
    private Double capacidadPeso;

    @Column(name = "CAPACIDAD_VOLUMEN")
    private Double capacidadVolumen;

    @Column(name = "CONSUMO_COMBUSTIBLE_X_KM")
    private Double consumoCombustibleXKilometro;

    @Column(name = "DISPONIBILIDAD")
    private String disponibilidad;

}