package Main.Java.Entidades;

import lombok.*;
import lombok.experimental.SuperBuilder;


@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString(exclude = "provincia")  // Se excluye provincia para evitar la recursividad infinita
@SuperBuilder
public class Localidad {

    private Long id;
    private String nombre;
    private Provincia provincia;


}

