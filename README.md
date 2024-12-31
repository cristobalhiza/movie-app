# Movie App Kotlin

## Descripción
**Movie App Hiza** es una aplicación de Android que permite a los usuarios explorar películas populares organizadas por género con paginación dinámica, buscar películas en la base de datos de TMDB, guardar películas favoritas y acceder a ellas incluso sin conexión a Internet. La aplicación utiliza arquitectura **MVVM** e inyección de dependencias para mantener un código modular y escalable.

## Tecnologías Utilizadas

### Lenguajes y Frameworks
- **Kotlin**: Lenguaje principal para el desarrollo.
- **Jetpack Compose**: Framework para la creación de UI declarativa.

### Arquitectura
- **MVVM (Model-View-ViewModel)**: Patrón arquitectónico para separación de responsabilidades.
- **Dagger Hilt**: Gestión de dependencias.

### Red y Persistencia
- **Retrofit**: Cliente HTTP para consumo de APIs REST.
- **OkHttp**: Interceptor de red para registros y control de solicitudes.
- **Room**: Base de datos local para persistencia de datos.

### Manejo de Imágenes
- **Coil**: Cargador de imágenes ligero y optimizado para Compose.

### Coroutines
- **Kotlin Coroutines**: Gestión de tareas asincrónicas.

### Testing
- **JUnit**: Framework de pruebas unitarias.
- **MockK**: Simulaciones para pruebas.
- **Androidx Test**: Herramientas de pruebas para Android.

---

## Funcionalidades
- **Explorar películas populares**: Navegación por géneros con paginación dinámica.
- **Favoritos**: Guardar y acceder a películas favoritas (sin conexión).
- **Buscar películas**: Filtrar películas en la base de datos de TMDB.
- **Detalles de películas**: Ver información detallada de cada película.

---

## Configuración de Clave API
El archivo `key.properties` debe contener la clave de API de TMDB para acceder a los datos. Ejemplo:
```properties
TMDB_API_KEY=tu_clave_api_aqui
