package com.tasheel.finance.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.tasheel.finance.domain.model.ApplicationStatus
import com.tasheel.finance.ui.theme.*

// Badge aligned to badges.component.scss:
// border-radius: round (9999dp), success/warning/danger variants

@Composable
fun StatusBadge(status: ApplicationStatus) {
    val (bg, fg) = when (status) {
        ApplicationStatus.APPROVED, ApplicationStatus.DISBURSED -> TasheelSuccessBg to TasheelSuccess
        ApplicationStatus.SUBMITTED, ApplicationStatus.UNDER_REVIEW -> TasheelPrimarySubtle to TasheelPrimary
        ApplicationStatus.REJECTED -> TasheelErrorBg to TasheelError
        ApplicationStatus.DRAFT -> Neutral100 to Neutral600
    }
    Text(
        text = status.label,
        style = MaterialTheme.typography.labelSmall,
        color = fg,
        modifier = Modifier.clip(RoundedCornerShape(Dimens.radiusXl)).background(bg).padding(horizontal = Dimens.md, vertical = Dimens.xs),
    )
}

@Preview @Composable private fun PreviewApproved() = TasheelTheme { StatusBadge(ApplicationStatus.APPROVED) }
@Preview @Composable private fun PreviewDraft() = TasheelTheme { StatusBadge(ApplicationStatus.DRAFT) }
