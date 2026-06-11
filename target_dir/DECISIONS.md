# ARCHITECTURE & DESIGN DECISIONS

## [DEC-001] Single Activity & Jetpack Compose Navigation
- **Status**: Approved
- **Context**: Transitioning smoothly between Calculator and Currency Counter with a premium bottom bar.
- **Decision**: Avoid tabs that jump instantly. Use a standard `NavHost` with slide transition animations to mirror typical device sliding interfaces.

## [DEC-002] State Management via ViewModels & StateFlow
- **Status**: Approved
- **Context**: Ensuring unidirectional data flow (UDF) without boilerplate.
- **Decision**: Put mathematical algorithms and formatting under a pure domain directory. Expose UI state to Composables as a single immutable `StateFlow` from a dedicated `ViewModel`.

## [DEC-003] Omission of Repository Pattern (Simplification)
- **Status**: Approved
- **Context**: The application resides fully in-memory and does not interface with network endpoints or cached database files.
- **Decision**: Avoid mock repository or empty cache interfaces. Feature screens interact directly with ViewModels which utilize pure domain utility engines.

## [DEC-004] Premium Light Themes Only
- **Status**: Approved
- **Decision**: Limit UI purely to light, minimal, tactile off-white controls to prevent generic "AI-generated" purple/dark futuristic looks. Honor Apple, Braun and Nothing design languages.
