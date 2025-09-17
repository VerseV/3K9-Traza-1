package Main.Java.Repositorio;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Repositorio genérico en memoria.
 * Esta clase nos permite simular una base de datos en memoria usando un Map,
 * almacenando cualquier tipo de entidad <T>.
 */
public class InMemoryRepository<T> {

    // === Almacenamiento interno de las entidades ===
    // Usamos un HashMap con clave Long (ID autoincremental) y valor T (la entidad)
    protected Map<Long, T> data = new HashMap<>();

    // === Generador de IDs autoincremental ===
    // Nos permite asignar un ID único a cada entidad que guardamos
    protected AtomicLong idGenerator = new AtomicLong();

    // === Método save: guarda una nueva entidad y le asigna un ID ===
    public T save(T entity) {
        // Generamos un nuevo ID incremental
        long id = this.idGenerator.incrementAndGet();

        try {
            // Mediante reflexión llamamos al setId(Long) de la entidad
            // para asignarle este nuevo ID generado
            entity.getClass().getMethod("setId", Long.class).invoke(entity, id);

            // Mostramos en consola la clase guardada y el ID asignado
            String clase = entity.getClass().getName();
            System.out.println(clase + "   id :" + id);
        } catch (Exception e) {
            // Si ocurre algún error con la reflexión mostramos el stacktrace
            e.printStackTrace();
        }

        // Guardamos la entidad en nuestro Map interno
        this.data.put(id, entity);

        // Retornamos la entidad con su ID ya asignado
        return entity;
    }

    // === Método findById: buscar una entidad por su ID ===
    public Optional<T> findById(Long id) {
        // Devuelve un Optional que puede contener la entidad o estar vacío
        return Optional.ofNullable(this.data.get(id));
    }

    // === Método findAll: listar todas las entidades guardadas ===
    public List<T> findAll() {
        // Devolvemos una lista con todos los valores del Map
        return new ArrayList<>(this.data.values());
    }

    // === Método genericUpdate: actualizar una entidad existente ===
    public Optional<T> genericUpdate(Long id, T updatedEntity) {
        // Verificamos si existe la entidad con ese ID
        if (!this.data.containsKey(id)) {
            return Optional.empty();
        } else {
            try {
                // Si existe, mediante reflexión le reasignamos el mismo ID
                Method setIdMethod = updatedEntity.getClass().getMethod("setId", Long.class);
                setIdMethod.invoke(updatedEntity, id);

                // Reemplazamos la entidad vieja por la nueva actualizada
                this.data.put(id, updatedEntity);

                // Devolvemos la entidad actualizada
                return Optional.of(updatedEntity);
            } catch (Exception e) {
                e.printStackTrace();
                return Optional.empty();
            }
        }
    }

    // === Método genericDelete: eliminar una entidad por su ID ===
    public Optional<T> genericDelete(Long id) {
        // Si no existe el ID, devolvemos Optional vacío, si existe removemos y devolvemos la entidad eliminada
        return !this.data.containsKey(id) ? Optional.empty() : Optional.ofNullable(this.data.remove(id));
    }

    // === Método genericFindByField: buscar entidades por un campo ===
    public List<T> genericFindByField(String fieldName, Object value) {
        List<T> results = new ArrayList<>();

        try {
            // Recorremos todas las entidades guardadas
            for (T entity : this.data.values()) {
                // Mediante reflexión obtenemos el método get del campo indicado
                Method getFieldMethod = entity.getClass().getMethod("get" + this.capitalize(fieldName));

                // Ejecutamos el método get para obtener el valor del campo en esa entidad
                Object fieldValue = getFieldMethod.invoke(entity);

                // Si coincide con el valor buscado lo agregamos al resultado
                if (fieldValue != null && fieldValue.equals(value)) {
                    results.add(entity);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return results;
    }

    // === Método auxiliar capitalize: convierte el primer caracter a mayúscula ===

    private String capitalize(String str) {
        if (str != null && !str.isEmpty()) {
            String primeraMayus = str.substring(0, 1).toUpperCase();
            return primeraMayus + str.substring(1);
        } else {
            return str;
        }
    }
}
