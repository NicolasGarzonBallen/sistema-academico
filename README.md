# Sistema Académico — Laboratorio 2 (Sistemas Distribuidos)

Backend con CRUD completo (GET, POST, PUT, PATCH, DELETE) para **Estudiantes**, **Materias** e
**Inscripciones**, sobre Spring Boot + MySQL, documentado con Swagger/OpenAPI.

## Requisitos

- Java 17+
- Maven 3.9+
- MySQL corriendo y accesible (por defecto se espera en `localhost:3307`, ver `application.properties`)

## 1. Base de datos

Ejecuta el script `script_base_datos.sql` (incluido en la raíz del proyecto) en tu instancia de MySQL:

```bash
mysql -u root -p -P 3307 < script_base_datos.sql
```

Esto crea la base `sistema_academico` y las tablas `estudiante`, `materia` e `inscripcion`.

## 2. Configuración

Revisa `src/main/resources/application.properties`: el puerto, usuario y contraseña deben coincidir
con tu instalación local de MySQL.

> ⚠️ **Importante para la entrega**: `application.properties` en este proyecto tiene tu usuario y
> contraseña de MySQL en texto plano. Si vas a subir el repositorio (aunque sea a un repo privado),
> considera mover esas credenciales a variables de entorno antes de hacer el primer commit, por ejemplo:
> ```properties
> spring.datasource.username=${DB_USERNAME:root}
> spring.datasource.password=${DB_PASSWORD:}
> ```
> y agregar `application.properties` (o un `application-local.properties`) a tu `.gitignore`.

## 3. Ejecutar

```bash
mvn spring-boot:run
```

Al arrancar, `DataLoader` puebla automáticamente la base con **1000 estudiantes** y **20 materias**
sintéticas (usando Datafaker) si aún no existen suficientes registros. Es idempotente: si ya hay
1000+ estudiantes o 20+ materias, no vuelve a insertar.

## 4. Swagger / OpenAPI

Con la app corriendo, entra a:

```
http://localhost:8080/swagger-ui.html
```

Desde ahí puedes probar ("Try it out") cada endpoint de las tres entidades.

## 5. El GET de Estudiantes (requisito especial)

`GET /estudiantes` soporta paginación, ordenamiento y filtros combinables:

```
GET /estudiantes?page=0&size=20&sortBy=nombre&sortDir=asc&carrera=Sistemas&estado=true&fechaNacimientoDesde=2000-01-01
```

| Parámetro | Descripción |
|---|---|
| `page` | página, empieza en 0 (por defecto 0) |
| `size` | tamaño de página, 1-200 (por defecto 20) |
| `sortBy` | `codigo`, `nombre`, `correo`, `fechaIngreso`, `fechaNacimiento`, `estado`, `carrera` |
| `sortDir` | `asc` o `desc` |
| `nombre`, `correo`, `carrera` | filtro parcial (contiene, insensible a mayúsculas) |
| `estado` | filtro exacto `true`/`false` |
| `fechaNacimientoDesde` / `fechaNacimientoHasta` | rango de fecha de nacimiento (ISO `yyyy-MM-dd`) |
| `fechaIngresoDesde` / `fechaIngresoHasta` | rango de fecha de ingreso (ISO `yyyy-MM-dd`) |

Todos los filtros son opcionales y se combinan con AND. La implementación usa
`Specification<Estudiante>` (JPA Criteria API), no un `findByX` fijo, para que cualquier combinación
de filtros funcione sin tener que escribir un método de repositorio por cada caso.

## Estructura del proyecto

```
entity/          Estudiante, Materia (+ MateriaId embebida), Inscripcion
repository/      Interfaces JpaRepository (+ JpaSpecificationExecutor en Estudiante)
dto/             DTOs de entrada (Request/Patch) y salida (Response) por entidad
specification/   Filtros dinámicos de Estudiante
service/         Lógica de negocio y mapeo entidad <-> DTO
controller/      Endpoints REST + documentación Swagger
exception/       Manejo centralizado de errores -> códigos HTTP coherentes
config/          DataLoader (carga inicial) y configuración de OpenAPI
```

## Códigos de error

`GlobalExceptionHandler` centraliza el mapeo de excepciones a HTTP:

| Situación | Código |
|---|---|
| Recurso no encontrado (código/id inexistente) | 404 |
| Recurso duplicado (código o correo ya existen) | 409 |
| Regla de negocio violada (ej. inscribir con estudiante/materia inactivos) | 422 |
| Body inválido / faltan campos obligatorios | 400 |
| Parámetro de query inválido (ej. `sortBy` no permitido, `size` fuera de rango) | 400 |
| Creación exitosa | 201 |
| Eliminación exitosa | 204 |

## Pendiente / fuera de alcance de esta fase

- Frontend
- Autenticación/autorización
- Separación en microservicios (fases 2 y 3 del proyecto incremental)

## Notas sobre las entidades

- `Estudiante`: PK natural `codigo` (VARCHAR).
- `Materia`: PK compuesta `(codigo, grupo)` → se expone en rutas como `/materias/{codigo}/{grupo}`.
- `Inscripcion`: PK autoincremental `id`, con FKs a `Estudiante` y a la clave compuesta de `Materia`.
  Al crear/reemplazar una inscripción se valida que el estudiante y la materia existan (404 si no)
  y que ambos estén activos (422 si no).
