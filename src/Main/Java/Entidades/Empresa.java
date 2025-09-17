package Main.Java.Entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString(exclude = "sucursales")  // Se excluye sucursales para evitar la recursividad infinita
@SuperBuilder
public class Empresa {

    private Long id;
    private String nombre;
    private String razonSocial;
    private Long cuil;
    private String logo;

    @Builder.Default
    private Set<Sucursal> sucursales = new HashSet<>();


}

