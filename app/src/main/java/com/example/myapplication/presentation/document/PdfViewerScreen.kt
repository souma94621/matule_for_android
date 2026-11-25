package com.example.myapplication.presentation.document

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color as AndroidColor
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.uikit.components.MatuleHeader
import com.example.uikit.theme.MatuleError
import com.example.uikit.theme.MatulePrimary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Просмотр PDF-документа из папки assets.
 *
 * @param assetName имя файла в assets (например «privacy_policy.pdf»)
 * @param title заголовок экрана
 * @param onBack вызывается при нажатии «Назад»
 */
@Composable
fun PdfViewerScreen(
    assetName: String,
    title: String,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var pages by remember(assetName) { mutableStateOf<List<ImageBitmap>?>(null) }
    var failed by remember(assetName) { mutableStateOf(false) }

    LaunchedEffect(assetName) {
        val rendered = withContext(Dispatchers.IO) { renderPdf(context, assetName) }
        if (rendered == null) failed = true else pages = rendered
    }

    Column(modifier = modifier) {
        MatuleHeader(
            title = title,
            action = { TextButton(onClick = onBack) { Text("Назад", color = MatulePrimary) } }
        )
        val loaded = pages
        when {
            failed -> Text(
                text = "Не удалось открыть документ",
                color = MatuleError,
                modifier = Modifier.padding(16.dp)
            )
            loaded == null -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MatulePrimary)
            }
            else -> LazyColumn(contentPadding = PaddingValues(16.dp)) {
                items(loaded) { page ->
                    Image(
                        bitmap = page,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/** Рисует страницы PDF в картинки. Возвращает null при ошибке. */
private fun renderPdf(context: Context, assetName: String): List<ImageBitmap>? = try {
    val file = File(context.cacheDir, assetName)
    context.assets.open(assetName).use { input ->
        file.outputStream().use { output -> input.copyTo(output) }
    }
    ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
        PdfRenderer(fd).use { renderer ->
            (0 until renderer.pageCount).map { index ->
                renderer.openPage(index).use { page ->
                    val width = 1080
                    val height = (width.toFloat() / page.width * page.height).toInt()
                    val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                    bitmap.eraseColor(AndroidColor.WHITE)
                    page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
                    bitmap.asImageBitmap()
                }
            }
        }
    }
} catch (e: Exception) {
    null
}