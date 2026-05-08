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
@Table(name = "DEPOSITOS")
public class Deposito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_DEPOSITO")
    private int idDeposito;

    @Column(name = "NOMBRE" , nullable = false)
    private String nombre;

    @Column(name = "COSTO_ESTADIA_DIARIO" , nullable = false)
    private Double costoEstadiaDiario;
    
    @Column(name = "ID_UBICACION")
    private Integer idUbicacion;

    @OneToMany(mappedBy = "deposito", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonManagedReference("deposito-contenedor")
    @ToString.Exclude            
    @EqualsAndHashCode.Exclude   
    private List<Contenedor> contenedores = new ArrayList<>();

    public void addContenedor (Contenedor contenedor) {
        contenedores.add(contenedor);
    }

    public void deleteContenedor(Contenedor contenedor) {
        contenedores.remove(contenedor);
    }
}