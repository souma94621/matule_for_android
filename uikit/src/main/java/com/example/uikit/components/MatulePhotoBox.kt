package com.example.uikit.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.uikit.theme.MatuleSurfaceMuted
import com.example.uikit.theme.MatuleTextSecondary

/**
 * Квадрат для фото: пока фото нет, показывает «+», потом само фото.
 *
 * @param photo выбранное фото или null
 * @param onClick вызывается при нажатии (открыть выбор фото)
 * @param modifier модификатор для внешнего управления
 */
@Composable
fun MatulePhotoBox(
    photo: ImageBitmap?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .size(190.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MatuleSurfaceMuted)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        if (photo != null) {
            Image(
                bitmap = photo,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Text(text = "+", fontSize = 48.sp, color = MatuleTextSecondary)
        }
    }
}