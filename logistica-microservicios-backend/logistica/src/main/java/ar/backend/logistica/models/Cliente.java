package ar.backend.logistica.models;

import lombok.*;
import java.util.List;
import jakarta.persistence.*;
import java.util.ArrayList;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "CLIENTES")
public class Cliente {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_CLIENTE")
    private int idCliente;

    @Column(name = "NOMBRE" , nullable = false)
    private String nombre;

    @Column(name = "APELLIDO" , nullable = false)
    private String apellido;

    @Column(name = "EMAIL" , nullable = false, unique = true)
    private String email;

    @Column(name = "TELEFONO" , nullable = false)
    private String telefono;
    
    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private List<Solicitud> solicitudes = new ArrayList<>();
}
