package com.ratioapp.libs

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

fun compressAndConvertToMultipart(
    context: Context,
    uri: Uri,
    partName: String,
    quality: Int = 60
): MultipartBody.Part? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.use { stream ->
            val bitmap = BitmapFactory.decodeStream(stream)
            val byteArrayOutputStream = ByteArrayOutputStream()

            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)

            val requestFile = RequestBody.create(
                "image/jpeg".toMediaTypeOrNull(),
                byteArrayOutputStream.toByteArray())

            return MultipartBody.Part.createFormData(partName, "image.jpeg", requestFile)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}