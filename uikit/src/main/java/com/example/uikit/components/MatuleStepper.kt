package com.example.uikit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.theme.MatulePrimary
import com.example.uikit.theme.MatuleSurfaceMuted

/**
 * Счётчик количества: «−», число, «+».
 *
 * @param value текущее количество
 * @param onMinus вызывается при нажатии на «−»
 * @param onPlus вызывается при нажатии на «+»
 */
@Composable
fun MatuleStepper(
    value: Int,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StepperKey("−", onMinus)
        Text(text = value.toString(), fontSize = 18.sp)
        StepperKey("+", onPlus)
    }
}

@Composable
private fun StepperKey(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(MatuleSurfaceMuted)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, fontSize = 20.sp, color = MatulePrimary)
    }
}