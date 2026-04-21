package com.tasheel.finance.ui.theme

import androidx.compose.ui.unit.dp

// Spacing scale aligned to design system: 4dp base grid
// --sp-1:4px, --sp-2:8px, --sp-3:12px, --sp-4:16px, --sp-5:20px, --sp-6:24px, --sp-8:32px, --sp-10:40px
object Dimens {
    val xs = 4.dp       // --sp-1 / --spacing-1x
    val sm = 8.dp       // --sp-2 / --spacing-2x
    val md = 12.dp      // --sp-3 / --spacing-3x
    val lg = 16.dp      // --sp-4 / --spacing-4x
    val xl = 20.dp      // --sp-5 / --spacing-5x
    val xxl = 24.dp     // --sp-6 / --spacing-6x
    val xxxl = 32.dp    // --sp-8 / --spacing-8x
    val xxxxl = 40.dp   // --sp-10 / --spacing-10x
    val minTouchTarget = 48.dp  // WCAG 2.1 AA / EA7

    // Border radius aligned to design system
    // --radius-sm:4px, --radius-md:6px, --radius-lg:8px, --radius-xl:12px
    val radiusSm = 4.dp   // buttons
    val radiusMd = 6.dp   // cards (--awe-card-border-radius: 6px)
    val radiusLg = 8.dp   // inputs (--3x: 8px from input.component.scss)
    val radiusXl = 12.dp  // modals
    val radiusXxl = 16.dp // bottom sheets

    // Input height from input.component.scss: height: 60px
    val inputHeight = 48.dp // mobile-optimized (60px is web, 48dp is mobile touch target)
}
