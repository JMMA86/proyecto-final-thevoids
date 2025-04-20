# Proyecto Final - The Voids

Este es el proyecto final del curso de Computación 2 (2025), desarrollado por el equipo **The Voids**. El proyecto
consiste en una aplicación basada en **Spring Boot** que gestiona especialidades, usuarios y citas médicas.

## Tecnologías Utilizadas

- **Java**: Lenguaje principal del backend.
- **Spring Boot**: Framework para el desarrollo del backend.
- **Maven**: Herramienta de gestión de dependencias y construcción.
- **SQL**: Base de datos relacional para el almacenamiento de datos.
- **JavaScript**: Para funcionalidades del frontend (si aplica).

## Características

- Gestión de especialidades médicas.
- Gestión de usuarios y sus especialidades.
- Gestión de citas médicas.
- Validación de datos y manejo de excepciones.
- Cobertura de pruebas con **JaCoCo**.

## Requisitos Previos

- **Java 17** o superior.
- **Maven 3.8** o superior.
- **MySQL** o cualquier base de datos compatible con Spring Data JPA.
- **IntelliJ IDEA** (opcional, pero recomendado).

## Instalación

1. Clona el repositorio:

    ```bash
    git clone https://github.com/Computacion-2-2025/proyecto-final-thevoids.git
    cd proyecto-final-thevoids
    ```

2. Abre el proyecto en tu IDE favorito (recomendado IntelliJ IDEA).

## URLs permitidas hasta el momento

### Recursos Públicos

- `/css/**` - Archivos de estilos.
- `/js/**` - Archivos JavaScript.
- `/img/**` - Archivos de imágenes.

### Recursos Protegidos

- `/web/users/**` - Requiere el permiso `VIEW_USERS`.
- `/web/roles/**` - Requiere el permiso `VIEW_ROLES`.
- `/web/permissions/**` - Requiere el permiso `VIEW_PERMISSIONS`.
- `/web/admin/**` - Requiere el rol `ADMIN`.

### Autenticación

- `/web/auth/login` - Página de inicio de sesión (pública).
- `/web/auth/logout` - URL para cerrar sesión (pública).
- `/web/home` - Redirección tras inicio de sesión exitoso.

## Ejecución de pruebas

Para ejecutar las pruebas unitarias y de integración, utiliza el siguiente comando:

```bash
mvn clean test
```

Esto generará un informe de cobertura de código utilizando **JaCoCo**. Puedes encontrar el informe en
`target/site/jacoco/index.html`.

### Último coverage conseguido

![Coverage Report](/doc/coverage/coverage.png)
