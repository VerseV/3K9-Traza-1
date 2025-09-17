package Main;


import Main.Java.Entidades.*;
import Main.Java.Repositorio.InMemoryRepository;

import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class Main {
    public static void main(String[] args) {

        // === REPOSITORIOS ===
        InMemoryRepository<Sucursal> sucursalRepo = new InMemoryRepository<>();
        InMemoryRepository<Empresa> empresaRepo = new InMemoryRepository<>();

        // === 1) PAÍS ===
        Pais argentina = Pais.builder()
                .nombre("Argentina")
                .build();

        // === 2) PROVINCIAS ===
        Provincia buenosAires = Provincia.builder()
                .nombre("Buenos Aires")
                .pais(argentina)
                .build();

        Provincia cordoba = Provincia.builder()
                .nombre("Córdoba")
                .pais(argentina)
                .build();

        argentina.getProvincias().add(buenosAires);
        argentina.getProvincias().add(cordoba);

        // === 3) LOCALIDADES + DOMICILIOS ===
        Localidad caba = Localidad.builder()
                .nombre("CABA")
                .provincia(buenosAires)
                .build();
        buenosAires.getLocalidades().add(caba);

        Domicilio domCaba = Domicilio.builder()
                .calle("Av. Corrientes")
                .numero(1000)
                .cp(1000)
                .piso(1)
                .nroDpto(1)
                .localidad(caba)
                .build();

        Localidad laPlata = Localidad.builder()
                .nombre("La Plata")
                .provincia(buenosAires)
                .build();
        buenosAires.getLocalidades().add(laPlata);

        Domicilio domLaPlata = Domicilio.builder()
                .calle("Calle 50")
                .numero(200)
                .cp(1900)
                .piso(2)
                .nroDpto(2)
                .localidad(laPlata)
                .build();

        Localidad cordobaCapital = Localidad.builder()
                .nombre("Córdoba Capital")
                .provincia(cordoba)
                .build();
        cordoba.getLocalidades().add(cordobaCapital);

        Domicilio domCordobaCap = Domicilio.builder()
                .calle("Bv. San Juan")
                .numero(500)
                .cp(5000)
                .piso(3)
                .nroDpto(3)
                .localidad(cordobaCapital)
                .build();

        Localidad villaCarlosPaz = Localidad.builder()
                .nombre("Villa Carlos Paz")
                .provincia(cordoba)
                .build();
        cordoba.getLocalidades().add(villaCarlosPaz);

        Domicilio domVCP = Domicilio.builder()
                .calle("Av. San Martín")
                .numero(300)
                .cp(5152)
                .piso(4)
                .nroDpto(4)
                .localidad(villaCarlosPaz)
                .build();

        // === 4) SUCURSALES === (crear y guardar en su repositorio)
        Sucursal suc1 = Sucursal.builder()
                .nombre("Sucursal1 - CABA")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(true)
                .domicilio(domCaba)
                .build();

        Sucursal suc2 = Sucursal.builder()
                .nombre("Sucursal2 - La Plata")
                .horarioApertura(LocalTime.of(9, 0))
                .horarioCierre(LocalTime.of(18, 0))
                .esCasaMatriz(false)
                .domicilio(domLaPlata)
                .build();

        Sucursal suc3 = Sucursal.builder()
                .nombre("Sucursal3 - Córdoba Capital")
                .horarioApertura(LocalTime.of(8, 30))
                .horarioCierre(LocalTime.of(17, 30))
                .esCasaMatriz(true)
                .domicilio(domCordobaCap)
                .build();

        Sucursal suc4 = Sucursal.builder()
                .nombre("Sucursal4 - Villa Carlos Paz")
                .horarioApertura(LocalTime.of(8, 30))
                .horarioCierre(LocalTime.of(17, 30))
                .esCasaMatriz(false)
                .domicilio(domVCP)
                .build();

        // Guardarlas en su repositorio para obtener IDs automáticos
        sucursalRepo.save(suc1);
        sucursalRepo.save(suc2);
        sucursalRepo.save(suc3);
        sucursalRepo.save(suc4);

        // === 5) EMPRESAS ===
        Empresa empresa1 = Empresa.builder()
                .nombre("Empresa1")
                .razonSocial("Empresa Uno SRL")
                .cuil(20300123456L)
                .sucursales(new HashSet<>(Set.of(suc1, suc2)))
                .build();

        Empresa empresa2 = Empresa.builder()
                .nombre("Empresa2")
                .razonSocial("Empresa Dos SA")
                .cuil(27300987654L)
                .sucursales(new HashSet<>(Set.of(suc3, suc4)))
                .build();

        // Relación inversa: asignar empresa a sucursales
        suc1.setEmpresa(empresa1);
        suc2.setEmpresa(empresa1);
        suc3.setEmpresa(empresa2);
        suc4.setEmpresa(empresa2);

        // Guardar empresas en repositorio
        empresaRepo.save(empresa1);
        empresaRepo.save(empresa2);

        // === 6) CRUD SOLICITADO ===

        // a) Mostrar todas las empresas
        System.out.println("=== Todas las empresas ===");
        empresaRepo.findAll().forEach(System.out::println);

        // b) Buscar empresa por ID
        System.out.println("\n=== Buscar empresa por ID ===");
        Optional<Empresa> empresaBuscada = empresaRepo.findById(empresa1.getId());
        empresaBuscada.ifPresent(System.out::println);

        // c) Buscar empresa por nombre
        System.out.println("\n=== Buscar empresa por nombre 'Empresa1' ===");
        List<Empresa> listaPorNombre = empresaRepo.genericFindByField("nombre", "Empresa1");
        listaPorNombre.forEach(System.out::println);

        // d) Actualizar CUIL de empresa1
        System.out.println("\n=== Actualizar CUIL de Empresa1 ===");
        empresa1.setCuil(20999888777L);
        empresaRepo.genericUpdate(empresa1.getId(), empresa1);
        empresaRepo.findById(empresa1.getId()).ifPresent(System.out::println);

        // e) Eliminar empresa2
        System.out.println("\n=== Eliminar Empresa2 ===");
        empresaRepo.genericDelete(empresa2.getId());
        empresaRepo.findAll().forEach(System.out::println);

        // Extra: mostrar sucursales de empresa1 con IDs
        System.out.println("\n=== Sucursales de Empresa1 ===");
        empresaRepo.findById(empresa1.getId())
                .ifPresent(e -> e.getSucursales().forEach(System.out::println));

        // Extra opcional: mostrar sucursales desde su propio repo
        System.out.println("\n=== Todas las sucursales en repositorio ===");
        sucursalRepo.findAll().forEach(System.out::println);
    }
}