# Integración de Firebase en cineMirón

Documentación breve de cómo está integrado Firebase en la aplicación Android **cineMirón**.

---

## 1. Configuración del proyecto

### Dependencias (`app/build.gradle.kts`)

- **Firebase BOM** (Bill of Materials) `34.6.0` para versiones coherentes.
- **Firebase Auth**: autenticación (email/contraseña).
- **Firebase Firestore**: base de datos en tiempo real para perfiles de usuario.
- **Firebase Analytics**: analíticas (incluido por defecto).

```kotlin
implementation(platform("com.google.firebase:firebase-bom:34.6.0"))
implementation("com.google.firebase:firebase-auth")
implementation("com.google.firebase:firebase-firestore")
implementation("com.google.firebase:firebase-analytics")
```

### Plugin

- En el proyecto raíz: `com.google.gms.google-services` (versión 4.4.2).
- En el módulo `app`: `id("com.google.gms.google-services")`.

La app espera un archivo `google-services.json` en `app/` (generado desde Firebase Console) para conectar con tu proyecto Firebase.

---

## 2. Firebase Authentication

### Inicialización

- Se usa **Firebase Auth** como singleton en `MainActivity`:
  - `auth = Firebase.auth` (equivalente a `FirebaseAuth.getInstance()`).
- Ese `auth` se pasa por parámetro a las pantallas que lo necesitan: login, registro, perfil, etc.

### Flujos utilizados

| Acción        | Uso en la app |
|---------------|----------------|
| **Registro**  | `auth.createUserWithEmailAndPassword(email, password)` en `RegisterScreen`; tras éxito se crea el documento del usuario en Firestore con `saveUserProfileToFirestore`. |
| **Login**     | `auth.signInWithEmailAndPassword(email, password)` en `LoginScreen`; el email puede resolverse desde el username vía Firestore (`findEmailByUsername`). Tras login se actualiza `lastLogin` en Firestore. |
| **Cerrar sesión** | Uso de `auth` (por ejemplo `auth.signOut()`) en la pantalla de perfil / navegación. |
| **Reset contraseña** | `auth.sendPasswordResetEmail(email)` en `ResetPasswordScreen`. |

El **estado de autenticación** (si hay usuario logueado o no) se usa para decidir la pantalla de inicio (`startDestination = "login"` si no hay sesión) y el acceso a perfil y búsqueda de usuarios.

---

## 3. Cloud Firestore

### Colección principal: `users`

Cada documento tiene como **ID** el `userId` de Firebase Auth (UID).

### Estructura del documento de usuario

Estructura actual (recomendada):

- **`basicInfo`** (mapa):
  - `email`, `username`, `fechaRegistro` (timestamp de servidor).
- **`profileInfo`** (mapa):
  - `bio`, `fotoUrl`, `ubicacion`, `perfilPublico`, `ultimaActualizacion`.
- **`settings`** (mapa):
  - `tema`, `notificaciones`, `colorPrimario`, `idioma`.

También se escribe **`lastLogin`** (timestamp) al iniciar sesión. El código en `UserProfile.kt` soporta tanto esta estructura como una antigua (campos planos `email`, `username`, etc.) para compatibilidad.

### Modelos Kotlin

- **`UserProfile`**: `userId`, `basicInfo`, `profileInfo`, `settings`.
- **`UserBasicInfo`**, **`UserProfileInfo`**, **`UserSettings`**: datos y `fromMap`/`toMap` para Firestore.
- **`UserProfile.fromDocument(DocumentSnapshot)`**: construye `UserProfile` desde un documento (estructura nueva o antigua).

---

## 4. Repositorio y pantallas que usan Firestore

### `UserRepository.kt` (paquete `data.local.repository`)

Funciones que hablan con Firestore:

- **`loadUserProfile(userId, onSuccess, onError)`**: obtiene el documento `users/{userId}` y devuelve un `UserProfile` (o perfil vacío si no existe).
- **`updateUserProfileInfo(...)`**: actualiza solo el mapa `profileInfo` del usuario (bio, foto, ubicación, perfil público, etc.).
- **`saveUserProfileToFirestore(userId, email, username, ...)`**: crea o sobrescribe el documento del usuario (por ejemplo tras registro). Comprueba que el `username` no esté en uso con `whereEqualTo("basicInfo.username", ...)`.

Se usa **`FieldValue.serverTimestamp()`** para `fechaRegistro`, `ultimaActualizacion` y `lastLogin`.

### Pantallas / lógica

- **LoginScreen**: `findEmailByUsername` (consulta por `basicInfo.username`), `loginWithEmail`, `updateLastLogin` (escribe en `users/{userId}`).
- **RegisterScreen**: tras crear usuario con Auth, llama a `saveUserProfileToFirestore`.
- **ProfileScreen**: recibe `FirebaseAuth`; la carga/edición del perfil usa las funciones del repositorio que leen/escriben en `users`.
- **SearchScreen**: búsqueda de usuarios por texto y por ubicación:
  - Consultas a `users` con `whereEqualTo("profileInfo.perfilPublico", true)` y filtrado en cliente por nombre, bio, ubicación.
  - `loadNearbyUsersAPI(location, ...)` y `loadPublicUsersAPI(...)` para listar usuarios públicos.

---

## 5. Resumen de archivos relevantes

| Archivo | Uso de Firebase |
|--------|------------------|
| `app/build.gradle.kts` | Dependencias y plugin Google Services. |
| `MainActivity.kt` | Inicialización de `Firebase.auth` y navegación según sesión. |
| `UserRepository.kt` | Lectura/escritura de perfiles en Firestore (`users`). |
| `UserProfile.kt` (y modelos) | Modelos y parsing desde `DocumentSnapshot` / mapas. |
| `LoginScreen.kt` | Auth (login) + Firestore (buscar email por username, `lastLogin`). |
| `RegisterScreen.kt` | Auth (registro) + Firestore (crear documento en `users`). |
| `ResetPasswordScreen.kt` | Auth (envío de email de restablecimiento). |
| `ProfileScreen.kt` | Auth (usuario actual) + datos de perfil desde Firestore. |
| `SearchScreen.kt` | Firestore (consultas a `users` para búsqueda y listados públicos). |

---

## 6. Reglas y permisos

Los mensajes de error del repositorio hacen referencia a **reglas de seguridad de Firestore**. Es necesario configurar en Firebase Console reglas que:

- Permitan **lectura/escritura** en la colección `users` según tu modelo (por ejemplo: solo el propio usuario puede escribir su documento, y solo perfiles públicos visibles para el resto).
- Ajusten permisos para que las consultas usadas en la app (por `basicInfo.username`, `profileInfo.perfilPublico`, `profileInfo.ubicacion`) estén permitidas.

Sin reglas adecuadas pueden aparecer errores tipo `PERMISSION_DENIED` o "Missing or insufficient permissions".

---

## 7. Flujo resumido

1. **Arranque**: `MainActivity` inicializa `Firebase.auth` y define la ruta inicial (p. ej. login si no hay usuario).
2. **Registro**: Auth crea usuario → se llama a `saveUserProfileToFirestore` para crear el documento en `users`.
3. **Login**: Se resuelve email desde username (Firestore) si hace falta → Auth inicia sesión → se actualiza `lastLogin` en Firestore.
4. **Perfil**: Se carga/actualiza con `loadUserProfile` y `updateUserProfileInfo` (Firestore).
5. **Búsqueda**: Consultas a `users` con `profileInfo.perfilPublico == true` y filtros por nombre/bio/ubicación.

Si quieres extender la documentación (por ejemplo reglas de ejemplo o diagramas), se puede añadir en este mismo archivo o en otros bajo `docs/`.
