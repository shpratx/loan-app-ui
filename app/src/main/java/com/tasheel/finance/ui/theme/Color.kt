package com.tasheel.finance.ui.theme

import androidx.compose.ui.graphics.Color

// ═══════════════════════════════════════════════════════════
// Design System Tokens — aligned to components SCSS
// Source: /components/cards/cards.component.scss, /components/input/input.component.scss,
//         /components/button/button.component.scss, /deploy/index.html
// ═══════════════════════════════════════════════════════════

// Primary
val TasheelPrimary = Color(0xFF4F6EF7)        // --primary-action / --button-primary-bg-action
val TasheelPrimaryHover = Color(0xFF3B5AE0)   // --primary-hover
val TasheelPrimaryActive = Color(0xFF2D4ACC)   // --primary-active
val TasheelPrimarySubtle = Color(0xFFEEF1FE)  // --primary-subtle

// Semantic
val TasheelSuccess = Color(0xFF2ECC71)         // --success / --icons-success
val TasheelSuccessBg = Color(0xFFE8FAF0)       // --success-bg
val TasheelWarning = Color(0xFFF39C12)         // --warning / --icons-warning
val TasheelWarningBg = Color(0xFFFEF9E7)       // --warning-bg
val TasheelError = Color(0xFFE74C3C)           // --danger / --button-secondary-label-warning
val TasheelErrorBg = Color(0xFFFDEDEC)         // --danger-bg

// Neutrals (from --Neutral-N-xxx in cards.component.scss)
val Neutral100 = Color(0xFFEDEDF3)             // --Neutral-N-100 (light surface/bg)
val Neutral200 = Color(0xFFD5D7E2)             // --neutral-200 (borders)
val Neutral400 = Color(0xFF858AAD)             // --neutral-400 / --border-input (#858aad)
val Neutral600 = Color(0xFF5A5F7D)             // --neutral-600 (secondary text)
val Neutral900 = Color(0xFF292C3D)             // --Neutral-N-1000 (primary text)
val Neutral1050 = Color(0xFF14161F)            // --Neutral-N-1050 (dark bg)

// Light theme
val LightPrimary = TasheelPrimary
val LightSecondary = TasheelPrimaryHover
val LightBackground = Color(0xFFF4F5F9)        // --bg
val LightSurface = Color.White                  // --surface
val LightSurfaceElevated = Color(0xFFF8F9FC)   // --surface-elevated
val LightOnPrimary = Color.White                // --text-white
val LightOnSurface = Neutral900                 // --text-primary (#292c3d)
val LightOnBackground = Neutral900              // --text-primary
val LightOnSurfaceVariant = Neutral600          // --text-secondary (#5a5f7d)
val LightOutline = Neutral200                   // --border (#d5d7e2)
val LightOutlineVariant = Neutral400            // --border-input (#858aad)

// Dark theme
val DarkPrimary = Color(0xFF8DA4FF)
val DarkSecondary = Color(0xFF6B8AFF)
val DarkBackground = Neutral1050                // --Neutral-N-1050
val DarkSurface = Color(0xFF1E2030)             // --neutral-1000
val DarkOnPrimary = Color(0xFF002366)
val DarkOnSurface = Neutral100                  // --Neutral-N-100
val DarkOnBackground = Neutral100
val DarkOnSurfaceVariant = Neutral400
val DarkOutline = Neutral600
val DarkOutlineVariant = Neutral400
