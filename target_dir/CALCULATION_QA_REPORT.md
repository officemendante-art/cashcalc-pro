# Calculation Engine QA Report

## Executive Summary
The previous hand-written expression parser has been completely replaced with a highly robust, professional **BigDecimal**-safe calculation engine powered by **EvalEx** (`ezylang/EvalEx`). 

This upgrade solves the classic IEEE-754 binary floating-point representation limits of traditional floating-point types (`Double`/`Float`), ensuring precision-critical accuracy for decimal arithmetic, clean financial calculations, and secure boundary-handling during division or invalid inputs.

---

## Architectural Enhancements

| Feature | Previous Parser (Stack-based) | New Engine (EvalEx + BigDecimal) |
| :--- | :--- | :--- |
| **Precision Model** | Binary Floating-Point (`Double` / binary representation). | Arbitrary-precision decimal arithmetic (`BigDecimal`). |
| **Parsing Technique** | Hand-written token splitting & manual dual stacks. | Full-scale AST compilation with robust validation. |
| **Percentage Behavior** | Hardcoded/unstable single-operator lookups. | Dynamic, contextual percentage translation (`X + Y%` -> percentage of base; `X * Y%` -> standard decimal fraction). |
| **Sequential Operators** | Broken/Crashed with consecutive symbols. | Automatic multi-pass normalization of consecutive key inputs. |
| **Trailing Operators** | Threw exceptions or returned partial NaN values. | Intelligent auto-stripping of incomplete trailing formula symbols. |
| **Divide by Zero** | Yielded `Infinity`/`NaN` or arbitrary crashes. | Strict `ArithmeticException` detection, rendering clean `"Error: Div by 0"`. |

---

## QA Reference Test Case Validations

The following test suite cases are implemented and verified within `CalculatorEngineTest.kt`:

### 1. Precision & Decimal Calculations
* **Input**: `0.1 + 0.2`
* **Result**: `0.3`
* **Technical Detail**: Standard floating points produce `0.30000000000000004` due to binary fraction rounding. EvalEx with BigDecimal evaluates it exactly as `0.3`.

### 2. Precise Fraction Rounding
* **Input**: `1 / 3`
* **Result**: `0.333333333333` (Scaled cleanly to 12 decimal places with trailing zeros stripped).

### 3. Operator Precedence (with Screen Symbols)
* **Input**: `2 + 3 × 4` (Pre-processed to `2 + 3 * 4`)
* **Result**: `14` (Multiplication correctly takes precedence over addition).

### 4. Divide by Zero Protection
* **Input**: `10 ÷ 0` (Pre-processed to `10 / 0`)
* **Result**: `"Error: Div by 0"`
* **Technical Detail**: The engine traps division by zero prior to execution and returns a helpful localization-safe error.

### 5. Standalone Percentage
* **Input**: `50%`
* **Result**: `0.5`

### 6. Dynamic Context-Based Percentage
* **Input**: `100 + 10%`
* **Result**: `110` (Evaluated correctly as `100 + (100 * 10 / 100)`).

### 7. Big Number Limits
* **Input**: `999999999 + 1`
* **Result**: `1000000000`

### 8. Resilient Parsing & Incomplete Inputs
* **Inputs**:
  * `100+` behaves as `100` (trailing operator dropped)
  * `100 ++ 2` behaves as `102` (repeated operators normalized)
  * `100 +- 2` behaves as `98` (signs simplified correctly)

---

## Conclusion
The calculation module of **CashCalc Pro** is now exceptionally stable, reliable, and behaves identical to standard high-end physical calculators. All unit tests build successfully and assert exact mathematical behavior.
