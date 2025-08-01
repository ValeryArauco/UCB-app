# UCB App

## Descripción General

UCB App es una aplicación móvil desarrollada para la Universidad Católica Boliviana (UCB) que permite a los docentes registrar el progreso sus materias, elementos de aprendizaje, y seguimiento académico.

## Arquitectura del Proyecto

### Clean Architecture + MVVM

El proyecto sigue una arquitectura limpia (Clean Architecture) con el patrón Model-View-ViewModel (MVVM), organizado en módulos:

```
ucbapp/
├── app/                    # Capa de Presentación (UI)
├── domain/                 # Capa de Dominio (Entidades y Reglas de Negocio)
├── data/                   # Capa de Datos (Repositorios y Fuentes de Datos)
├── framework/              # Capa de Framework (Implementaciones Externas)
└── usecases/              # Casos de Uso (Lógica de Aplicación)
```

### Módulos del Proyecto

#### 1. **app** - Capa de Presentación
- **UI Components**: Pantallas y componentes de Jetpack Compose
- **ViewModels**: Lógica de presentación y manejo de estado
- **Navigation**: Navegación entre pantallas
- **DI**: Configuración de inyección de dependencias con Hilt

#### 2. **domain** - Capa de Dominio
- **Entidades**: Modelos de datos del dominio (Materia, Elemento, etc.)
- **Reglas de Negocio**: Lógica central de la aplicación

#### 3. **data** - Capa de Datos
- **Repositorios**: Implementación de acceso a datos
- **DataSources**: Interfaces para fuentes de datos remotas y locales
- **NetworkResult**: Manejo de resultados de red

#### 4. **framework** - Capa de Framework
- **Implementaciones**: Implementaciones concretas de servicios externos
- **Retrofit**: Configuración de API REST
- **Firebase**: Servicios de Firebase

#### 5. **usecases** - Casos de Uso
- **Lógica de Aplicación**: Operaciones específicas del negocio

## Tecnologías y Dependencias

### Core Technologies
- **Kotlin**: Lenguaje principal de desarrollo
- **Jetpack Compose**: UI declarativa moderna
- **Android SDK**: API 35 (Android 15)
- **Min SDK**: 24 (Android 7.0)

### Arquitectura y Patrones
- **MVVM**: Model-View-ViewModel
- **Clean Architecture**: Separación de capas
- **Repository Pattern**: Patrón repositorio
- **Dependency Injection**: Hilt para inyección de dependencias

### UI/UX
- **Material Design 3**: Sistema de diseño moderno
- **Jetpack Compose**: UI declarativa
- **Navigation Compose**: Navegación entre pantallas
- **Coil**: Carga de imágenes

### Networking y Datos
- **Retrofit**: Cliente HTTP para APIs REST
- **Kotlinx Serialization**: Serialización JSON
- **Firebase**: Autenticación, base de datos y notificaciones
    - Firebase Auth
    - Firebase Database
    - Firebase Messaging


### Herramientas de Desarrollo
- **Sentry**: Monitoreo de errores

## Funcionalidades Principales

### 1. **Autenticación**
- **Google Sign-In**: Inicio de sesión con Google
- **Email/Password**: Inicio de sesión con credenciales institucionales
- **Validación de Usuario**: Verificación de usuarios autorizados
- **Firebase Auth**: Gestión de autenticación

### 2. **Gestión de Materias**
- **Lista de Materias**: Visualización de materias del estudiante
- **Progreso Académico**: Seguimiento de elementos completados y evaluados
- **Navegación**: Acceso a elementos de cada materia

### 3. **Elementos de Aprendizaje**
- **Lista de Elementos**: Visualización de elementos por materia
- **Detalles de Elementos**: Información detallada de cada elemento
- **Saberes**: Gestión de conocimientos específicos
- **Recuperatorios**: Manejo de evaluaciones de recuperación

### 4. **Notificaciones**
- **Push Notifications**: Notificaciones push con Firebase
- **Gestión de Tokens**: Registro y actualización de tokens de dispositivo

### 5. **Perfil de Usuario**
- **Información Personal**: Datos del estudiante

## Estructura de Navegación

### Pantallas Principales
```
LoginScreen
    ↓
MateriasScreen (Home-lista de materias)
    ↓
ElementosScreen (Lista de EC)
    ↓
ElementoDetailsScreen (Lista de saberes, evaluación, recuperatorios y comentarios)
```

### Navegación Inferior
- **Inicio**: MateriasScreen
- **Notificaciones**: NotificationsScreen
- **Perfil**: PerfilScreen

### Flujo de Navegación
1. **Login**: Autenticación inicial
2. **Materias**: Lista de materias
3. **Elementos**: Elementos de una materia específica
4. **Detalles**: Información detallada de un elemento
5. **Notificaciones**: Centro de notificaciones
6. **Perfil**: Información del usuario

## Configuración del Proyecto

### Variables de Entorno
```kotlin
// Configurar en gradle.properties o variables de entorno
GOOGLE_WEB_CLIENT_ID=your_web_client_id
FIREBASE_PROJECT_ID=your_firebase_project_id
```


## Estado de la Aplicación

### Estados de UI
Cada pantalla maneja diferentes estados:

```kotlin
sealed class UIState {
    object Loading : UIState()
    class Loaded(val data: T) : UIState()
    class Error(val message: String) : UIState()
}
```

### Manejo de Errores
- **NetworkResult**: Wrapper para resultados de red
- **Error Handling**: Manejo centralizado de errores

## Seguridad

### Autenticación
- **Firebase Auth**: Autenticación segura
- **Verificación de Usuario**: Validación en backend


## Monitoreo

### Sentry
- **Error Tracking**: Monitoreo de errores en producción
- **Performance Monitoring**: Monitoreo de rendimiento
- **Release Tracking**: Seguimiento de versiones


## Documentación Adicional

### Archivos Importantes
- `MainActivity.kt`: Punto de entrada de la aplicación
- `AppNavigation.kt`: Configuración de navegación
- `AppModule.kt`: Configuración de inyección de dependencias
- `AndroidManifest.xml`: Configuración de la aplicación


**Desarrollado para la Universidad Católica Boliviana** 

