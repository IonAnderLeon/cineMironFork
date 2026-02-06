# Reestructuración del Proyecto CineMiron

## Estructura Antigua

```
com/example/cinemiron/
├── MainActivity.kt
├── CineMironApplication.kt
├── data/
│   ├── Pelis.kt ❌
│   ├── PelisRepository.kt ❌
│   ├── Resenas.kt ✅
│   ├── ResenasRepository.kt ✅
│   ├── UserProfile.kt ✅
│   └── UserRepository.kt ✅
├── tmp_common/
│   └── data/
│       └── ApiMapper.kt
├── tmp_di/
│   └── MovieModule.kt
├── tmp_movie/
│   ├── data/
│   │   ├── mapper_impl/
│   │   │   ├── MovieApiMapperImpl.kt
│   │   │   └── MovieDetailApiMapperImpl.kt
│   │   ├── remote/
│   │   │   ├── api/
│   │   │   │   └── MovieApiService.kt
│   │   │   └── models/
│   │   │       ├── Genre.kt
│   │   │       ├── MovieDetailDTO.kt
│   │   │       ├── MovieDto.kt
│   │   │       ├── MovieVideoDto.kt
│   │   │       ├── MovieVideoResponseDto.kt
│   │   │       ├── MovieVideoResultDto.kt
│   │   │       ├── ProductionCompany.kt
│   │   │       ├── ProductionCountry.kt
│   │   │       ├── Result.kt
│   │   │       └── SpokenLanguage.kt
│   │   └── repository_impl/
│   │       └── MovieRepositoryImpl.kt
│   └── domain/
│       ├── models/
│       │   ├── Genre.kt
│       │   ├── Movie.kt
│       │   ├── MovieDetail.kt
│       │   ├── ProductionCompany.kt
│       │   ├── ProductionCountry.kt
│       │   └── SpokenLanguage.kt
│       └── repository/
│           └── MovieRepository.kt
├── tmp_ui/
│   ├── filminfo/
│   │   └── FilmInfoViewModel.kt
│   ├── home/
│   │   └── HomeViewModel.kt
│   └── search/
│       └── SearchViewModel.kt
├── tmp_utils/
│   ├── Extension.kt
│   ├── GenreConstants.kt
│   ├── K.kt
│   └── Response.kt
└── ui/
    ├── components/
    │   ├── BottomNavBar.kt ✅
    │   ├── CardPelis.kt ❌
    │   ├── EditProfileDialog.kt ✅
    │   ├── MovieCard.kt ✅
    │   ├── SearchFiltersDialog.kt ✅
    │   ├── SettingsDialog.kt ✅
    │   └── UserSearchCard.kt ✅
    ├── screens/
    │   ├── FilmInfo.kt ❌
    │   ├── FilmInfoAPI.kt ✅
    │   ├── Home.kt ❌
    │   ├── HomeAPI.kt ✅
    │   ├── Login.kt ✅
    │   ├── Profile.kt ✅
    │   ├── Register.kt ✅
    │   ├── ResetPassword.kt ✅
    │   ├── Review.kt ✅
    │   ├── Search.kt ❌
    │   └── SearchAPI.kt ✅
    └── theme/
        ├── Color.kt ✅
        ├── Theme.kt ✅
        └── Type.kt ✅
```

## Estructura Nueva (Clean Architecture)

```
com/example/cinemiron/
├── MainActivity.kt
├── CineMironApplication.kt
├── core/
│   ├── di/
│   │   └── MovieModule.kt (movido desde tmp_di)
│   └── utils/
│       ├── K.kt (movido desde tmp_utils)
│       ├── Response.kt (movido desde tmp_utils)
│       ├── Extension.kt (movido desde tmp_utils)
│       └── GenreConstants.kt (movido desde tmp_utils)
├── data/
│   ├── local/
│   │   ├── models/
│   │   │   ├── Resenas.kt
│   │   │   └── UserProfile.kt
│   │   └── repository/
│   │       ├── ResenasRepository.kt
│   │       └── UserRepository.kt
│   ├── remote/
│   │   ├── api/
│   │   │   └── MovieApiService.kt (movido desde tmp_movie/data/remote/api)
│   │   └── models/
│   │       ├── Genre.kt (movido desde tmp_movie/data/remote/models)
│   │       ├── MovieDetailDTO.kt
│   │       ├── MovieDto.kt
│   │       ├── MovieVideoDto.kt
│   │       ├── MovieVideoResponseDto.kt
│   │       ├── MovieVideoResultDto.kt
│   │       ├── ProductionCompany.kt
│   │       ├── ProductionCountry.kt
│   │       ├── Result.kt
│   │       └── SpokenLanguage.kt
│   ├── mapper/
│   │   ├── MovieApiMapperImpl.kt (movido desde tmp_movie/data/mapper_impl)
│   │   └── MovieDetailApiMapperImpl.kt
│   └── repository/
│       └── MovieRepositoryImpl.kt (movido desde tmp_movie/data/repository_impl)
├── domain/
│   ├── models/
│   │   ├── Genre.kt (movido desde tmp_movie/domain/models)
│   │   ├── Movie.kt
│   │   ├── MovieDetail.kt
│   │   ├── ProductionCompany.kt
│   │   ├── ProductionCountry.kt
│   │   └── SpokenLanguage.kt
│   ├── repository/
│   │   └── MovieRepository.kt (movido desde tmp_movie/domain/repository)
│   └── common/
│       └── ApiMapper.kt (movido desde tmp_common/data)
├── ui/
│   ├── auth/
│   │   ├── login/
│   │   │   └── LoginScreen.kt (movido desde ui/screens/Login.kt)
│   │   ├── register/
│   │   │   └── RegisterScreen.kt (movido desde ui/screens/Register.kt)
│   │   └── resetpassword/
│   │       └── ResetPasswordScreen.kt (movido desde ui/screens/ResetPassword.kt)
│   ├── home/
│   │   ├── HomeScreen.kt (renombrado desde HomeAPI.kt)
│   │   └── HomeViewModel.kt (movido desde tmp_ui/home)
│   ├── search/
│   │   ├── SearchScreen.kt (renombrado desde SearchAPI.kt)
│   │   └── SearchViewModel.kt (movido desde tmp_ui/search)
│   ├── movie/
│   │   ├── detail/
│   │   │   ├── MovieDetailScreen.kt (renombrado desde FilmInfoAPI.kt)
│   │   │   └── MovieDetailViewModel.kt (renombrado desde FilmInfoViewModel.kt)
│   ├── profile/
│   │   └── ProfileScreen.kt (movido desde ui/screens/Profile.kt)
│   ├── review/
│   │   └── ReviewScreen.kt (movido desde ui/screens/Review.kt)
│   ├── components/
│   │   ├── BottomNavBar.kt
│   │   ├── EditProfileDialog.kt
│   │   ├── MovieCard.kt
│   │   ├── SearchFiltersDialog.kt
│   │   ├── SettingsDialog.kt
│   │   └── UserSearchCard.kt
│   └── theme/
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
```

## Cambios Principales

### 1. Eliminación de Prefijos `tmp_`
- `tmp_common` → `domain/common`
- `tmp_di` → `core/di`
- `tmp_movie` → `data/remote` y `domain`
- `tmp_ui` → `ui/[feature]`
- `tmp_utils` → `core/utils`

### 2. Organización por Feature
- Las pantallas se organizan por funcionalidad (auth, home, search, movie, profile, review)
- Cada feature tiene su ViewModel junto a la pantalla

### 3. Separación de Capas
- **core/**: Utilidades y configuración compartida
- **data/**: Toda la lógica de datos (local y remote)
- **domain/**: Modelos y contratos de dominio
- **ui/**: Toda la interfaz de usuario organizada por feature

### 4. Archivos Eliminados
- `ui/screens/Home.kt` ❌
- `ui/screens/Search.kt` ❌
- `ui/screens/FilmInfo.kt` ❌
- `ui/components/CardPelis.kt` ❌
- `data/Pelis.kt` ❌
- `data/PelisRepository.kt` ❌

### 5. Renombramientos
- `HomeAPI.kt` → `HomeScreen.kt`
- `SearchAPI.kt` → `SearchScreen.kt`
- `FilmInfoAPI.kt` → `MovieDetailScreen.kt`
- `ResetPassword.kt` → `ResetPasswordScreen.kt`
- `FilmInfoViewModel.kt` → `MovieDetailViewModel.kt`

## Beneficios de la Nueva Estructura

1. **Mejor Organización**: Archivos agrupados por funcionalidad y capa
2. **Escalabilidad**: Fácil agregar nuevas features
3. **Mantenibilidad**: Estructura clara y predecible
4. **Clean Architecture**: Separación clara de responsabilidades
5. **Sin Prefijos**: Nombres más limpios sin `tmp_`
6. **Cohesión**: ViewModels junto a sus pantallas correspondientes


