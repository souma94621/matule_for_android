package com.example.myapplication.presentation.createproject

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import com.example.myapplication.domain.model.PhotoData
import java.io.ByteArrayOutputStream

/** Превращает выбранное фото в сжатый JPEG, чтобы на сервер не уходили десятки мегабайт. */
object PhotoReader {
    private const val MAX_SIDE = 1600

    /** Читает фото из галереи по адресу; null при ошибке. */
    fun fromUri(context: Context, uri: Uri): PhotoData? = try {
        val source = ImageDecoder.createSource(context.contentResolver, uri)
        val bitmap = ImageDecoder.decodeBitmap(source) { decoder, info, _ ->
            val scale = MAX_SIDE.toFloat() / maxOf(info.size.width, info.size.height)
            if (scale < 1f) {
                decoder.setTargetSize((info.size.width * scale).toInt(), (info.size.height * scale).toInt())
            }
            decoder.allocator = ImageDecoder.ALLOCATOR_SOFTWARE
        }
        fromBitmap(bitmap)
    } catch (e: Exception) {
        null
    }

    /** Сжимает готовый Bitmap (например, снимок с камеры). */
    fun fromBitmap(bitmap: Bitmap): PhotoData {
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, out)
        return PhotoData(out.toByteArray())
    }
}