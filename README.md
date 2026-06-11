# 🪙 CashCalc Pro - Project Blueprint

## Overview
**CashCalc Pro** is a premium, offline-first Android application designed to combine a robust standard calculator with a specialized Indian currency counter. It features a highly polished, tactile user interface with spring physics, custom haptics, and fluid animations.

## ✨ Key Features

### 1. Advanced Calculator
*   **Mathematical Engine:** Powered by `EvalEx` for robust, safe string-based mathematical evaluation (handles implicit multiplication, division-by-zero safety, and percentage logic).
*   **Live Expression & Hierarchy:** Displays the ongoing expression and real-time evaluated result with clear typographic hierarchy.
*   **Premium Keypad:** Custom `SoftTactileButton` components featuring CSS-like hover effects, active spring-scale physics (`stiffness = 1200f`), and localized lightweight haptic feedback (`TextHandleMove`).

### 2. Currency Ledger (Cash Counter)
*   **Indian Denominations:** Quick counters for ₹500, ₹200, ₹100, ₹50, ₹20, and ₹10 notes.
*   **Interaction Modes:** Tap to increment (+1), tap the "-" to decrement, or explicitly type note quantities via keyboard (with instant `ImeAction.Done` focus clearing).
*   **Visual Flourishes:** Tap-to-add features a floating, fading chip animation (`PopChipAnimation`) that rises organically above the interaction point.
*   **Grand Total:** Real-time Indian Number System formatted grand total calculation.

### 3. Fluid Navigation
*   **Swipe-based Paging:** Edge-to-edge `HorizontalPager` without a traditional bottom navigation bar. Employs a parallax slide transition and subtle opacity-driven directional arrow hints for elegant discoverability.

---

## 🛠 Tech Stack & Architecture

*   **Language:** Kotlin
*   **UI Framework:** Jetpack Compose
*   **Architecture:** Clean-Architecture inspired MVVM (Model-View-ViewModel).
    *   No heavy DI layers (Hilt/Dagger) used; ViewModels are minimally and cleanly instantiated.
*   **State Management:** Kotlin `StateFlow`, `MutableStateFlow`, and `update { }` state mutations.
*   **Core Logic:** Fast calculation engines mapped directly to Compose UI state representing independent, distinct UI modules.
*   **Design System:** Custom Material Design 3 implementation stripped of standard material styling, replaced with a specialized Soft/Tactile premium light aesthetic.

---

## 📁 Project Structure

The application is heavily modularized by feature, ensuring easy domain isolation:

```text
app/src/main/java/com/example/
├── core/
│   ├── components/       # SoftTactileButton, AnimatedScalingWrapper, ArrowHints
│   ├── navigation/       # NavigationGraph (HorizontalPager)
│   └── theme/            # Type, Colors, AppTheme (Premium Light Aesthetic)
├── features/
│   ├── calculator/
│   │   ├── domain/       # CalculatorEngine, CalculatorAction models
│   │   └── presentation/ # CalculatorScreen, CalculatorViewModel, UiState
│   └── currencycounter/
│       ├── domain/       # Currency math, Denomination data classes
│       └── presentation/ # CurrencyCounterScreen, CurrencyViewModel, Dashboard
```

---

## 🎨 UI & UX Philosophy

*   **Hyper-Responsive Feel:** Animations explicitly discard standard `tween` (linear/ease) animations in favor of Compose `spring` physics. Specifically tuned mappings (e.g., `dampingRatio = 0.62f` and `stiffness = 1200f`) ensure UI reacts instantly to user taps.
*   **Edge-to-Edge Design:** Heavy utilization of `statusBarsPadding` and `navigationBarsPadding`.
*   **Subtle Discoverability:** Features like the secondary Currency Counter are hinted via a ~18% opacity chevron that elegantly fades out once the user begins a swipe drag gesture.

---

## 🚀 Future Scope / Phase 4 Roadmap
For developers taking over this project or building atop this blueprint, these are the immediate roadmap targets:

1.  **Persistent Storage:** Integrate `Room Database` to save cash counter states and calculator history logs safely across App kills.
2.  **Clipboard System:** Implement long-press action on the calculator display to copy the active result or expression to the Android clipboard.
3.  **Dark Mode:** True OLED-black dark theme mapping (the app is currently locked to the polished light mode).
4.  **Test Coverage:** Adopt `Robolectric` CUJ testing for calculation models and `Roborazzi` screenshot tests for the specialized UI.

---

## ⚙️ How to Clone and Build

1. **Clone** the repository to your local machine.
2. **Open** via Android Studio (Koala or newer highly recommended for native Compose support).
3. **Sync Gradle** (Ensure dependencies like `EvalEx`, `Navigation`, and `Compose` resolve smoothly).
4. **Compile targets:** Ensure your IDE targets minSdk 26+ and targetSdk 34+.
5. **Run:** Execute the application on a *physical device* for accurate haptics and interaction performance evaluation (Emulators often drop frames on spring physics testing).
