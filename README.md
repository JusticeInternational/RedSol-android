# RedSol Android

[![Docker Image CI](https://github.com/JusticeInternational/RedSol-android/workflows/Docker%20Image%20CI/badge.svg?branch=master)](https://github.com/JusticeInternational/RedSol-android/actions)
[![Platform](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-1.7.22-blue.svg)](https://kotlinlang.org)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-28-orange.svg)](https://developer.android.com/about/versions/pie)

**RedSol (Red Solidaria)** es una plataforma móvil nativa para Android diseñada para conectar a organizaciones benéficas, proveedores de servicios sociales y personas beneficiarias. La aplicación facilita la búsqueda, ofrecimiento y gestión de servicios asistenciales (alimentación, salud, acogida, ropa, voluntariado) mediante geolocalización en mapa, catálogo por categorías y registro de atenciones presenciales mediante códigos QR.

---

## Características Principales

### 👥 Perfiles de Usuario y Roles

* **Beneficiarios:**
  * Búsqueda geolocalizada de comedores, centros médicos y recursos sociales con Google Maps.
  * Exploración de catálogo por categorías y servicios ofrecidos.
  * Credencial digital personal con **Código QR único** para identificación rápida en centros asistenciales.
  * Posibilidad de navegar como invitado (*Login Later*) para consultas inmediatas.

* **Proveedores y Organizaciones (Administradores):**
  * Catálogo de recursos y servicios gestionados por la organización.
  * **Escáner QR integrado** para contabilizar y registrar atenciones a beneficiarios en tiempo real.
  * **Registro asistencial a personas no registradas** (alta directa desde formulario de campo).
  * Estadísticas de atenciones brindadas por servicio y categoría.
  * Perfil público de organización con datos de contacto, horarios y ubicación.

---

## 🛠️ Stack Tecnológico y Arquitectura

* **Lenguaje:** Kotlin
* **Arquitectura:** Arquitectura reactiva con MVVM (Model-View-ViewModel) y MVP para navegación legacy.
* **Capa de Red & Datos:**
  * [Apollo GraphQL](https://www.apollographql.com/docs/kotlin/) v2 para consultas y mutaciones tipadas.
  * [OkHttp 4](https://square.github.io/okhttp/) con interceptores de logging y políticas de red controladas.
  * [RxJava 2 & RxAndroid](https://github.com/ReactiveX/RxAndroid) para flujos reactivos asíncronos.
* **Inyección de Dependencias:**
  * [Koin](https://insert-koin.io/) como solución unificada de DI para ViewModels, Presenters, controladores de red y clientes Apollo (`appModule`, `controllersModule`, `presentersModule`, `viewModelsModule`).
* **Interfaz de Usuario (UI):**
  * ViewBinding para acceso seguro a vistas XML.
  * Google Material Components.
* **Mapas & Ubicación:**
  * Google Play Services Maps & Location SDK.
  * [Leku](https://github.com/AdevintaSpain/Leku) para selector de ubicaciones geográficas.
* **Escaneo y Generación de Códigos QR:**
  * [ZXing Android Embedded](https://github.com/journeyapps/zxing-android-embedded) y ZXing Core.

---

## 📁 Estructura del Código

```text
app/src/main/
├── graphql/               # Esquemas y operaciones GraphQL (.graphql)
│   └── com/cleteci/redsolidaria/
├── java/com/cleteci/redsolidaria/
│   ├── BaseApp.kt         # Inicialización global (Koin, Apollo, SharedPreferences)
│   ├── data/              # Proveedor de datos mock locales para pruebas UI
│   ├── di/                # Módulos de inyección de Koin (app, controllers, presenters, viewModels)
│   ├── models/            # Modelos de dominio (User, Organization, Service, Category, Resource)
│   ├── network/           # Capa de red (GraphQLController, GraphQLFactory)
│   ├── ui/                # Vistas y componentes de presentación
│   │   ├── activities/    # MainActivity, SplashActivity, LoginActivity, etc.
│   │   ├── adapters/      # Adaptadores de RecyclerView
│   │   ├── customUIComponents/ # Controlador de navegación y stack
│   │   ├── fragments/     # Fragmentos modulares (escaneo, atenciones, búsqueda, perfil)
│   │   └── resources/     # Recursos ofrecidos y recursos del beneficiario
│   ├── util/              # Utilidades, constantes y preferencias locales
│   └── viewModels/        # ViewModels de arquitectura (OrganizationViewModel, UserAccountViewModel, etc.)
└── res/                   # Layouts, recursos gráficos, estilos y strings localizados
```

---

## 🚀 Requisitos y Configuración del Entorno

### Requisitos Previos
* **Android Studio** (Koala / Ladybug o superior recomendado).
* **JDK:** Java 8 o Java 11 (target JVM 1.8).
* **Android SDK:**
  * `compileSdkVersion`: 28
  * `minSdkVersion`: 19
  * `targetSdkVersion`: 28

### Configuración de Google Maps API Key
Para que el selector de ubicación y los mapas funcionen correctamente, se debe contar con una clave de Google Maps configurada en `app/src/main/res/values/strings.xml`:
```xml
<string name="google_maps_key">TU_GOOGLE_MAPS_API_KEY</string>
```

---

## 💻 Compilación y Ejecución

### Desde Android Studio
1. Clona el repositorio:
   ```bash
   git clone https://github.com/JusticeInternational/RedSol-android.git
   ```
2. Abre el proyecto en Android Studio.
3. Permite que Gradle sincronice las dependencias del proyecto.
4. Selecciona el módulo `app` y ejecuta en un emulador o dispositivo físico (`Shift + F10`).

### Desde la Terminal (Gradle Wrapper)
* **Compilar APK de Debug:**
  ```bash
  ./gradlew assembleDebug
  ```
* **Limpiar el proyecto:**
  ```bash
  ./gradlew clean
  ```

---

## 📄 Licencia

Este proyecto está bajo la Licencia especificada en el archivo [LICENSE](LICENSE).
