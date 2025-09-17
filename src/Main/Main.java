package Main;

import Main.Java.Entidades.*;
import Main.Java.Repositorio.InMemoryRepository;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {

        // ===============================================================
        //   INICIALIZACIÓN DE LOS REPOSITORIOS
        // ===============================================================
        // Cada repositorio guarda en memoria las entidades creadas.
        // Nos permite simular una "base de datos en memoria".
        InMemoryRepository<Pais> paisRepo = new InMemoryRepository<>();
        InMemoryRepository<Provincia> provinciaRepo = new InMemoryRepository<>();
        InMemoryRepository<Localidad> localidadRepo = new InMemoryRepository<>();
        InMemoryRepository<Domicilio> domicilioRepo = new InMemoryRepository<>();
        InMemoryRepository<Sucursal> sucursalRepo = new InMemoryRepository<>();
        InMemoryRepository<Empresa> empresaRepo = new InMemoryRepository<>();

        // ===============================================================
        //   CREACIÓN DE PAÍS
        // ===============================================================
        Pais argentina = Pais.builder()
                .nombre("Argentina")
                .build();
        paisRepo.save(argentina);  // se guarda y se asigna un ID automático

        // ===============================================================
        //   PROVINCIA: Buenos Aires
        // ===============================================================
        Provincia buenosAires = Provincia.builder()
                .nombre("Buenos Aires")
                .pais(argentina)
                .build();
        provinciaRepo.save(buenosAires);
        argentina.getProvincias().add(buenosAires); // relación bidireccional

        // ===============================================================
        //   LOCALIDAD: CABA + DOMICILIO
        // ===============================================================
        Localidad caba = Localidad.builder()
                .nombre("CABA")
                .provincia(buenosAires)
                .build();
        localidadRepo.save(caba);
        buenosAires.getLocalidades().add(caba); // relación bidireccional

        Domicilio domCaba = Domicilio.builder()
                .calle("Av. Corrientes")
                .numero(1000)
                .cp(1000)
                .localidad(caba)
                .build();
        domicilioRepo.save(domCaba);

        // ===============================================================
        //   LOCALIDAD: La Plata + DOMICILIO
        // ===============================================================
        Localidad laPlata = Localidad.builder()
                .nombre("La Plata")
                .provincia(buenosAires)
                .build();
        localidadRepo.save(laPlata);
        buenosAires.getLocalidades().add(laPlata);

        Domicilio domLaPlata = Domicilio.builder()
                .calle("Calle 50")
                .numero(200)
                .cp(1900)
                .localidad(laPlata)
                .build();
        domicilioRepo.save(domLaPlata);

        // ===============================================================
        //   PROVINCIA: Córdoba + LOCALIDADES Y DOMICILIOS
        // ===============================================================
        Provincia cordoba = Provincia.builder()
                .nombre("Córdoba")
                .pais(argentina)
                .build();
        provinciaRepo.save(cordoba);
        argentina.getProvincias().add(cordoba);

        Localidad cordobaCapital = Localidad.builder()
                .nombre("Córdoba Capital")
                .provincia(cordoba)
                .build();
        localidadRepo.save(cordobaCapital);
        cordoba.getLocalidades().add(cordobaCapital);

        Domicilio domCordobaCap = Domicilio.builder()
                .calle("Bv. San Juan")
                .numero(500)
                .cp(5000)
                .localidad(cordobaCapital)
                .build();
        domicilioRepo.save(domCordobaCap);

        Localidad villaCarlosPaz = Localidad.builder()
                .nombre("Villa Carlos Paz")
                .provincia(cordoba)
                .build();
        localidadRepo.save(villaCarlosPaz);
        cordoba.getLocalidades().add(villaCarlosPaz);

        Domicilio domVCP = Domicilio.builder()
                .calle("Av. San Martín")
                .numero(300)
                .cp(5152)
                .localidad(villaCarlosPaz)
                .build();
        domicilioRepo.save(domVCP);

        // ===============================================================
        //   CREACIÓN DE SUCURSALES
        // ===============================================================
        // Cada sucursal se asocia a un domicilio.
        Sucursal suc1 = Sucursal.builder()
                .nombre("Sucursal1 - CABA")
                .horarioApertura(LocalTime.of(9,0))
                .horarioCierre(LocalTime.of(18,0))
                .esCasaMatriz(true)
                .domicilio(domCaba)
                .build();
        sucursalRepo.save(suc1);

        Sucursal suc2 = Sucursal.builder()
                .nombre("Sucursal2 - La Plata")
                .horarioApertura(LocalTime.of(9,0))
                .horarioCierre(LocalTime.of(18,0))
                .esCasaMatriz(false)
                .domicilio(domLaPlata)
                .build();
        sucursalRepo.save(suc2);

        Sucursal suc3 = Sucursal.builder()
                .nombre("Sucursal3 - Córdoba Capital")
                .horarioApertura(LocalTime.of(8,30))
                .horarioCierre(LocalTime.of(17,30))
                .esCasaMatriz(true)
                .domicilio(domCordobaCap)
                .build();
        sucursalRepo.save(suc3);

        Sucursal suc4 = Sucursal.builder()
                .nombre("Sucursal4 - Villa Carlos Paz")
                .horarioApertura(LocalTime.of(8,30))
                .horarioCierre(LocalTime.of(17,30))
                .esCasaMatriz(false)
                .domicilio(domVCP)
                .build();
        sucursalRepo.save(suc4);

        // ===============================================================
        //   CREACIÓN DE EMPRESAS Y RELACIÓN CON SUCURSALES
        // ===============================================================
        Empresa empresa1 = Empresa.builder()
                .nombre("Empresa1")
                .razonSocial("Empresa Uno SRL")
                .cuil(20300123456L)
                .build();
        empresa1.getSucursales().add(suc1);
        empresa1.getSucursales().add(suc2);
        empresaRepo.save(empresa1);

        Empresa empresa2 = Empresa.builder()
                .nombre("Empresa2")
                .razonSocial("Empresa Dos SA")
                .cuil(27300987654L)
                .build();
        empresa2.getSucursales().add(suc3);
        empresa2.getSucursales().add(suc4);
        empresaRepo.save(empresa2);

        // Asignamos la empresa a cada sucursal (bidireccional)
        suc1.setEmpresa(empresa1);
        suc2.setEmpresa(empresa1);
        suc3.setEmpresa(empresa2);
        suc4.setEmpresa(empresa2);

        // ===============================================================
        //   RESULTADOS DURANTE LA EJECUCIÓN
        // ===============================================================

        // a) Mostrar todas las empresas
        System.out.println("=== Todas las empresas ===");
        empresaRepo.findAll().forEach(System.out::println);

        // b) Buscar empresa por ID
        System.out.println("\n=== Buscar empresa por ID ===");
        System.out.println(empresaRepo.findById(empresa1.getId()).orElse(null));

        // c) Buscar empresa por nombre
        System.out.println("\n=== Buscar por nombre 'Empresa1' ===");
        System.out.println(empresaRepo.genericFindByField("nombre", "Empresa1"));

        // d) Actualizar datos de empresa1 (ejemplo: cambiar su CUIL)
        System.out.println("\n=== Actualizar CUIL de Empresa1 ===");
        empresa1.setCuil(20999888777L);
        empresaRepo.genericUpdate(empresa1.getId(), empresa1);
        System.out.println(empresaRepo.findById(empresa1.getId()).orElse(null));

        // e) Eliminar empresa2
        System.out.println("\n=== Eliminar Empresa2 ===");
        empresaRepo.genericDelete(empresa2.getId());
        System.out.println("Empresas luego de eliminar Empresa2:");
        empresaRepo.findAll().forEach(System.out::println);

        // f) Mostrar sucursales de empresa1
        System.out.println("\n=== Sucursales de Empresa1 ===");
        empresa1.getSucursales().forEach(System.out::println);
    }
}
